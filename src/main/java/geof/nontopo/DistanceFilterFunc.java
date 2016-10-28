/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import com.vividsolutions.jts.geom.Geometry;
import datatype.GeometryDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uom.UomConverter;

/**
 *
 * @author haozhechen
 */
public class DistanceFilterFunc extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        GeometryDatatype generalDatatype = new GeometryDatatype();

        Node node1 = v1.asNode();
        Node node2 = v2.asNode();
        //the units
        Node node3 = v3.asNode();
        String destiUnit = node3.getURI();
        LOGGER.info("Current unit: {}", node3.getLocalName());

        try {
            Geometry g1 =  generalDatatype.parse(node1.getLiteralLexicalForm());
            Geometry g2 =  generalDatatype.parse(node2.getLiteralLexicalForm());

            double distance = g1.distance(g2);
            //Default unit is central angle degrees, need t oconvert to destination uom
            distance = UomConverter.ConvertToUnit(destiUnit, distance);

            //Default unit is central angle degrees
            distance = UomConverter.ConvertToUnit(destiUnit, distance);

            return NodeValue.makeDouble(distance);
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);

            return NodeValue.nvZERO;
        }

    }

}
