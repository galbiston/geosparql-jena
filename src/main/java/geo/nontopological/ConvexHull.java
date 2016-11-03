/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.nontopological;

import implementation.GeometryWrapper;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class ConvexHull extends FunctionBase1 {

    @Override
    public NodeValue exec(NodeValue v) {

        try {
            GeometryWrapper geometry = GeometryWrapper.extract(v);

            GeometryWrapper convexHull = geometry.convexHull();
            return convexHull.getResultNode();

        } catch (DatatypeFormatException dfx) {
            return NodeValue.nvEmptyString;
        }

    }

}
