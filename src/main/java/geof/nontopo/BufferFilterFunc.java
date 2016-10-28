/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import com.vividsolutions.jts.geom.Geometry;
import datatype.GeneralDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uom.UomConverter;

/**
 *
 * Return type ogc:geomLiteral ogc:geomLiteral can be a string value like
 * "<gml:Point ...>...</gml:Point>"
 *
 * @author haozhechen
 */
public class BufferFilterFunc extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(BufferFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        GeneralDatatype generalDatatype = new GeneralDatatype();

        //Transfer the parameters as Nodes
        Node node1 = v1.asNode();
        Node node2 = v2.asNode();
        //the units
        Node node3 = v3.asNode();
        String destiUnit = node3.getURI();
        LOGGER.info("Current unit: {}", node3.getLocalName());

        try {
            Geometry g1 = (Geometry) generalDatatype.parse(node1.getLiteralLexicalForm());

            double radius = Double.parseDouble(node2.getLiteralLexicalForm());
            radius = UomConverter.ConvertToUnit(destiUnit, radius);

            Geometry buffer = g1.buffer(radius);

            return NodeValue.makeNodeString(generalDatatype.unparse(buffer));
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);
            return NodeValue.nvEmptyString;
        }
    }
}
