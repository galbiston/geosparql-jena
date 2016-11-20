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
public class IsSimpleEF extends GenericBooleanExpressionFunction {

    public IsSimpleEF(Expr expr) {
        super(expr, Geo.ISSIMPLE);
    }

    @Override
    protected boolean booleanProperty(GeometryWrapper geometry) {
        return geometry.isSimple();
    }

    @Override
    public Expr copy(Expr expr) {
        return new IsSimpleEF(expr);
    }

}
