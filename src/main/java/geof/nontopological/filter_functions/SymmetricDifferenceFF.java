/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopological.filter_functions;

import implementation.GeometryWrapper;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class SymmetricDifferenceFF extends FunctionBase2 {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
            GeometryWrapper geometry2 = GeometryWrapper.extract(v2);

            GeometryWrapper difference = geometry1.symDifference(geometry2);
            return difference.asNode();

        } catch (DatatypeFormatException | FactoryException | MismatchedDimensionException | TransformException dfx) {
            return NodeValue.nvEmptyString;
        }

    }

}
