/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.geometryproperty.property_functions;

import geo.topological.GenericGeometryPropertyFunction;
import geof.topological.geometry_property.expression_functions.DimensionEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 */
public class DimensionPF extends GenericGeometryPropertyFunction {

    @Override
    protected Expr propFunc(Expr expr) {
        return new DimensionEF(expr);
    }

}
