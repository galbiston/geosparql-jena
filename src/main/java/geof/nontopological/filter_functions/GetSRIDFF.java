/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopological.filter_functions;

import implementation.GeometryWrapper;
import java.lang.invoke.MethodHandles;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 */
public class GetSRIDFF extends FunctionBase1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public NodeValue exec(NodeValue v) {

        try {
            GeometryWrapper geometryWrapper = GeometryWrapper.extract(v);

            String srid = geometryWrapper.getSRID();

            return NodeValue.makeNodeString(srid);
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Datatype Format Exception: {}, {}", v, dfx.getMessage());
            return NodeValue.nvEmptyString;
        }
    }

}
