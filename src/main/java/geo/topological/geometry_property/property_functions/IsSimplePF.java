/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.geometry_property.property_functions;

import geo.topological.GenericGeometryPropertyFunction;
import geof.topological.geometry_property.expression_functions.IsSimpleEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * 
 */
public class IsSimplePF extends GenericGeometryPropertyFunction {

    @Override
    protected Expr propFunc(Expr expr) {
        return new IsSimpleEF(expr);
    }

}
