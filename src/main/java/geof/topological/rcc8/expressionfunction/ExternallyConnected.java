/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.rcc8.expressionfunction;

import implementation.GeometryWrapper;
import geof.topological.GenericExpressionFunction;
import org.apache.jena.sparql.expr.Expr;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import implementation.support.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class ExternallyConnected extends GenericExpressionFunction {

    public ExternallyConnected(Expr expr1, Expr expr2) {
        super(expr1, expr2, Vocabulary.RCC_EXTCONNECTED_SYMBOL);
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new ExternallyConnected(arg1, arg2);
    }

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.relate(targetGeometry, "FFTFTTTTT");
    }

}
