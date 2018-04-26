/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological;

import implementation.GeometryWrapper;
import implementation.vocabulary.Geo;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.iterator.QueryIterNullIterator;
import org.apache.jena.sparql.engine.iterator.QueryIterSingleton;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.vocabulary.RDF;

/**
 *
 *
 */
public abstract class GenericGeometryPropertyFunction extends PFuncSimple {

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        Graph graph = execCxt.getActiveGraph();

        if (subject.isURI()) {
            //Subject is bound

            //Ensure that the subject is a Geometry, otherwise exit quickly.
            if (!graph.contains(subject, RDF.type.asNode(), Geo.GEOMETRY_NODE)) {
                return QueryIterNullIterator.create(execCxt);
            }

            Model model = ModelFactory.createModelForGraph(graph);
            Resource subjectRes = ResourceFactory.createResource(subject.getURI());

            //Check for the asserted value and return if found.
            Property predicateProp = ResourceFactory.createProperty(predicate.getURI());
            if (model.contains(subjectRes, predicateProp)) {
                RDFNode result = model.getRequiredProperty(subjectRes, predicateProp).getObject();
                return QueryIterSingleton.create(binding, Var.alloc(object.getName()), result.asNode(), execCxt);
            }

            //Check that the Geometry has a serialisation to use.
            if (model.contains(subjectRes, Geo.HAS_SERIALIZATION_PROP)) {
                Literal geomLiteral = model.getProperty(subjectRes, Geo.HAS_SERIALIZATION_PROP).getLiteral();
                GeometryWrapper geometryWrapper = GeometryWrapper.extract(geomLiteral);
                Literal result = applyPredicate(geometryWrapper);
                return QueryIterSingleton.create(binding, Var.alloc(object.getName()), result.asNode(), execCxt);
            } else {
                return QueryIterNullIterator.create(execCxt);
            }

        } else {
            //Subject is unbound. Exit quickly.
            return QueryIterNullIterator.create(execCxt);
        }

    }

    protected abstract Literal applyPredicate(GeometryWrapper geometryWrapper);

}
