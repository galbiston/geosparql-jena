/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.simplefeatures.expressionfunction;

import datatype.GeometryWrapper;
import geof.topological.GenericExpressionFunction;
import org.apache.jena.sparql.expr.Expr;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import support.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class Crosses extends GenericExpressionFunction {

    public Crosses(Expr expr1, Expr expr2) {
        super(expr1, expr2, Vocabulary.SF_CROSSES_SYMBOL);
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new Crosses(arg1, arg2);
    }

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.crosses(targetGeometry);
    }
}
