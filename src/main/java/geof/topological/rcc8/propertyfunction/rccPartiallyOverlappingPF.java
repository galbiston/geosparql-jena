/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.rcc8.propertyfunction;

import geof.topological.GenericPropertyFunction;
import geof.topological.rcc8.expressionfunction.rccPartiallyOverlappingEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class rccPartiallyOverlappingPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new rccPartiallyOverlappingEF(expr1, expr2);
    }

}
