/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.egenhofer.property_functions;

import geo.topological.GenericPropertyFunction;
import geo.topological.GenericExpressionFunction;
import geof.topological.egenhofer.filter_functions.EhOverlapFF;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 *
 *
 */
public class EhOverlapPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new GenericExpressionFunction(expr1, expr2, Geof.EH_OVERLAP, new EhOverlapFF());
    }
}
