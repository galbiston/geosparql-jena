/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological;

import geof.topological.GenericFilterFunction;
import implementation.vocabulary.Geo;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PropertyNotFoundException;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.binding.BindingFactory;
import org.apache.jena.sparql.engine.iterator.QueryIterNullIterator;
import org.apache.jena.sparql.engine.iterator.QueryIterPlainWrapper;
import org.apache.jena.sparql.engine.iterator.QueryIterSingleton;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 */
public abstract class GenericPropertyFunction extends PFuncSimple {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
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

        if (subject.isURI() && object.isURI()) {
            //Both are bound.
            return bothBound(binding, subject, predicate, object, execCxt);
        } else {
            return oneBound(binding, subject, predicate, object, execCxt);
        }

    }

    private QueryIterator bothBound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        if (subject.equals(object)) {
            //The subject and object are the same, so exit.
            return QueryIterSingleton.create(binding, execCxt);
        }

        Graph graph = execCxt.getActiveGraph();
        if (graph.contains(subject, predicate, object)) {
            //The graph contains the asserted triple, return the current binding.
            return QueryIterSingleton.create(binding, execCxt);
        }

        Model model = ModelFactory.createModelForGraph(graph);
        Resource subjectSpatialObject = ResourceFactory.createResource(subject.getURI());
        Literal subjectGeometryLiteral = retrieveGeometryLiteral(model, subjectSpatialObject);
        if (subjectGeometryLiteral == null) {
            //Subject is not a Feature or a Geometry so exit.
            return QueryIterNullIterator.create(execCxt);
        }

        Resource objectSpatialObject = ResourceFactory.createResource(object.getURI());
        Literal objectGeometryLiteral = retrieveGeometryLiteral(model, objectSpatialObject);
        if (objectGeometryLiteral == null) {
            //Object is not a Feature or a Geometry so exit.
            return QueryIterNullIterator.create(execCxt);
        }

        if (testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral, predicate, true)) {
            //Filter function test succeded so retain binding.
            return QueryIterSingleton.create(binding, execCxt);
        } else {
            //Filter function test failed so null result.
            return QueryIterNullIterator.create(execCxt);
        }

    }

    private QueryIterator oneBound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {
        Graph graph = execCxt.getActiveGraph();
        Model model = ModelFactory.createModelForGraph(graph);
        Resource boundSpatialObject;
        Node unboundNode;
        Boolean isSubjectBound;
        if (subject.isURI()) {
            //Subject is bound, object is unbound.
            boundSpatialObject = ResourceFactory.createResource(subject.getURI());
            unboundNode = object;
            isSubjectBound = true;
        } else {
            //Object is bound, subject is unbound.
            boundSpatialObject = ResourceFactory.createResource(object.getURI());
            unboundNode = subject;
            isSubjectBound = false;
        }

        Literal boundGeometryLiteral = retrieveGeometryLiteral(model, boundSpatialObject);
        if (boundGeometryLiteral == null) {
            //Subject is not a Feature or a Geometry so exit.
            return QueryIterNullIterator.create(execCxt);
        }

        HashMap<Resource, Literal> unboundSpatialObjectLiterals = retrieveGeometryLiterals(model);

        //Create result bindings after checking in the index.
        List<Binding> bindings = createBindings(boundGeometryLiteral, unboundSpatialObjectLiterals, unboundNode, binding, predicate, isSubjectBound);

        return new QueryIterPlainWrapper(bindings.iterator(), execCxt);
    }

    /**
     * Retrieve the default geometry for Feature or Geometry.
     *
     * @param model
     * @param targetSpatialObject
     * @return
     */
    private Literal retrieveGeometryLiteral(Model model, Resource targetSpatialObject) {

        try {
            if (model.contains(targetSpatialObject, RDF.type, Geo.FEATURE_RES)) {
                Resource geometry = model.getRequiredProperty(targetSpatialObject, Geo.HAS_DEFAULT_GEOMETRY_PROP).getResource();
                return geometry.getRequiredProperty(Geo.HAS_SERIALIZATION_PROP).getLiteral();
            } else if (model.contains(targetSpatialObject, RDF.type, Geo.GEOMETRY_RES)) {
                return model.getRequiredProperty(targetSpatialObject, Geo.HAS_SERIALIZATION_PROP).getLiteral();
            }
        } catch (PropertyNotFoundException ex) {
            LOGGER.error("Property Found Exception: {} for {}", ex.getMessage(), targetSpatialObject);
        }
        //Target resource isn't a Feature or Geometry so ignore.
        return null;
    }

    private HashMap<Resource, Literal> retrieveGeometryLiterals(Model model) {

        //Features
        ResIterator features = model.listResourcesWithProperty(RDF.type, Geo.FEATURE_RES);
        HashMap<Resource, Literal> spatialObjectLiterals = buildGeometryLiterals(features, model);

        //Geometeries
        ResIterator geometries = model.listResourcesWithProperty(RDF.type, Geo.GEOMETRY_RES);
        spatialObjectLiterals.putAll(buildGeometryLiterals(geometries, model));

        return spatialObjectLiterals;
    }

    private HashMap<Resource, Literal> buildGeometryLiterals(ResIterator resIterator, Model model) {
        HashMap<Resource, Literal> spatialObjectLiterals = new HashMap<>();

        while (resIterator.hasNext()) {
            Resource spatialObject = resIterator.nextResource();
            Literal geometryLiteral = retrieveGeometryLiteral(model, spatialObject);
            if (geometryLiteral != null) {
                spatialObjectLiterals.put(spatialObject, geometryLiteral);
            }

        }
        return spatialObjectLiterals;
    }

    private List<Binding> createBindings(Literal boundGeometryLiteral, HashMap<Resource, Literal> unboundSpatialObjectLiterals, Node unboundNode, Binding binding, Node predicate, Boolean isSubjectBound) {
        List<Binding> bindings = new ArrayList<>();
        Var unboundVar = Var.alloc(unboundNode.getName());

        for (Entry<Resource, Literal> unboundEntry : unboundSpatialObjectLiterals.entrySet()) {
            Resource spatialObject = unboundEntry.getKey();
            Literal unboundGeometryLiteral = unboundEntry.getValue();

            if (testFilterFunction(boundGeometryLiteral, unboundGeometryLiteral, predicate, isSubjectBound)) {
                Binding newBind = BindingFactory.binding(binding, unboundVar, spatialObject.asNode());
                bindings.add(newBind);
            }
        }

        return bindings;
    }

    private Boolean testFilterFunction(Literal boundGeometryLiteral, Literal unboundGeometryLiteral, Node predicate, Boolean isSubjectBound) {
        //TODO pass the filter function and predicate to Query Rewrite Index for checking and storage. Use isSubjectBound to identify how to order in index, i.e. when true use boundGeometryLiteral as last key.
        return filterFunction.exec(boundGeometryLiteral, unboundGeometryLiteral);
    }

}
