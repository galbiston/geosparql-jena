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
 * @author haozhechen
 */
public abstract class GenericGeometryPropertyFunction extends PFuncSimple {

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {
        if (object.isLiteral()) {
            //These Property Functions do not accept literals as objects so exit quickly.

            return QueryIterNullIterator.create(execCxt);
        } else {
            /**
             * Apply all the query rewrite rules.
             */
            Op featureProperty = featurePropertyRule(subject);
            Op geometryProperty = geometryPropertyRule(subject);

            Op opUnion = OpUnion.create(featureProperty, geometryProperty);

            SSE.write(opUnion);
            // Use the default, optimizing query engine.
            return QC.execute(opUnion, binding, execCxt);
        }

    }

    protected abstract Expr expressionFunction(Expr expr);

    private Op featurePropertyRule(Node subject) {
        Var subjectGeometryVar = createNewVar();
        Var subjectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.FEATURE_NODE));

        bp.add(new Triple(subject, Geo.HAS_GEOMETRY_NODE, subjectGeometryVar));

        bp.add(new Triple(subjectGeometryVar, Geo.HAS_SERIALIZATION_NODE, subjectLiteralVar));

        OpBGP op = new OpBGP(bp);

        Expr expr = getExpressionFunction(subjectLiteralVar);

        return OpFilter.filter(expr, op);
    }

    private Op geometryPropertyRule(Node subject) {
        Var subjectLiteralVar = createNewVar();

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.GEOMETRY_NODE));

        bp.add(new Triple(subject, Geo.HAS_SERIALIZATION_NODE, subjectLiteralVar));

        OpBGP op = new OpBGP(bp);

        Expr expr = getExpressionFunction(subjectLiteralVar);

        return OpFilter.filter(expr, op);
    }

    private static int variableCount = 0;

    // Create a new, hidden, variable.
    private static Var createNewVar() {
        variableCount++;
        String varName = "Var-" + variableCount;
        return Var.alloc(varName);
    }

    private Expr getExpressionFunction(Var subjectLiteralVar) {

        ExprVar subjectExprVar = new ExprVar(subjectLiteralVar);

        return expressionFunction(subjectExprVar);
    }
}
