/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topo;

import com.vividsolutions.jts.geom.Geometry;
import datatype.GmlDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class OverlapsFilterFunc extends FunctionBase2 {

    private static Logger LOGGER = LoggerFactory.getLogger(OverlapsFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;

        Node node1 = v1.asNode();
        Node node2 = v2.asNode();

        boolean result = false;

        try {
            Geometry g1 = (Geometry) gmlDataType.parse(node1.getLiteralLexicalForm());
            Geometry g2 = (Geometry) gmlDataType.parse(node2.getLiteralLexicalForm());

            result = g1.contains(g2);
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry");
        }

        return NodeValue.makeBoolean(result);

    }

}
