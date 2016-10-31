/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological;

import datatype.GeometryWrapper;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;

/**
 *
 * @author Greg
 */
public abstract class GenericFilterFunction extends FunctionBase2 {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
            GeometryWrapper geometry2 = GeometryWrapper.extract(v2);

            boolean result = relate(geometry1, geometry2);

            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException dfx) {
            return NodeValue.FALSE;
        }
    }

    protected abstract boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry);

}
