/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.pf.sf.gml.featuretofeature;

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
import geof.topological.simplefeatures.expressionfunction.sfIntersectsEF;
import implementation.vocabulary.General;
import implementation.vocabulary.Geo;

/**
 *
 * @author haozhechen
 */
public class SFIntersectsQRGmlFeatureToFeature extends PropertyFunctionBase {

    public static int VARIABLE = 0;

    @Override
    public QueryIterator exec(Binding binding, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCxt) {

        argSubject = Substitute.substitute(argSubject, binding);
        argObject = Substitute.substitute(argObject, binding);

        Node nodeVar_SUB = argSubject.getArg();
        Node nodeVar_OBJ = argObject.getArg();

        // Build  SPARQL algebra expression
        Var GMLVar_SUB = createNewVar();
        Var GMLVar_OBJ = createNewVar();
        Var GeomVar_SUB = createNewVar();
        Var GeomVar_OBJ = createNewVar();

        BasicPattern bp = new BasicPattern();

        Triple FeaHasGeom_SUB = new Triple(nodeVar_SUB, Geo.HAS_GEOMETRY_PRO.asNode(), GeomVar_SUB);
        Triple FeaHasGeom_OBJ = new Triple(nodeVar_OBJ, Geo.HAS_GEOMETRY_PRO.asNode(), GeomVar_OBJ);
        Triple GeomHasGML_SUB = new Triple(GeomVar_SUB, Geo.AS_GML_PRO.asNode(), GMLVar_SUB);
        Triple GeomHasGML_OBJ = new Triple(GeomVar_OBJ, Geo.AS_GML_PRO.asNode(), GMLVar_OBJ);

        //Spefify the Expr Function type here:
        Expr expr = new sfIntersectsEF(new ExprVar(GMLVar_SUB.getName()), new ExprVar(GMLVar_OBJ.getName()));

        bp.add(FeaHasGeom_SUB);
        bp.add(FeaHasGeom_OBJ);
        bp.add(GeomHasGML_SUB);
        bp.add(GeomHasGML_OBJ);

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
