/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import com.vividsolutions.jts.geom.Geometry;
import datatype.WktDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class EnvelopFilterFunc extends FunctionBase1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvelopFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v) {

        RDFDatatype wktDataType = WktDatatype.theWktDatatype;

        Node node = v.asNode();

        try {
            Geometry g1 = (Geometry) wktDataType.parse(node.getLiteralLexicalForm());

            Geometry envelope = g1.getEnvelope();

            return NodeValue.makeNodeString(wktDataType.unparse(envelope));
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);
            return NodeValue.nvEmptyString;
        }
    }

}
