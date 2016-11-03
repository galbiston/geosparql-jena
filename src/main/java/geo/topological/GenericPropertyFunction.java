/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological;

import implementation.vocabulary.Geo;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.algebra.op.OpUnion;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.iterator.QueryIterNullIterator;
import org.apache.jena.sparql.engine.main.QC;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.sparql.sse.SSE;
import org.apache.jena.vocabulary.RDF;

/**
 *
 * @author Gregory Albiston
 */
public abstract class GenericPropertyFunction extends PFuncSimple {

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        if (object.isLiteral()) {
            //These Property Functions do not accept literals as objects so exit quickly.
            return QueryIterNullIterator.create(execCxt);
        } else {

            Op assertedStatement = assertedStatementRule(subject, predicate, object);
            Op featureFeature = featureFeatureRule(subject, object);
            Op featureGeometry = featureGeometryRule(subject, object);
            Op geometryFeature = geometryFeatureRule(subject, object);
            Op geometryGeometry = geometryGeometryRule(subject, object);

            Op opStart = OpUnion.create(featureFeature, featureGeometry);
            Op opMiddle = OpUnion.create(opStart, geometryFeature);
            Op opEnd = OpUnion.create(geometryGeometry, assertedStatement);

            Op opFinal = OpUnion.create(opMiddle, opEnd);
            //opFinal = OpUnion.create(featureFeature, featureGeometry);
            //opFinal = featureFeature;

            //BasicPattern bp = new BasicPattern();
            //bp.add(new Triple(subject, RDF.type.asNode(), Geo.FEATURE_NODE));
            //opFinal = new OpBGP(bp);
            SSE.write(opFinal);

            return QC.execute(opFinal, binding, execCxt);
        }
    }

    protected abstract Expr expressionFunction(Expr expr1, Expr expr2);

    private OpBGP assertedStatementRule(Node subject, Node predicate, Node object) {

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, predicate, object));

        return new OpBGP(bp);
    }

    private Op featureFeatureRule(Node subject, Node object) {

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

        OpBGP op = new OpBGP(bp);

        Expr expr = getExpressionFunction(subjectLiteralVar, objectLiteralVar);

        return OpFilter.filter(expr, op);
    }

    private Op featureGeometryRule(Node subject, Node object) {

        Var subjectGeometryVar = createNewVar();
        Var subjectLiteralVar = createNewVar();
        Var objectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.FEATURE_NODE));
        bp.add(new Triple(object, RDF.type.asNode(), Geo.GEOMETRY_NODE));

        bp.add(new Triple(subject, Geo.HAS_DEFAULT_GEOMETRY_NODE, subjectGeometryVar));

        bp.add(new Triple(subjectGeometryVar, Geo.HAS_SERIALISATION_NODE, subjectLiteralVar));
        bp.add(new Triple(object, Geo.HAS_SERIALISATION_NODE, objectLiteralVar));

        OpBGP op = new OpBGP(bp);

        Expr expr = getExpressionFunction(subjectLiteralVar, objectLiteralVar);

        return OpFilter.filter(expr, op);
    }

    private Op geometryFeatureRule(Node subject, Node object) {

        Var objectGeometryVar = createNewVar();
        Var subjectLiteralVar = createNewVar();
        Var objectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.GEOMETRY_NODE));
        bp.add(new Triple(object, RDF.type.asNode(), Geo.FEATURE_NODE));

        bp.add(new Triple(object, Geo.HAS_DEFAULT_GEOMETRY_NODE, objectGeometryVar));

        bp.add(new Triple(subject, Geo.HAS_SERIALISATION_NODE, subjectLiteralVar));
        bp.add(new Triple(objectGeometryVar, Geo.HAS_SERIALISATION_NODE, objectLiteralVar));

        OpBGP op = new OpBGP(bp);

        Expr expr = getExpressionFunction(subjectLiteralVar, objectLiteralVar);

        return OpFilter.filter(expr, op);
    }

    private Op geometryGeometryRule(Node subject, Node object) {

        Var subjectLiteralVar = createNewVar();
        Var objectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.GEOMETRY_NODE));
        bp.add(new Triple(object, RDF.type.asNode(), Geo.GEOMETRY_NODE));

        bp.add(new Triple(subject, Geo.HAS_SERIALISATION_NODE, subjectLiteralVar));
        bp.add(new Triple(object, Geo.HAS_SERIALISATION_NODE, objectLiteralVar));

        OpBGP op = new OpBGP(bp);

        Expr expr = getExpressionFunction(subjectLiteralVar, objectLiteralVar);

        return OpFilter.filter(expr, op);
    }

    private static int variableCount = 0;

    // Create a new, hidden, variable.
    private static Var createNewVar() {
        variableCount++;
        String varName = "Var-" + variableCount;
        return Var.alloc(varName);
    }

    private Expr getExpressionFunction(Var subjectLiteralVar, Var objectLiteralVar) {

        ExprVar subjectExprVar = new ExprVar(subjectLiteralVar);
        ExprVar objectExprVar = new ExprVar(objectLiteralVar);

        return expressionFunction(subjectExprVar, objectExprVar);
    }

}
