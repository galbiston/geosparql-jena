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
import org.apache.jena.sparql.algebra.op.OpAssign;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.algebra.op.OpUnion;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.main.QC;
import org.apache.jena.sparql.expr.E_Equals;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.sparql.sse.SSE;

/**
 * @author Gregory Albiston
 * @author haozhechen
 */
public abstract class GenericGeometryPropertyFunction extends PFuncSimple {

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, Node object, ExecutionContext execCxt) {

        Op opFinal;

        //TODO only checking that the subject is bound when the subject or both could be bound/unbound.
        if (object.isVariable()) {
            opFinal = unboundPattern(subject, predicate, object);
        } else {
            opFinal = boundPattern(subject, predicate, object);
        }

        SSE.write(opFinal);
        // Use the default, optimizing query engine.
        return QC.execute(opFinal, binding, execCxt);
    }

    public Op boundPattern(Node subject, Node predicate, Node object) {

        Op assertedStatement = assertedStatement(subject, predicate, object);

        BasicPattern bp = new BasicPattern();
        Var litVar = createNewVar();
        bp.add(new Triple(subject, Geo.HAS_SERIALIZATION_NODE, litVar));

        OpBGP opBound = new OpBGP(bp);

        Expr expr = propFunc(new ExprVar(litVar));

        Var resultVar = createNewVar();

        Op opPart = OpAssign.assign(opBound, resultVar, expr);

        Expr equalsExpr = new E_Equals(new ExprVar(resultVar), new ExprVar(Var.alloc(object)));
        Op opPattern = OpFilter.filter(equalsExpr, opPart);

        Op opFinal = OpUnion.create(assertedStatement, opPattern);

        return opFinal;
    }

    public Op unboundPattern(Node subject, Node predicate, Node object) {

        Op assertedStatement = assertedStatement(subject, predicate, object);

        BasicPattern bp = new BasicPattern();
        Var litVar = createNewVar();
        bp.add(new Triple(subject, Geo.HAS_SERIALIZATION_NODE, litVar));

        OpBGP opUnbound = new OpBGP(bp);

        Expr expr = propFunc(new ExprVar(litVar));

        Var objectVar = Var.alloc(object);

        Op opPattern = OpAssign.assign(opUnbound, objectVar, expr);

        Op opFinal = OpUnion.create(assertedStatement, opPattern);

        return opFinal;
    }

    protected abstract Expr propFunc(Expr expr);

    //  protected abstract Node getPropertyNode();
    private OpBGP assertedStatement(Node subject, Node predicate, Node object) {

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, predicate, object));

        return new OpBGP(bp);
    }
    /*
    private Op getProperty(Node subject, Node object) {

        Var subjectLiteralVar = createNewVar();
        Var objectLiteralVar = Var.alloc(object);

        BasicPattern bp = new BasicPattern();
        bp.add(new Triple(subject, RDF.type.asNode(), Geo.GEOMETRY_NODE));

        bp.add(new Triple(subject, Geo.HAS_SERIALIZATION_NODE, subjectLiteralVar));
        bp.add(new Triple(subjectLiteralVar, getPropertyNode(), objectLiteralVar));

        return new OpBGP(bp);
    }
     */
    private static int variableCount = 0;

    // Create a new, hidden, variable.
    private static Var createNewVar() {
        variableCount++;
        String varName = "Var-" + variableCount;
        return Var.alloc(varName);
    }

}
