/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.geometry_property.expression_functions;

import geof.topological.GenericGeometryPropertyExpressionFunction;
import implementation.GeometryWrapper;
import implementation.vocabulary.Geo;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.NodeValue;

/**
 *
 * @author haozhechen
 */
public class DimensionEF extends GenericGeometryPropertyExpressionFunction {

    public DimensionEF(Expr expr) {
        super(expr, Geo.DIMENSION);
    }

    @Override
    public Expr copy(Expr expr) {
        return new DimensionEF(expr);
    }

    @Override
    protected NodeValue getValue(GeometryWrapper geometry) {
        return NodeValue.makeInteger(geometry.getTopologicalDimension());
    }

}
