/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.pfunction.PropertyFunctionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In order to make this class work, the OpJoin and OpUnion need to be
 * re-implemented so that they can take more than 2 Ops as their parameters.
 *
 *
 * @author haozhechen
 * @deprecated
 */
public class georEquals extends PropertyFunctionBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(georEquals.class);

    public static int VARIABLE = 0;

    @Override
    public QueryIterator exec(Binding binding, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCxt) {

//        LOGGER.info("Start property function optimization");
//        argSubject = Substitute.substitute(argSubject, binding);
//        argObject = Substitute.substitute(argObject, binding);
//
//        Node nodeVar_SUB = argSubject.getArg();
//        Node nodeVar_OBJ = argObject.getArg();
//
//        // Build  SPARQL algebra expression
//        Var GMLVar_SUB = createNewVar();
//        Var GMLVar_OBJ = createNewVar();
//        Var GeomVar_SUB = createNewVar();
//        Var GeomVar_OBJ = createNewVar();
//
//        Triple NodeVarHasGML_SUB = new Triple(nodeVar_SUB, Vocabulary.GML_PRO.asNode(), GMLVar_SUB);
//        Triple NodeVarHasGML_OBJ = new Triple(nodeVar_OBJ, Vocabulary.GML_PRO.asNode(), GMLVar_OBJ);
//
//        Triple FeaHasGeom_SUB = new Triple(nodeVar_SUB, Vocabulary.GEOMEXACT_PRO.asNode(), GeomVar_SUB);
//        Triple FeaHasGeom_OBJ = new Triple(nodeVar_OBJ, Vocabulary.GEOMEXACT_PRO.asNode(), GeomVar_OBJ);
//        Triple GeomHasGML_SUB = new Triple(GeomVar_SUB, Vocabulary.GML_PRO.asNode(), GMLVar_SUB);
//        Triple GeomHasGML_OBJ = new Triple(GeomVar_OBJ, Vocabulary.GML_PRO.asNode(), GMLVar_OBJ);
//
//        //Spefify the Expr Function type here:
//        Expr expr = new EqualsExprFunc(new ExprVar(GMLVar_SUB.getName()), new ExprVar(GMLVar_OBJ.getName()));
        //List all the query rewrite posibilities:
        // 1. feature - feature
//        BasicPattern F2Fbp = new BasicPattern();
//        F2Fbp.add(FeaHasGeom_SUB);
//        F2Fbp.add(FeaHasGeom_OBJ);
//        F2Fbp.add(GeomHasGML_SUB);
//        F2Fbp.add(GeomHasGML_OBJ);
//        OpBGP F2Fop = new OpBGP(F2Fbp);
//        Op F2Ffilter = OpFilter.filter(expr, F2Fop);
//        // 2. feature - geometry
//        BasicPattern F2Gbp = new BasicPattern();
//        F2Gbp.add(FeaHasGeom_SUB);
//        F2Gbp.add(GeomHasGML_SUB);
//        F2Gbp.add(NodeVarHasGML_OBJ);
//        OpBGP F2Gop = new OpBGP(F2Gbp);
//        Op F2Gfilter = OpFilter.filter(expr, F2Gop);
//        // 3. geometry - feature
//        BasicPattern G2Fbp = new BasicPattern();
//        G2Fbp.add(NodeVarHasGML_SUB);
//        G2Fbp.add(FeaHasGeom_OBJ);
//        G2Fbp.add(GeomHasGML_OBJ);
//        OpBGP G2Fop = new OpBGP(G2Fbp);
//        Op G2Ffilter = OpFilter.filter(expr, G2Fop);
//        // 4. geometry - geometry
//        BasicPattern G2Gbp = new BasicPattern();
//        G2Gbp.add(NodeVarHasGML_SUB);
//        G2Gbp.add(NodeVarHasGML_OBJ);
//        OpBGP G2Gop = new OpBGP(G2Gbp);
//        Op G2Gfilter = OpFilter.filter(expr, G2Gop);
//
//        List<Op> opList = new ArrayList();
//        opList.add(F2Ffilter);
//        opList.add(F2Gfilter);
//        opList.add(G2Ffilter);
//        opList.add(G2Gfilter);
//
//        Op opJoin1 = OpJoin.create(F2Ffilter, null);
//        Op opJoin2 = OpJoin.create(F2Gfilter, null);
//        Op opJoin3 = OpJoin.create(G2Ffilter, null);
//        Op opJoin4 = OpJoin.create(G2Gfilter, null);
//        opJoin1 = Transformer.transform(new TransformSimplify(), opJoin1);
//
//        System.out.println("join is: " + opJoin1.toString());
//
//        Op join1 = OpJoin.create(opJoin1, opJoin2);
//        Op join2 = OpJoin.create(opJoin3, opJoin4);
////        Op join1 = OpJoin.create(F2Ffilter, F2Gfilter);
////        Op join2 = OpJoin.create(G2Ffilter, G2Gfilter);
//        Op op1 = OpUnion.create(join1, join2);
//        op1 = Transformer.transform(new TransformSimplify(), op1);
//        System.out.println("op is: " + op1.toString());
        //Use the default, optimizing query engine.
//        return QC.execute(op1, binding, execCxt);
        return null;
    }

    // Create a new, hidden, variable.
    private static Var createNewVar() {
        VARIABLE++;
        String varName = "Var-" + VARIABLE;
        return Var.alloc(varName);
    }

}
