/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.geometryproperty;

import geof.topological.GenericGeometryPropertyExpressionFunction;
import implementation.GeometryWrapper;
import implementation.vocabulary.Geo;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.NodeValue;

/**
 *
 * @author haozhechen
 */
public class SpatialDimensionEF extends GenericGeometryPropertyExpressionFunction {

    public SpatialDimensionEF(Expr expr) {
        super(expr, Geo.SPATIAL_DIMENSION);
    }

    @Override
    protected NodeValue getValue(GeometryWrapper geometry) {
        return NodeValue.makeInteger(geometry.getSpatialDimension());
    }

    @Override
    public Expr copy(Expr expr) {
        return new SpatialDimensionEF(expr);
    }

}
