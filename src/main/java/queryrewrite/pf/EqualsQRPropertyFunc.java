/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.pf;

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
import queryrewrite.expr.EqualsExprFunc;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class EqualsQRPropertyFunc extends PropertyFunctionBase {

    public static int VARIABLE = 0;

    @Override
    public QueryIterator exec(Binding binding, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCxt) {

        argSubject = Substitute.substitute(argSubject, binding);
        argObject = Substitute.substitute(argObject, binding);

        Node nodeVar_SUB = argSubject.getArg();
        Node nodeVar_OBJ = argObject.getArg();

        //If the argSubject and argObject are GML Literal, need to transfer it to Expr
        Expr exprVar_SUB = argSubject.asExprList().get(0);
        Expr exprVar_OBJ = argObject.asExprList().get(0);

        // Build  SPARQL algebra expression
        Var GMLVar_SUB = createNewVar();
        Var GMLVar_OBJ = createNewVar();
        Var GeomVar_SUB = createNewVar();
        Var GeomVar_OBJ = createNewVar();

        BasicPattern bp = new BasicPattern();

        Triple NodeVarHasGML_SUB = new Triple(nodeVar_SUB, Vocabulary.GML_PRO.asNode(), GMLVar_SUB);
        Triple NodeVarHasGML_OBJ = new Triple(nodeVar_OBJ, Vocabulary.GML_PRO.asNode(), GMLVar_OBJ);

        Triple FeaHasGeom_SUB = new Triple(nodeVar_SUB, Vocabulary.GEOMPOINT_PRO.asNode(), GeomVar_SUB);
        Triple FeaHasGeom_OBJ = new Triple(nodeVar_OBJ, Vocabulary.GEOMPOINT_PRO.asNode(), GeomVar_OBJ);
        Triple GeomHasGML_SUB = new Triple(GeomVar_SUB, Vocabulary.GML_PRO.asNode(), GMLVar_SUB);
        Triple GeomHasGML_OBJ = new Triple(GeomVar_OBJ, Vocabulary.GML_PRO.asNode(), GMLVar_OBJ);

        //Spefify the Expr Function type here:
        Expr expr = new EqualsExprFunc(new ExprVar(GMLVar_SUB.getName()), new ExprVar(GMLVar_OBJ.getName()));

        if (nodeVar_SUB.isLiteral()) {
            if (nodeVar_OBJ.isLiteral()) {
                //Subject is GML literal, Object is GML Literal

                expr = new EqualsExprFunc(exprVar_SUB, exprVar_OBJ);
            } else if (nodeVar_OBJ.isURI()) {
                //Subject is GML literal, Object is Feature
                bp.add(FeaHasGeom_OBJ);
                bp.add(GeomHasGML_OBJ);

                expr = new EqualsExprFunc(exprVar_SUB, new ExprVar(GMLVar_OBJ.getName()));
            } else {
                //Subject is GML literal, Object is Geometry variable
                bp.add(NodeVarHasGML_OBJ);

                expr = new EqualsExprFunc(exprVar_SUB, new ExprVar(GMLVar_OBJ.getName()));
            }

        } else if (nodeVar_SUB.isURI()) {
            if (nodeVar_OBJ.isLiteral()) {
                //Subject is Feature, Object is GML Literal
                bp.add(FeaHasGeom_SUB);
                bp.add(GeomHasGML_SUB);

                expr = new EqualsExprFunc(new ExprVar(GMLVar_SUB.getName()), exprVar_OBJ);
            } else if (nodeVar_OBJ.isURI()) {
                //Subject is Feature, Object is Feature
                bp.add(FeaHasGeom_SUB);
                bp.add(FeaHasGeom_OBJ);
                bp.add(GeomHasGML_SUB);
                bp.add(GeomHasGML_OBJ);

            } else {
                //Subject is Feature, Object is Geometry variable
                bp.add(FeaHasGeom_SUB);
                bp.add(GeomHasGML_SUB);
                bp.add(NodeVarHasGML_OBJ);

            }

        } else if (!nodeVar_OBJ.isLiteral() && !nodeVar_SUB.isURI()) {
            if (nodeVar_OBJ.isLiteral()) {
                //Subject is Geometry variable, Object is GML Literal
                bp.add(NodeVarHasGML_SUB);

                expr = new EqualsExprFunc(new ExprVar(GMLVar_SUB.getName()), exprVar_OBJ);
            } else if (nodeVar_OBJ.isURI()) {
                //Subject is Geometry variable, Object is Feature
                bp.add(NodeVarHasGML_SUB);
                bp.add(FeaHasGeom_OBJ);
                bp.add(GeomHasGML_OBJ);

            } else {
                //Subject is Geometry variable, Object is Geometry variable
                bp.add(NodeVarHasGML_SUB);
                bp.add(NodeVarHasGML_OBJ);

            }
        }

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
