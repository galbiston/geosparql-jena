/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topo.relate;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import datatype.WktDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class RelateFilterFunc extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelateFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {
        RDFDatatype wktDataType = WktDatatype.theWktDatatype;

        Node node1 = v1.asNode();
        Node node2 = v2.asNode();
        Node node3 = v3.asNode();

        try {
            Geometry g1 = (Geometry) wktDataType.parse(node1.getLiteralLexicalForm());
            Geometry g2 = (Geometry) wktDataType.parse(node2.getLiteralLexicalForm());
            String compreMatrix = node3.getLiteral().getLexicalForm();

            IntersectionMatrix matrix = g1.relate(g2);
            boolean result = matrix.matches(compreMatrix);

            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);
            return NodeValue.FALSE;
        }
    }

}
