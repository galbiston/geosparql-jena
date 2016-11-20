/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.geometryproperty;

import geof.topological.GenericBooleanExpressionFunction;
import implementation.GeometryWrapper;
import implementation.vocabulary.Geo;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 */
public class IsEmptyEF extends GenericBooleanExpressionFunction {

    public IsEmptyEF(Expr expr) {
        super(expr, Geo.ISEMPTY);
    }

    @Override
    protected boolean booleanProperty(GeometryWrapper geometry) {
        return geometry.isEmpty();
    }

    @Override
    public Expr copy(Expr expr) {
        return new IsEmptyEF(expr);
    }

}
