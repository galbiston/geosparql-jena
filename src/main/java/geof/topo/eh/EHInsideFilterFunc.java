/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topo.eh;

import com.vividsolutions.jts.geom.Geometry;
import datatype.GeneralDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class EHInsideFilterFunc extends FunctionBase2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(EHInsideFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        GeneralDatatype generalDatatype = new GeneralDatatype();

        Node node1 = v1.asNode();
        Node node2 = v2.asNode();

        try {
            Geometry g1 = (Geometry) generalDatatype.parse(node1.getLiteralLexicalForm());
            Geometry g2 = (Geometry) generalDatatype.parse(node2.getLiteralLexicalForm());

            // Use DE-9IM to model the relationship
            // Use JTS's relate function to implement
            boolean result = g1.relate(g2, "TFF*FFT**");

            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);
            return NodeValue.FALSE;
        }
    }

}
