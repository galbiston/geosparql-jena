/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.geometryproperty;

import geof.topological.GenericIntegerExpressionFunction;
import implementation.GeometryWrapper;
import implementation.vocabulary.Geo;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 */
public class DimensionEF extends GenericIntegerExpressionFunction {

    public DimensionEF(Expr expr) {
        super(expr, Geo.DIMENSION);
    }

    @Override
    protected int integerProperty(GeometryWrapper geometry) {
        return geometry.getTopologicalDimension();
    }

    @Override
    public Expr copy(Expr expr) {
        return new DimensionEF(expr);
    }

}
