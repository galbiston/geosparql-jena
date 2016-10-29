/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import datatype.CRSGeometry;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;

/**
 *
 * @author haozhechen
 */
public class BoundaryFilterFunc extends FunctionBase1 {

    @Override
    public NodeValue exec(NodeValue v) {

        NodeValue resultNodeValue;

        try {
            CRSGeometry geometry = CRSGeometry.extract(v);
            CRSGeometry boundary = geometry.getBoundary();
            resultNodeValue = boundary.getResultNode();

        } catch (DatatypeFormatException dfx) {
            resultNodeValue = NodeValue.nvEmptyString;
        }

        return resultNodeValue;
    }

}
