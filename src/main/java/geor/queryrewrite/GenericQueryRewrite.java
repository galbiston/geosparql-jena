/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geor.queryrewrite;

import implementation.vocabulary.Geo;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpUnion;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.main.QC;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.vocabulary.RDF;

/**
 *
 * @author Greg
 */
public class GenericQueryRewrite extends PFuncSimple {

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        //Feature - Feature rule
        OpBGP featureFeature = featureFeatureRule(subject, predicate, object);
        OpBGP featureGeometry = featureGeometryRule(subject, predicate, object);
        OpBGP geometryFeature = geometryFeatureRule(subject, predicate, object);
        OpBGP geometryGeometry = geometryGeometryRule(subject, predicate, object);

        Op opLeft = OpUnion.create(featureFeature, featureGeometry);
        Op opRight = OpUnion.create(geometryFeature, geometryGeometry);

        //TODO Check if this is four unions or two nested unions? Does it make a difference?
        Op opFinal = OpUnion.create(opLeft, opRight);
        //SSE.write(opFinal);

        // Use the default, optimizing query engine.
        return QC.execute(opFinal, binding, execCxt);

    }

    private OpBGP featureFeatureRule(Node subject, Node predicate, Node object) {

        Var subjectGeometryVar = createNewVar();
        Var objectGeometryVar = createNewVar();
        Var subjectLiteralVar = createNewVar();
        Var objectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.FEATURE_NODE));
        bp.add(new Triple(object, RDF.type.asNode(), Geo.FEATURE_NODE));

        bp.add(new Triple(subject, Geo.HAS_DEFAULT_GEOMETRY_NODE, subjectGeometryVar));
        bp.add(new Triple(object, Geo.HAS_DEFAULT_GEOMETRY_NODE, objectGeometryVar));

        bp.add(new Triple(subjectGeometryVar, Geo.HAS_SERIALISATION_NODE, subjectLiteralVar));
        bp.add(new Triple(objectGeometryVar, Geo.HAS_SERIALISATION_NODE, objectLiteralVar));

        bp.add(new Triple(subjectLiteralVar, predicate, objectLiteralVar));

        return new OpBGP(bp);
    }

    private OpBGP featureGeometryRule(Node subject, Node predicate, Node object) {

        Var subjectGeometryVar = createNewVar();
        Var subjectLiteralVar = createNewVar();
        Var objectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.FEATURE_NODE));
        bp.add(new Triple(object, RDF.type.asNode(), Geo.GEOMETRY_NODE));

        bp.add(new Triple(subject, Geo.HAS_DEFAULT_GEOMETRY_NODE, subjectGeometryVar));

        bp.add(new Triple(subjectGeometryVar, Geo.HAS_SERIALISATION_NODE, subjectLiteralVar));
        bp.add(new Triple(object, Geo.HAS_SERIALISATION_NODE, objectLiteralVar));

        bp.add(new Triple(subjectLiteralVar, predicate, objectLiteralVar));

        return new OpBGP(bp);
    }

    private OpBGP geometryFeatureRule(Node subject, Node predicate, Node object) {

        Var objectGeometryVar = createNewVar();
        Var subjectLiteralVar = createNewVar();
        Var objectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.GEOMETRY_NODE));
        bp.add(new Triple(object, RDF.type.asNode(), Geo.FEATURE_NODE));

        bp.add(new Triple(object, Geo.HAS_DEFAULT_GEOMETRY_NODE, objectGeometryVar));

        bp.add(new Triple(subject, Geo.HAS_SERIALISATION_NODE, subjectLiteralVar));
        bp.add(new Triple(objectGeometryVar, Geo.HAS_SERIALISATION_NODE, objectLiteralVar));

        bp.add(new Triple(subjectLiteralVar, predicate, objectLiteralVar));

        return new OpBGP(bp);
    }

    private OpBGP geometryGeometryRule(Node subject, Node predicate, Node object) {

        Var subjectLiteralVar = createNewVar();
        Var objectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.GEOMETRY_NODE));
        bp.add(new Triple(object, RDF.type.asNode(), Geo.GEOMETRY_NODE));

        bp.add(new Triple(subject, Geo.HAS_SERIALISATION_NODE, subjectLiteralVar));
        bp.add(new Triple(object, Geo.HAS_SERIALISATION_NODE, objectLiteralVar));

        bp.add(new Triple(subjectLiteralVar, predicate, objectLiteralVar));

        return new OpBGP(bp);
    }

    private static int variableCount = 0;

    // Create a new, hidden, variable.
    private static Var createNewVar() {
        variableCount++;
        String varName = "Var-" + variableCount;
        return Var.alloc(varName);
    }

}
