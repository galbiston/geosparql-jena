/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopological.FF;

import implementation.GeometryWrapper;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class DistanceFF extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceFF.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
            GeometryWrapper geometry2 = GeometryWrapper.extract(v2);

            double distance = geometry1.distance(geometry2, v3.asNode().getURI());

            return NodeValue.makeDouble(distance);
        } catch (DatatypeFormatException | FactoryException | MismatchedDimensionException | TransformException dfx) {
            return NodeValue.nvZERO;
        }

    }

}
