/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import com.vividsolutions.jts.geom.Geometry;
import datatype.GmlDatatype;
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
public class GetSRIDFilterFunc extends FunctionBase1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSRIDFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v) {

        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;

        Node node = v.asNode();

        try {
            Geometry g1 = (Geometry) gmlDataType.parse(node.getLiteralLexicalForm());

            int srid = g1.getSRID();

            return NodeValue.makeNodeString("SRID:" + srid);
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);
            return NodeValue.nvEmptyString;
        }
    }

}
