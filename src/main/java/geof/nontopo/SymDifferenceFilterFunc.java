/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import datatype.CRSGeometry;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author haozhechen
 */
public class SymDifferenceFilterFunc extends FunctionBase2 {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        NodeValue resultNodeValue;

        try {
            CRSGeometry geometry1 = CRSGeometry.extract(v1);
            CRSGeometry geometry2 = CRSGeometry.extract(v2);

            CRSGeometry difference = geometry1.symDifference(geometry2);
            resultNodeValue = difference.getResultNode();

        } catch (DatatypeFormatException | FactoryException | MismatchedDimensionException | TransformException dfx) {
            resultNodeValue = NodeValue.nvEmptyString;
        }

        return resultNodeValue;
    }

}
