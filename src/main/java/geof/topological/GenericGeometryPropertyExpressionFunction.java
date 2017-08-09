/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological;

import implementation.GeometryWrapper;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprFunction1;
import org.apache.jena.sparql.expr.NodeValue;
import org.opengis.geometry.MismatchedDimensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 
 */
public abstract class GenericGeometryPropertyExpressionFunction extends ExprFunction1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericGeometryPropertyExpressionFunction.class);

    public GenericGeometryPropertyExpressionFunction(Expr expr, String symbol) {
        super(expr, symbol);
    }

    @Override
    public NodeValue eval(NodeValue v) {
        try {
            GeometryWrapper geometry = GeometryWrapper.extract(v);

            return getValue(geometry);
        } catch (DatatypeFormatException | MismatchedDimensionException ex) {
            LOGGER.error("Expression Function Exception: {}", ex.getMessage());
            return Expr.NONE.getConstant();
        }

    }

    protected abstract NodeValue getValue(GeometryWrapper geometry);

}
