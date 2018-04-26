/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopological.filter_functions;

import implementation.GeometryWrapper;
import java.lang.invoke.MethodHandles;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
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

            //Transfer the parameters as Nodes
            Node node2 = v2.asNode();
            double radius = Double.parseDouble(node2.getLiteralLexicalForm());

            //Obtain the target distance units
            String unitsURI = v3.asNode().getURI();
            GeometryWrapper buffer = geometry.buffer(radius, unitsURI);

            return buffer.asNode();
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Exception: {}, {}, {}, {}", v1, v2, v3, dfx.getMessage());
            return NodeValue.nvEmptyString;
        }

    }
}
