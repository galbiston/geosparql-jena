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
package io.github.galbiston.geosparql_jena.geo.topological;

import io.github.galbiston.geosparql_jena.geof.topological.GenericFilterFunction;
import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.index.QueryRewriteIndex;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
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
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.sparql.util.FmtUtils;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;
import org.locationtech.jts.geom.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

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
        QueryRewriteIndex queryRewriteIndex;
        if (SpatialIndex.isDefined(execCxt)) {
            SpatialIndex spatialIndex = SpatialIndex.retrieve(execCxt);
            queryRewriteIndex = spatialIndex.getQueryRewriteIndex();
        } else {
            queryRewriteIndex = null;
        }
        Boolean isPositiveResult = queryRewrite(graph, subject, predicate, object, queryRewriteIndex);

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
        Var subjectVar = Var.alloc(subject.getName());

        Graph graph = execCxt.getActiveGraph();

        ExtendedIterator<Triple> spatialObjects = graph.find(null, RDF.type.asNode(), Geo.SPATIAL_OBJECT_NODE);

        //Bind all the Spatial Objects once as the subject and search for corresponding Objects.
        while (spatialObjects.hasNext()) {
            Triple spatialObjectTriple = spatialObjects.next();
            Node boundSubject = spatialObjectTriple.getSubject();
            Binding subjectBind = BindingFactory.binding(binding, subjectVar, boundSubject);
            QueryIterator queryIter = oneBound(subjectBind, boundSubject, predicate, object, execCxt);
            queryIterConcat.add(queryIter);
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
            //Bound node is not a Feature or a Geometry so exit.
            return QueryIterNullIterator.create(execCxt);
        }

        boolean isSpatialIndex = SpatialIndex.isDefined(execCxt);
        QueryIterConcat queryIterConcat;
        if (!isSpatialIndex || filterFunction.isDisjoint() || filterFunction.isDisconnected()) {
            //Disjointed so retrieve all cases.
            queryIterConcat = findAll(graph, boundNode, unboundNode, binding, isSubjectBound, predicate, execCxt);
        } else {
            //Only retrieve those in the spatial index which are within same bounding box.
            queryIterConcat = findIndex(graph, boundNode, unboundNode, binding, isSubjectBound, predicate, execCxt);
        }

        return queryIterConcat;
    }

    private QueryIterConcat findAll(Graph graph, Node boundNode, Node unboundNode, Binding binding, boolean isSubjectBound, Node predicate, ExecutionContext execCxt) {

        //Prepare the results.
        Var unboundVar = Var.alloc(unboundNode.getName());
        QueryIterConcat queryIterConcat = new QueryIterConcat(execCxt);

        //Search for both Features and Geometry in the Graph.
        ExtendedIterator<Triple> unboundTriples = graph.find(null, RDF.type.asNode(), Geo.SPATIAL_OBJECT_NODE);
        while (unboundTriples.hasNext()) {
            Triple unboundTriple = unboundTriples.next();
            Node spatialNode = unboundTriple.getSubject();
            Binding newBind = BindingFactory.binding(binding, unboundVar, spatialNode);
            QueryIterator queryIter;
            if (isSubjectBound) {
                queryIter = bothBound(newBind, boundNode, predicate, spatialNode, execCxt);
            } else {
                queryIter = bothBound(newBind, spatialNode, predicate, boundNode, execCxt);
            }
            queryIterConcat.add(queryIter);
        }

        return queryIterConcat;
    }

    private QueryIterConcat findIndex(Graph graph, Node boundNode, Node unboundNode, Binding binding, boolean isSubjectBound, Node predicate, ExecutionContext execCxt) {

        try {
            //Prepare for results.
            Var unboundVar = Var.alloc(unboundNode.getName());
            QueryIterConcat queryIterConcat = new QueryIterConcat(execCxt);

            //Find the asserted triples.
            List<Node> assertedNodes = findAsserted(graph, boundNode, isSubjectBound, predicate);
            for (Node node : assertedNodes) {
                Binding newBind = BindingFactory.binding(binding, unboundVar, node);
                QueryIterator queryIter = QueryIterSingleton.create(newBind, execCxt);
                queryIterConcat.add(queryIter);
            }

            //Find the GeometryLiteral of the Bound Node.
            SpatialObjectGeometryLiteral boundGeometryLiteral = SpatialObjectGeometryLiteral.retrieve(graph, boundNode);
            if (!boundGeometryLiteral.isValid()) {
                //Bound Node is not a Feature or a Geometry or there is no GeometryLiteral so exit.
                return queryIterConcat;
            }

            Node geometryLiteral = boundGeometryLiteral.getGeometryLiteral();

            //Perform the search of the Spatial Index of the Dataset.
            SpatialIndex spatialIndex = SpatialIndex.retrieve(execCxt);
            GeometryWrapper geom = GeometryWrapper.extract(geometryLiteral);
            GeometryWrapper transformedGeom = geom.transform(spatialIndex.getSrsInfo());
            Envelope searchEnvelope = transformedGeom.getEnvelope();
            HashSet<Resource> features = spatialIndex.query(searchEnvelope);

            //Check each of the Features that match the search.
            for (Resource feature : features) {
                Node featureNode = feature.asNode();

                //Ensure not already an asserted node.
                if (!assertedNodes.contains(featureNode)) {

                    Binding newBind = BindingFactory.binding(binding, unboundVar, featureNode);
                    QueryIterator queryIter;
                    if (isSubjectBound) {
                        queryIter = bothBound(newBind, boundNode, predicate, featureNode, execCxt);
                    } else {
                        queryIter = bothBound(newBind, featureNode, predicate, boundNode, execCxt);
                    }
                    queryIterConcat.add(queryIter);
                }

                //Also test all Geometry of the Features. All, some or one Geometry may have matched.
                ExtendedIterator<Triple> featureGeometryTriples = graph.find(feature.asNode(), Geo.HAS_GEOMETRY_NODE, null);
                while (featureGeometryTriples.hasNext()) {
                    Triple unboundTriple = featureGeometryTriples.next();
                    Node geomNode = unboundTriple.getObject();

                    //Ensure not already an asserted node.
                    if (!assertedNodes.contains(geomNode)) {
                        Binding newBind = BindingFactory.binding(binding, unboundVar, geomNode);
                        QueryIterator queryIter;
                        if (isSubjectBound) {
                            queryIter = bothBound(newBind, boundNode, predicate, geomNode, execCxt);
                        } else {
                            queryIter = bothBound(newBind, geomNode, predicate, boundNode, execCxt);
                        }
                        queryIterConcat.add(queryIter);
                    }
                }
            }

            return queryIterConcat;
        } catch (MismatchedDimensionException | TransformException | FactoryException ex) {
            throw new ExprEvalException(ex.getMessage() + ": " + FmtUtils.stringForNode(boundNode) + ", " + FmtUtils.stringForNode(unboundNode) + ", " + FmtUtils.stringForNode(predicate), ex);
        }
    }

    private List<Node> findAsserted(Graph graph, Node boundNode, boolean isSubjectBound, Node predicate) {
        List<Node> assertedNodes = new ArrayList<>();
        if (isSubjectBound) {
            ExtendedIterator<Triple> assertedTriples = graph.find(boundNode, predicate, null);
            while (assertedTriples.hasNext()) {
                Node assertedNode = assertedTriples.next().getObject();
                assertedNodes.add(assertedNode);
            }
        } else {
            ExtendedIterator<Triple> assertedTriples = graph.find(null, predicate, boundNode);
            while (assertedTriples.hasNext()) {
                Node assertedNode = assertedTriples.next().getSubject();
                assertedNodes.add(assertedNode);
            }
        }
        return assertedNodes;
    }

    protected final Boolean queryRewrite(Graph graph, Node subject, Node predicate, Node object, QueryRewriteIndex queryRewriteIndex) {

        if (graph.contains(subject, predicate, object)) {
            //The graph contains the asserted triple, return the current binding.
            return true;
        }

        //If query re-writing is disabled then exit - graph does not contain the asserted relation.
        //May be null if there is no SpatialIndex created and to avoid constant recreation.
        if (queryRewriteIndex == null || !queryRewriteIndex.isIndexActive()) {
            return false;
        }

        //Begin Query Re-write by finding the literals of the Feature or Geometry.
        SpatialObjectGeometryLiteral subjectSpatialLiteral = SpatialObjectGeometryLiteral.retrieve(graph, subject);
        if (!subjectSpatialLiteral.isValid()) {
            //Subject is not a Feature or a Geometry or there is no GeometryLiteral so exit.
            return false;
        }

        SpatialObjectGeometryLiteral objectSpatialLiteral = SpatialObjectGeometryLiteral.retrieve(graph, object);
        if (!objectSpatialLiteral.isValid()) {
            //Object is not a Feature or a Geometry or there is no GeometryLiteral so exit.
            return false;
        }

        //Check the QueryRewriteIndex for the result.
        Property predicateProp = ResourceFactory.createProperty(predicate.getURI());
        Boolean isPositive = queryRewriteIndex.test(subjectSpatialLiteral.getGeometryLiteral(), predicateProp, objectSpatialLiteral.getGeometryLiteral(), this);
        return isPositive;
    }

    public Boolean testFilterFunction(Node subjectGeometryLiteral, Node objectGeometryLiteral) {
        return filterFunction.exec(subjectGeometryLiteral, objectGeometryLiteral);
    }

}
