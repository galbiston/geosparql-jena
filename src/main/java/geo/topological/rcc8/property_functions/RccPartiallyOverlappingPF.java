/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.rcc8.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.rcc8.expression_functions.RccPartiallyOverlappingEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class RccPartiallyOverlappingPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new RccPartiallyOverlappingEF(expr1, expr2);
    }

}
