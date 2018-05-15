/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopological.filter_functions;

import implementation.GeometryWrapper;
import java.lang.invoke.MethodHandles;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Return type ogc:geomLiteral ogc:geomLiteral can be a string value like
 * "<gml:Point ...>...</gml:Point>"
 *
 *
 *
 */
public class BufferFF extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        try {
            GeometryWrapper geometry = GeometryWrapper.extract(v1);
            if (geometry == null) {
                return NodeValue.nvEmptyString;
            }

            //Transfer the parameters as Nodes
            if (!v2.isDouble()) {
                return NodeValue.nvEmptyString;
            }
            Node node2 = v2.asNode();
            double radius = Double.parseDouble(node2.getLiteralLexicalForm());

            //Obtain the target distance units
            if (!v3.isIRI()) {
                return NodeValue.nvEmptyString;
            }
            String unitsURI = v3.getNode().getURI();
            GeometryWrapper buffer = geometry.buffer(radius, unitsURI);

            return buffer.asNode();
        } catch (FactoryException | MismatchedDimensionException | TransformException dfx) {
            LOGGER.error("Exception: {}, {}, {}, {}", v1, v2, v3, dfx.getMessage());
            return NodeValue.nvEmptyString;
        }

    }
}
