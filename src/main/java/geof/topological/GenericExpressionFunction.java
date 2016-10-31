/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological;

import implementation.GeometryWrapper;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprFunction2;
import org.apache.jena.sparql.expr.NodeValue;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Greg
 */
public abstract class GenericExpressionFunction extends ExprFunction2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExpressionFunction.class);

    public GenericExpressionFunction(Expr expr1, Expr expr2, String symbol) {
        super(expr1, expr2, symbol);
    }

    @Override
    public NodeValue eval(NodeValue v1, NodeValue v2) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
            GeometryWrapper geometry2 = GeometryWrapper.extract(v2);

            boolean result = relate(geometry1, geometry2);

            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException | FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Expression Function Exception: {}", ex.getMessage());
            return NodeValue.FALSE;
        }
    }

    protected abstract boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException;

}
