/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopological.filter_functions;

import implementation.GeometryWrapper;
import implementation.index.GeometryLiteralIndex.GeometryIndex;
import java.lang.invoke.MethodHandles;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
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
public class DistanceFF extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1, GeometryIndex.PRIMARY);
            if (geometry1 == null) {
                return NodeValue.nvNaN;
            }
            GeometryWrapper geometry2 = GeometryWrapper.extract(v2, GeometryIndex.SECONDARY);
            if (geometry2 == null) {
                return NodeValue.nvNaN;
            }

            if (!v3.isIRI()) {
                return NodeValue.nvNaN;
            }

            double distance = geometry1.distance(geometry2, v3.asNode().getURI());

            return NodeValue.makeDouble(distance);
        } catch (FactoryException | MismatchedDimensionException | TransformException dfx) {
            LOGGER.error("Exception: {}, {}, {}, {}", v1, v2, v3, dfx.getMessage());
            return NodeValue.nvNaN;
        }

    }

}
