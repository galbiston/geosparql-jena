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
 * @author haozhechen
 */
public abstract class GenericBooleanExpressionFunction extends ExprFunction1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericBooleanExpressionFunction.class);

    public GenericBooleanExpressionFunction(Expr expr, String symbol) {
        super(expr, symbol);
    }

    @Override
    public NodeValue eval(NodeValue v) {
        try {
            GeometryWrapper geometry = GeometryWrapper.extract(v);

            boolean result = booleanProperty(geometry);

            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException | MismatchedDimensionException ex) {
            LOGGER.error("Expression Function Exception: {}", ex.getMessage());
            return NodeValue.nvNothing;
        }

    }

    protected abstract boolean booleanProperty(GeometryWrapper geometry);

}
