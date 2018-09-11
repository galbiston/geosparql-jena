/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geo.topological;

import implementation.GeometryWrapper;
import implementation.vocabulary.Geo;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
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
 */
public abstract class GenericGeometryPropertyFunction extends PFuncSimple {

    protected abstract Literal applyPredicate(GeometryWrapper geometryWrapper);

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        if (subject.isBlank() || object.isBlank()) {
            //These Property Functions do not accept blank nodes so exit quickly.
            return QueryIterNullIterator.create(execCxt);
        }

        if (subject.isURI() && object.isLiteral()) {
            //Both are bound.
            return bothBound(binding, subject, predicate, object, execCxt);
        } else if (subject.isVariable() && object.isVariable()) {
            //Both are unbound.
            return bothUnbound(binding, subject, predicate, object, execCxt);
        } else if (subject.isURI() && object.isVariable()) {
            //Subject bound and object unbound.
            return objectUnbound(binding, subject, predicate, object, execCxt);
        } else {
            //Subject unbound and object bound.
            return subjectUnbound(binding, subject, predicate, object, execCxt);
        }

    }

    private Literal getGeometryLiteral(Node subject, Node predicate, ExecutionContext execCxt) {
        Graph graph = execCxt.getActiveGraph();

        if (!graph.contains(subject, RDF.type.asNode(), Geo.GEOMETRY_NODE)) {
            //Ensure that the subject is a Geometry, otherwise exit quickly.
            return null;
        }

        Model model = ModelFactory.createModelForGraph(graph);
        Resource subjectRes = ResourceFactory.createResource(subject.getURI());

        //Check for the asserted value and return if found.
        Property predicateProp = ResourceFactory.createProperty(predicate.getURI());
        if (model.contains(subjectRes, predicateProp)) {
            Literal geometryLiteral = model.getRequiredProperty(subjectRes, predicateProp).getLiteral();
            return geometryLiteral;
        }

        //Check that the Geometry has a serialisation to use.
        if (model.contains(subjectRes, Geo.HAS_SERIALIZATION_PROP)) {
            Literal geomLiteral = model.getProperty(subjectRes, Geo.HAS_SERIALIZATION_PROP).getLiteral();
            GeometryWrapper geometryWrapper = GeometryWrapper.extract(geomLiteral);
            Literal geometryLiteral = applyPredicate(geometryWrapper);
            return geometryLiteral;
        } else {
            return null;
        }
    }

    private QueryIterator bothBound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        Literal geometryLiteral = getGeometryLiteral(subject, predicate, execCxt);
        Literal objectLiteral = ResourceFactory.createTypedLiteral(object.getLiteral().getLexicalForm(), object.getLiteral().getDatatype());

        Boolean isEqual = objectLiteral.equals(geometryLiteral);

        if (isEqual) {
            return QueryIterSingleton.create(binding, execCxt);
        } else {
            return QueryIterNullIterator.create(execCxt);
        }
    }

    private QueryIterator subjectUnbound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {
        QueryIterConcat queryIterConcat = new QueryIterConcat(execCxt);

        Graph graph = execCxt.getActiveGraph();

        ExtendedIterator<Triple> subjectTriples = graph.find(null, RDF.type.asNode(), Geo.GEOMETRY_NODE);

        Var subjectVar = Var.alloc(subject.getName());
        while (subjectTriples.hasNext()) {
            Triple subjectTriple = subjectTriples.next();
            Binding newBind = BindingFactory.binding(binding, subjectVar, subjectTriple.getSubject());
            QueryIterator queryIter = bothBound(newBind, subjectTriple.getSubject(), predicate, object, execCxt);
            queryIterConcat.add(queryIter);
        }

        return queryIterConcat;
    }

    private QueryIterator objectUnbound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        Literal geometryLiteral = getGeometryLiteral(subject, predicate, execCxt);

        if (geometryLiteral != null) {
            return QueryIterSingleton.create(binding, Var.alloc(object.getName()), geometryLiteral.asNode(), execCxt);
        } else {
            return QueryIterNullIterator.create(execCxt);
        }
    }

    private QueryIterator bothUnbound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {
        QueryIterConcat queryIterConcat = new QueryIterConcat(execCxt);

        Graph graph = execCxt.getActiveGraph();

        ExtendedIterator<Triple> subjectTriples = graph.find(null, RDF.type.asNode(), Geo.GEOMETRY_NODE);

        Var subjectVar = Var.alloc(subject.getName());
        while (subjectTriples.hasNext()) {
            Triple subjectTriple = subjectTriples.next();
            Binding newBind = BindingFactory.binding(binding, subjectVar, subjectTriple.getSubject());
            QueryIterator queryIter = objectUnbound(newBind, subjectTriple.getSubject(), predicate, object, execCxt);
            queryIterConcat.add(queryIter);
        }

        return queryIterConcat;
    }

}
