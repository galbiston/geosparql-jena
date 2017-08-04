/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.egenhofer.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.egenhofer.expression_functions.EhCoveredByEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class EhCoveredByPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new EhCoveredByEF(expr1, expr2);
    }

}
