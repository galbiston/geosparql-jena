/*
 * Copyright 2018 Greg Albiston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geo.topological;

import geof.topological.GenericFilterFunction;
import implementation.index.QueryRewriteIndex;
import implementation.vocabulary.Geo;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PropertyNotFoundException;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.binding.BindingFactory;
import org.apache.jena.sparql.engine.iterator.QueryIterConcat;
import org.apache.jena.sparql.engine.iterator.QueryIterNullIterator;
import org.apache.jena.sparql.engine.iterator.QueryIterSingleton;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;

/**
 *
 *
 *
 */
public abstract class GenericPropertyFunction extends PFuncSimple {

    private final GenericFilterFunction filterFunction;

    public GenericPropertyFunction(GenericFilterFunction filterFunction) {
        this.filterFunction = filterFunction;
    }

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {
        if (object.isLiteral() || subject.isBlank() || object.isBlank()) {
            //These Property Functions do not accept literals as objects or blank nodes so exit quickly.
            return QueryIterNullIterator.create(execCxt);
        }

        if (subject.isURI() && object.isURI()) {
            //Both are bound.
            return bothBound(binding, subject, predicate, object, execCxt);
        } else if (subject.isVariable() && object.isVariable()) {
            //Both are unbound.
            return bothUnbound(binding, subject, predicate, object, execCxt);
        } else {
            //One bound and one unbound.
            return oneBound(binding, subject, predicate, object, execCxt);
        }

    }

    private QueryIterator bothBound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        Graph graph = execCxt.getActiveGraph();
        if (graph.contains(subject, predicate, object)) {
            //The graph contains the asserted triple, return the current binding.
            return QueryIterSingleton.create(binding, execCxt);
        }

        Model model = ModelFactory.createModelForGraph(graph);
        Resource subjectSpatialObject = ResourceFactory.createResource(subject.getURI());
        SpatialObjectGeometryLiteral subjectSpatialLiteral = retrieveGeometryLiteral(model, subjectSpatialObject);
        if (subjectSpatialLiteral == null) {
            //Subject is not a Feature or a Geometry so exit.
            return QueryIterNullIterator.create(execCxt);
        }

        Resource objectSpatialObject = ResourceFactory.createResource(object.getURI());
        SpatialObjectGeometryLiteral objectSpatialLiteral = retrieveGeometryLiteral(model, objectSpatialObject);
        if (objectSpatialLiteral == null) {
            //Object is not a Feature or a Geometry so exit.
            return QueryIterNullIterator.create(execCxt);
        }

        //Check the QueryRewriteIndex for the result.
        Property predicateProp = ResourceFactory.createProperty(predicate.getURI());
        Boolean isPositiveResult = QueryRewriteIndex.test(subjectSpatialLiteral.getGeometryLiteral(), predicateProp, objectSpatialLiteral.getGeometryLiteral(), this);
        if (isPositiveResult) {
            //Filter function test succeded so retain binding.
            return QueryIterSingleton.create(binding, execCxt);
        } else {
            //Filter function test failed so null result.
            return QueryIterNullIterator.create(execCxt);
        }

    }

    private QueryIterator bothUnbound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        QueryIterConcat queryIterConcat = new QueryIterConcat(execCxt);

        Graph graph = execCxt.getActiveGraph();

        ExtendedIterator<Triple> spatialObjects = graph.find(null, RDF.type.asNode(), Geo.SPATIAL_OBJECT_NODE);
        List<Triple> subjectTriples = spatialObjects.toList();
        List<Triple> objectTriples = new ArrayList<>(subjectTriples);

        Var subjectVar = Var.alloc(subject.getName());
        Var objectVar = Var.alloc(object.getName());
        for (Triple subjectTriple : subjectTriples) {
            for (Triple objectTriple : objectTriples) {
                Binding startBind = BindingFactory.binding(binding, subjectVar, subjectTriple.getSubject());
                Binding newBind = BindingFactory.binding(startBind, objectVar, objectTriple.getSubject());
                QueryIterator queryIter = bothBound(newBind, subjectTriple.getSubject(), predicate, objectTriple.getSubject(), execCxt);
                queryIterConcat.add(queryIter);
            }
        }

        return queryIterConcat;
    }

    private QueryIterator oneBound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        Graph graph = execCxt.getActiveGraph();
        Node boundNode;
        Node unboundNode;
        Boolean isSubjectBound;
        if (subject.isURI()) {
            //Subject is bound, object is unbound.
            boundNode = subject;
            unboundNode = object;
            isSubjectBound = true;
        } else {
            //Object is bound, subject is unbound.
            boundNode = object;
            unboundNode = subject;
            isSubjectBound = false;
        }

        if (!graph.contains(boundNode, RDF.type.asNode(), Geo.SPATIAL_OBJECT_NODE)) {
            //Subject is not a Feature or a Geometry so exit.
            return QueryIterNullIterator.create(execCxt);
        }

        ExtendedIterator<Triple> unboundTriples = graph.find(null, RDF.type.asNode(), Geo.SPATIAL_OBJECT_NODE);
        Var unboundVar = Var.alloc(unboundNode.getName());
        QueryIterConcat queryIterConcat = new QueryIterConcat(execCxt);
        while (unboundTriples.hasNext()) {
            Triple unboundTriple = unboundTriples.next();
            Binding newBind = BindingFactory.binding(binding, unboundVar, unboundTriple.getSubject());
            QueryIterator queryIter;
            if (isSubjectBound) {
                queryIter = bothBound(newBind, boundNode, predicate, unboundTriple.getSubject(), execCxt);
            } else {
                queryIter = bothBound(newBind, unboundTriple.getSubject(), predicate, boundNode, execCxt);
            }
            queryIterConcat.add(queryIter);
        }

        return queryIterConcat;
    }

    /**
     * Retrieve the default geometry for Feature or Geometry.
     *
     * @param model
     * @param targetSpatialObject
     * @return
     */
    private SpatialObjectGeometryLiteral retrieveGeometryLiteral(Model model, Resource targetSpatialObject) {

        try {
            if (model.contains(targetSpatialObject, RDF.type, Geo.FEATURE_RES)) {
                Resource geometry = model.getRequiredProperty(targetSpatialObject, Geo.HAS_DEFAULT_GEOMETRY_PROP).getResource();
                Literal geometryLiteral = geometry.getRequiredProperty(Geo.HAS_SERIALIZATION_PROP).getLiteral();
                return new SpatialObjectGeometryLiteral(targetSpatialObject, geometryLiteral);
            } else if (model.contains(targetSpatialObject, RDF.type, Geo.GEOMETRY_RES)) {
                Literal geometryLiteral = model.getRequiredProperty(targetSpatialObject, Geo.HAS_SERIALIZATION_PROP).getLiteral();
                return new SpatialObjectGeometryLiteral(targetSpatialObject, geometryLiteral);
            }
        } catch (PropertyNotFoundException ex) {
            //LOGGER.warn("Property Not Found Exception: {} for {}", ex.getMessage(), targetSpatialObject);
            return null;
        }
        //Target resource isn't a Feature or Geometry so ignore.
        return null;

    }

    public Boolean testFilterFunction(Literal subjectGeometryLiteral, Literal objectGeometryLiteral) {
        return filterFunction.exec(subjectGeometryLiteral, objectGeometryLiteral);
    }

}
