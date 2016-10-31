/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.rcc8.propertyfunction;

import geof.topological.GenericPropertyFunction;
import org.apache.jena.sparql.expr.Expr;
import queryrewrite.expr.rcc8.RCC8NTPPExprFunc;

/**
 *
 * @author haozhechen
 */
public class NonTangentalProperPart extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new RCC8NTPPExprFunc(expr1, expr2);
    }

}
