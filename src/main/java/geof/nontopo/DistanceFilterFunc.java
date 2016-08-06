/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import datatype.GmlDatatype;
import com.vividsolutions.jts.geom.Geometry;
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
public class DistanceFilterFunc extends FunctionBase3 {

    private static Logger LOGGER = LoggerFactory.getLogger(DistanceFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;

        Node node1 = v1.asNode();
        Node node2 = v2.asNode();
        //the units
        Node node3 = v3.asNode();
        LOGGER.info("Current unit: {}", node3.getLocalName());
        //Default unit is central angle degrees

        Geometry g1 = (Geometry) gmlDataType.parse(node1.getLiteralLexicalForm());
        Geometry g2 = (Geometry) gmlDataType.parse(node2.getLiteralLexicalForm());

        Double distance = g1.distance(g2);

        return NodeValue.makeDouble(distance);
    }

}
