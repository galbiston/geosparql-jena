/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.pf.eh.wkt.geometrytofeature;

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
import geof.topological.egenhofer.expressionfunction.ehMeetEF;
import implementation.vocabulary.General;
import implementation.vocabulary.Geo;

/**
 *
 * @author haozhechen
 */
public class EHMeetQRWktGeometryToFeature extends PropertyFunctionBase {

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
        Var GeomVar_OBJ = createNewVar();

        BasicPattern bp = new BasicPattern();

        Triple NodeVarHasWKT_SUB = new Triple(nodeVar_SUB, Geo.AS_WKT_PRO.asNode(), WKTVar_SUB);

        Triple FeaHasGeom_OBJ = new Triple(nodeVar_OBJ, Geo.HAS_GEOMETRY_PRO.asNode(), GeomVar_OBJ);
        Triple GeomHasWKT_OBJ = new Triple(GeomVar_OBJ, Geo.AS_WKT_PRO.asNode(), WKTVar_OBJ);

        //Spefify the Expr Function type here:
        Expr expr = new ehMeetEF(new ExprVar(WKTVar_SUB.getName()), new ExprVar(WKTVar_OBJ.getName()));

        bp.add(NodeVarHasWKT_SUB);
        bp.add(FeaHasGeom_OBJ);
        bp.add(GeomHasWKT_OBJ);

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
