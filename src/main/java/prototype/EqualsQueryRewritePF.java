/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.main.QC;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.pfunction.PropertyFunction;
import org.apache.jena.sparql.util.NodeUtils;
import geof.topological.simplefeatures.expressionfunction.SFEqualsExprFunc;

/**
 *
 * @author haozhechen
 * @deprecated
 */
public class EqualsQueryRewritePF implements PropertyFunction {

    public static final String uri = "http://www.opengis.net/ont/gml#";

    @Override
    public void build(PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCxt) {

        if (argSubject.isList() || argObject.isList()) {
            throw new QueryBuildException("List arguments to " + predicate.getURI());
        }
    }

    /* This be called once, with unevaluated arguments.
     * To do a rewrite of part of a query, we must use the fundamental PropertyFunction
     * interface to be called once with the input iterator.
     * Must not return null nor throw an exception.  Instead, return a QueryIterNullIterator
     * indicating no matches.
     */
    @Override
    public QueryIterator exec(QueryIterator input, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCxt) {
        // No real need to check the pattern arguments because
        // the replacement triple pattern and regex will cope
        // but we illustrate testing here.

        //Get and Print the subject
        Node nodeVar1 = argSubject.getArg();
        System.out.println("subject: " + nodeVar1.toString());
        //Get and Print the object
        Node nodeVar2 = argObject.getArg();
        System.out.println("object: " + nodeVar2.toString());

        String pattern = NodeUtils.stringLiteral(argObject.getArg());
//        System.out.println("object: " + pattern);
//        if (pattern == null) {
//            Log.warn(this, "Pattern must be a plain literal or xsd:string: " + argObject.getArg());
//            return QueryIterNullIterator.create(execCxt);
//        }

        // Better
        // Build a SPARQL algebra expression
        Var var1 = createNewVar();
        Var var2 = createNewVar();// Hidden variable
        System.out.println("var2: " + var2.getName());

        BasicPattern bp = new BasicPattern();
        Property gmlProperty = ResourceFactory.createProperty(uri + "asGML");
        Property geometry = ResourceFactory.createProperty("http://ntu.ac.uk/ont/geo#" + "hasExactGeometry");

        Triple t1 = new Triple(nodeVar1, gmlProperty.asNode(), var1);
        Triple t2 = new Triple(nodeVar2, gmlProperty.asNode(), var2);
        bp.add(t1);
        bp.add(t2);
        OpBGP op = new OpBGP(bp);
        System.out.println("op: " + op.toString());

        Expr expr = new SFEqualsExprFunc(new ExprVar(var1.getName()), new ExprVar(var2.getName()));
        //Expr regex = new E_Regex(new ExprVar(var2.getName()), pattern, "i");
        Op filter = OpFilter.filter(expr, op);
        System.out.println("filter: " + filter.toString());
//        System.out.println("Input Query Iterator: " + input.toString());
//        System.out.println("Execution Context: " + execCxt.toString());

        // Use the default, optimizing query engine.
        return QC.execute(filter, input, execCxt);
    }

    static int hiddenVariableCount = 0;

    // Create a new, hidden, variable.
    private static Var createNewVar() {
        hiddenVariableCount++;
        String varName = "-search-" + hiddenVariableCount;
        return Var.alloc(varName);
    }

}
