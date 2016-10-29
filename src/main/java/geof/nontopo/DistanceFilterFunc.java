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
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import datatype.UomConverter;

/**
 *
 * @author haozhechen
 */
public class DistanceFilterFunc extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        //the units
        Node node3 = v3.asNode();
        String destiUnit = node3.getURI();
        LOGGER.info("Current unit: {}", node3.getLocalName());

        try {
            CRSGeometry geometry1 = CRSGeometry.extract(v1);
            CRSGeometry geometry2 = CRSGeometry.extract(v2);

            double distance = geometry1.distance(geometry2);
            //Default unit is central angle degrees, need t oconvert to destination uom
            distance = UomConverter.ConvertToUnit(destiUnit, distance);

            //Default unit is central angle degrees
            distance = UomConverter.ConvertToUnit(destiUnit, distance);

            return NodeValue.makeDouble(distance);
        } catch (DatatypeFormatException | FactoryException | MismatchedDimensionException | TransformException dfx) {
            return NodeValue.nvZERO;
        }

    }

}
