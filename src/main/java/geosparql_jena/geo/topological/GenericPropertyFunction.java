/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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
package geosparql_jena.geo.topological;

import geosparql_jena.geof.topological.GenericFilterFunction;
import geosparql_jena.configuration.GeoSPARQLConfig;
import geosparql_jena.implementation.index.QueryRewriteIndex;
import geosparql_jena.implementation.vocabulary.Geo;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
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
        if (object.isLiteral()) {
            //These Property Functions do not accept literals as objects so exit quickly.
            return QueryIterNullIterator.create(execCxt);
        }

        if (subject.isConcrete() && object.isConcrete()) {
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
        Boolean isPositiveResult = queryRewrite(graph, subject, predicate, object);

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
        if (subject.isConcrete()) {
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
     * @param graph
     * @param targetSpatialObject
     * @return
     */
    protected static final SpatialObjectGeometryLiteral retrieveGeometryLiteral(Graph graph, Node targetSpatialObject) {

        Node geometry;
        if (graph.contains(targetSpatialObject, RDF.type.asNode(), Geo.FEATURE_NODE)) {
            //Target is Feature - find the default Geometry.
            ExtendedIterator<Triple> geomIter = graph.find(targetSpatialObject, Geo.HAS_DEFAULT_GEOMETRY_NODE, null);
            geometry = extractObject(geomIter);
        } else if (graph.contains(targetSpatialObject, RDF.type.asNode(), Geo.GEOMETRY_NODE)) {
            //Target is a Geometry.
            geometry = targetSpatialObject;
        } else {
            //Target is not a Feature or Geometry.
            geometry = null;
        }

        if (geometry != null) {
            //Find the Geometry Literal of the Geometry.
            ExtendedIterator<Triple> iter = graph.find(geometry, Geo.HAS_SERIALIZATION_NODE, null);
            Node literalNode = extractObject(iter);
            if (literalNode != null) {
                return new SpatialObjectGeometryLiteral(targetSpatialObject, literalNode);
            }
        }
        return null;

    }

    private static Node extractObject(ExtendedIterator<Triple> iter) {

        if (iter.hasNext()) {
            Triple triple = iter.next();
            return triple.getObject();
        } else {
            return null;
        }
    }

    protected Boolean queryRewrite(Graph graph, Node subject, Node predicate, Node object) {

        if (graph.contains(subject, predicate, object)) {
            //The graph contains the asserted triple, return the current binding.
            return true;
        }

        //If query re-writing is disabled then exit - graph does not contain the asserted relation.
        if (!GeoSPARQLConfig.isQueryRewriteEnabled()) {
            return false;
        }

        //Begin Query Re-write by finding the literals of the Feature or Geometry.
        SpatialObjectGeometryLiteral subjectSpatialLiteral = retrieveGeometryLiteral(graph, subject);
        if (subjectSpatialLiteral == null) {
            //Subject is not a Feature or a Geometry so exit.
            return false;
        }

        SpatialObjectGeometryLiteral objectSpatialLiteral = retrieveGeometryLiteral(graph, object);
        if (objectSpatialLiteral == null) {
            //Object is not a Feature or a Geometry so exit.
            return false;
        }

        //Check the QueryRewriteIndex for the result.
        Property predicateProp = ResourceFactory.createProperty(predicate.getURI());
        Boolean isPositive = QueryRewriteIndex.test(subjectSpatialLiteral.getGeometryLiteral(), predicateProp, objectSpatialLiteral.getGeometryLiteral(), this);
        return isPositive;
    }

    public Boolean testFilterFunction(Node subjectGeometryLiteral, Node objectGeometryLiteral) {
        return filterFunction.exec(subjectGeometryLiteral, objectGeometryLiteral);
    }

}
