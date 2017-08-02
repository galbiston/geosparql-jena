/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.geometryproperty.EF;

import geof.topological.GenericGeometryPropertyExpressionFunction;
import implementation.GeometryWrapper;
import implementation.vocabulary.Geo;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.NodeValue;

/**
 *
 * @author haozhechen
 */
public class CoordinateDimensionEF extends GenericGeometryPropertyExpressionFunction {

    public CoordinateDimensionEF(Expr expr) {
        super(expr, Geo.COORDINATE_DIMENSION);
    }

    @Override
    protected NodeValue getValue(GeometryWrapper geometry) {
        return NodeValue.makeInteger(geometry.getCoordinateDimension());
    }

    @Override
    public Expr copy(Expr expr) {
        return new CoordinateDimensionEF(expr);
    }

}
