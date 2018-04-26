/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopological.filter_functions;

import implementation.GeometryWrapper;
import java.lang.invoke.MethodHandles;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 */
public class DifferenceFF extends FunctionBase2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
            GeometryWrapper geometry2 = GeometryWrapper.extract(v2);

            GeometryWrapper difference = geometry1.difference(geometry2);
            return difference.asNode();

        } catch (DatatypeFormatException | FactoryException | MismatchedDimensionException | TransformException dfx) {
            LOGGER.error("Exception: {}, {}, {}", v1, v2, dfx.getMessage());
            return NodeValue.nvEmptyString;
        }

    }

}
