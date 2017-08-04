/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.egenhofer.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.egenhofer.expression_functions.EhEqualsEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class EhEqualsPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new EhEqualsEF(expr1, expr2);
    }

}
