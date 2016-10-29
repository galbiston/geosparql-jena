/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import datatype.CRSGeometry;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import datatype.UomConverter;

/**
 *
 * Return type ogc:geomLiteral ogc:geomLiteral can be a string value like
 * "<gml:Point ...>...</gml:Point>"
 *
 * @author haozhechen
 */
public class BufferFilterFunc extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(BufferFilterFunc.class);

    //TODO - This seems to be assuming WGS84 rather than handling different coordinate systems and scales.
    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        //Transfer the parameters as Nodes
        Node node2 = v2.asNode();
        //the units
        Node node3 = v3.asNode();
        String destiUnit = node3.getURI();
        LOGGER.info("Current unit: {}", node3.getLocalName());

        NodeValue resultNodeValue;

        try {
            CRSGeometry geometry = CRSGeometry.extract(v1);

            double radius = Double.parseDouble(node2.getLiteralLexicalForm());
            radius = UomConverter.ConvertToUnit(destiUnit, radius);

            CRSGeometry buffer = geometry.buffer(radius);

            resultNodeValue = buffer.getResultNode();
        } catch (DatatypeFormatException dfx) {
            resultNodeValue = NodeValue.nvEmptyString;
        }

        return resultNodeValue;
    }
}
