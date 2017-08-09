/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.simplefeatures.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.simplefeatures.expression_functions.SfOverlapsEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * 
 * 
 */
public class SfOverlapsPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new SfOverlapsEF(expr1, expr2);
    }

}
