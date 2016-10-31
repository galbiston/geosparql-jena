/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.pf.eh.wkt.featuretogeometry;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Substitute;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.main.QC;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.pfunction.PropertyFunctionBase;
import geof.topological.egenhofer.expressionfunction.Disjoint;
import support.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class EHDisjointQRWktFeatureToGeometry extends PropertyFunctionBase {

    public static int VARIABLE = 0;

    @Override
    public QueryIterator exec(Binding binding, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCxt) {

        argSubject = Substitute.substitute(argSubject, binding);
        argObject = Substitute.substitute(argObject, binding);

        Node nodeVar_SUB = argSubject.getArg();
        Node nodeVar_OBJ = argObject.getArg();

        // Build  SPARQL algebra expression
        Var WKTVar_SUB = createNewVar();
        Var WKTVar_OBJ = createNewVar();
        Var GeomVar_SUB = createNewVar();

        BasicPattern bp = new BasicPattern();

        Triple NodeVarHasWKT_OBJ = new Triple(nodeVar_OBJ, Vocabulary.WKT_PRO.asNode(), WKTVar_OBJ);

        Triple FeaHasGeom_SUB = new Triple(nodeVar_SUB, Vocabulary.GEOM_PRO.asNode(), GeomVar_SUB);
        Triple GeomHasWKT_SUB = new Triple(GeomVar_SUB, Vocabulary.WKT_PRO.asNode(), WKTVar_SUB);

        //Spefify the Expr Function type here:
        Expr expr = new Disjoint(new ExprVar(WKTVar_SUB.getName()), new ExprVar(WKTVar_OBJ.getName()));

        bp.add(FeaHasGeom_SUB);
        bp.add(GeomHasWKT_SUB);
        bp.add(NodeVarHasWKT_OBJ);

        OpBGP op = new OpBGP(bp);
        Op filter = OpFilter.filter(expr, op);

        // Use the default, optimizing query engine.
        return QC.execute(filter, binding, execCxt);
    }

    // Create a new, hidden, variable.
    private static Var createNewVar() {
        VARIABLE++;
        String varName = "Var-" + VARIABLE;
        return Var.alloc(varName);
    }

}
