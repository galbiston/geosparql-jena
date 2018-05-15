/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopological.filter_functions;

import implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;

/**
 *
 *
 *
 */
public class ConvexHullFF extends FunctionBase1 {

    @Override
    public NodeValue exec(NodeValue v) {

        GeometryWrapper geometry = GeometryWrapper.extract(v);
        if (geometry == null) {
            return NodeValue.nvEmptyString;
        }
        GeometryWrapper convexHull = geometry.convexHull();
        return convexHull.asNode();

    }

}
