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
import java.util.List;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
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

        if (subject.equals(object)) {
            //The subject and object are the same therefore always true, so exit.
            return QueryIterSingleton.create(binding, execCxt);
        }

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

        Property predicateProp = ResourceFactory.createProperty(predicate.getURI());
        if (testFilterFunction(subjectSpatialLiteral.getGeometryLiteral(), objectSpatialLiteral.getGeometryLiteral(), predicateProp, true)) {
            //Filter function test succeded so retain binding.
            return QueryIterSingleton.create(binding, execCxt);
        } else {
            //Filter function test failed so null result.
            return QueryIterNullIterator.create(execCxt);
        }

    }

    private QueryIterator bothUnbound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        Graph graph = execCxt.getActiveGraph();
        Model model = ModelFactory.createModelForGraph(graph);

        Boolean isSubjectBound = true; //For this purpose the subject is considered bound for passing to createBindings.

        //Find all possible SpatialObjects then copy for comparison.
        List<SpatialObjectGeometryLiteral> bounds = retrieveGeometryLiterals(model);
        List<SpatialObjectGeometryLiteral> unbounds = new ArrayList<>(bounds);

        //Create result bindings after checking in the index.
        Property predicateProp = ResourceFactory.createProperty(predicate.getURI());
        List<Binding> bindings = new ArrayList<>(bounds.size() * unbounds.size()); //Worst case is all are successful.
        for (SpatialObjectGeometryLiteral bound : bounds) {
            List<Binding> binds = createBindings(bound, unbounds, subject, object, model, binding, predicateProp, isSubjectBound);
            bindings.addAll(binds);
        }
        return new QueryIterPlainWrapper(bindings.iterator(), execCxt);

    }

    private QueryIterator oneBound(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {
        Graph graph = execCxt.getActiveGraph();
        Model model = ModelFactory.createModelForGraph(graph);
        Resource boundSpatialObject;
        Node boundNode;
        Node unboundNode;
        Boolean isSubjectBound;
        if (subject.isURI()) {
            //Subject is bound, object is unbound.
            boundSpatialObject = ResourceFactory.createResource(subject.getURI());
            boundNode = subject;
            unboundNode = object;
            isSubjectBound = true;
        } else {
            //Object is bound, subject is unbound.
            boundSpatialObject = ResourceFactory.createResource(object.getURI());
            boundNode = object;
            unboundNode = subject;
            isSubjectBound = false;
        }

        SpatialObjectGeometryLiteral bound = retrieveGeometryLiteral(model, boundSpatialObject);
        if (bound == null) {
            //Subject is not a Feature or a Geometry so exit.
            return QueryIterNullIterator.create(execCxt);
        }

        List<SpatialObjectGeometryLiteral> unbounds = retrieveGeometryLiterals(model);

        //Create result bindings after checking in the index.
        Property predicateProp = ResourceFactory.createProperty(predicate.getURI());
        List<Binding> bindings = createBindings(bound, unbounds, boundNode, unboundNode, model, binding, predicateProp, isSubjectBound);

        return new QueryIterPlainWrapper(bindings.iterator(), execCxt);
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
            LOGGER.error("Property Found Exception: {} for {}", ex.getMessage(), targetSpatialObject);
        }
        //Target resource isn't a Feature or Geometry so ignore.
        return null;
    }

    private List<SpatialObjectGeometryLiteral> retrieveGeometryLiterals(Model model) {

        //Features
        ResIterator features = model.listResourcesWithProperty(RDF.type, Geo.FEATURE_RES);
        List<SpatialObjectGeometryLiteral> spatialObjectLiterals = buildGeometryLiterals(features, model);

        //Geometeries
        ResIterator geometries = model.listResourcesWithProperty(RDF.type, Geo.GEOMETRY_RES);
        spatialObjectLiterals.addAll(buildGeometryLiterals(geometries, model));

        return spatialObjectLiterals;
    }

    private List<SpatialObjectGeometryLiteral> buildGeometryLiterals(ResIterator resIterator, Model model) {
        List<SpatialObjectGeometryLiteral> spatialObjectLiterals = new ArrayList<>();

        while (resIterator.hasNext()) {
            Resource spatialObject = resIterator.nextResource();
            SpatialObjectGeometryLiteral objectLiteral = retrieveGeometryLiteral(model, spatialObject);
            if (objectLiteral != null) {
                spatialObjectLiterals.add(objectLiteral);
            }
        }
        return spatialObjectLiterals;
    }

    /**
     * Target may be bound or unbound.
     *
     * @param target
     * @param unbounds
     * @param targetNode
     * @param unboundNode
     * @param model
     * @param binding
     * @param predicate
     * @param isTargetSubject
     * @return
     */
    private List<Binding> createBindings(SpatialObjectGeometryLiteral target, List<SpatialObjectGeometryLiteral> unbounds, Node targetNode, Node unboundNode, Model model, Binding binding, Property predicate, Boolean isTargetSubject) {

        List<Binding> bindings = new ArrayList<>();
        Var targetVar;
        if (targetNode.isVariable()) {
            targetVar = Var.alloc(targetNode.getName());
        } else {
            targetVar = null;
        }
        Var unboundVar = Var.alloc(unboundNode.getName());

        Resource targetSpatialObject = target.getSpatialObject();
        Literal targetGeometryLiteral = target.getGeometryLiteral();

        for (SpatialObjectGeometryLiteral unbound : unbounds) {
            Resource unboundSpatialObject = unbound.getSpatialObject();
            Literal unboundGeometryLiteral = unbound.getGeometryLiteral();
            boolean isPositiveResult;

            //Check for asserted statement according to whether or not the target pair are the subject.
            if (isTargetSubject) {
                isPositiveResult = model.contains(targetSpatialObject, predicate, unboundSpatialObject);
            } else {
                isPositiveResult = model.contains(unboundSpatialObject, predicate, targetSpatialObject);
            }
            if (!isPositiveResult) {
                //No asserted statement found.
                //Test the predicate for the target and unbound literals.
                isPositiveResult = testFilterFunction(targetGeometryLiteral, unboundGeometryLiteral, predicate, isTargetSubject);
            }

            if (isPositiveResult) {
                //The result is successful so return the binding.
                Binding unboundBind = BindingFactory.binding(binding, unboundVar, unboundSpatialObject.asNode());

                if (targetVar == null) {
                    //Target is bound so only unbound is used.
                    bindings.add(unboundBind);
                } else {
                    //Both target and unbound are used for cases when both are actually unbound.
                    Binding targetBind = BindingFactory.binding(unboundBind, targetVar, targetSpatialObject.asNode());
                    bindings.add(targetBind);
                }
            }
        }

        return bindings;
    }

    private Boolean testFilterFunction(Literal boundGeometryLiteral, Literal unboundGeometryLiteral, Property predicate, Boolean isSubjectBound) {
        //TODO pass the filter function and predicate to Query Rewrite Index for checking and storage. Use isSubjectBound to identify how to order in index, i.e. when true use boundGeometryLiteral as last key.
        return filterFunction.exec(boundGeometryLiteral, unboundGeometryLiteral);
    }

}
