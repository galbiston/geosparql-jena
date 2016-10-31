/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.simplefeatures.propertyfunction;

import geof.topological.GenericPropertyFunction;
import org.apache.jena.sparql.expr.Expr;
import geof.topological.simplefeatures.expressionfunction.SFWithinExprFunc;

/**
 *
 * @author haozhechen
 */
public class Within extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new SFWithinExprFunc(expr1, expr2);
    }

}
