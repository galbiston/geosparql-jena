/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.egenhofer.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.egenhofer.expression_functions.EhMeetEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * 
 * 
 */
public class EhMeetPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new EhMeetEF(expr1, expr2);
    }

}
