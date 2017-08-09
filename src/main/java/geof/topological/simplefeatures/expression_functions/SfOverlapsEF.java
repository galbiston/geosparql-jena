/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.simplefeatures.expression_functions;

import geof.topological.GenericExpressionFunction;
import implementation.GeometryWrapper;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.expr.Expr;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * 
 * 
 */
public class SfOverlapsEF extends GenericExpressionFunction {

    public SfOverlapsEF(Expr expr1, Expr expr2) {
        super(expr1, expr2, Geof.SF_OVERLAPS);
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new SfOverlapsEF(arg1, arg2);
    }

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.overlaps(targetGeometry);
    }
}
