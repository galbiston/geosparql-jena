/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package implementation.data_conversion;

import implementation.datatype.WKTDatatype;
import java.lang.invoke.MethodHandles;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class WKTCreation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Literal createLineString(Double xMin, Double yMin, Double xMax, Double yMax) {
        return createLineString(xMin, yMin, xMax, yMax, "");
    }

    public static Literal createLineString(Double xMin, Double yMin, Double xMax, Double yMax, String srsURI) {
        String tidyURI;
        if (!srsURI.isEmpty()) {
            tidyURI = "<" + srsURI + "> ";
        } else {
            tidyURI = "";
        }
        return ResourceFactory.createTypedLiteral(tidyURI + "LINESTRING(" + xMin + " " + yMin + ", " + xMax + " " + yMax + ")", WKTDatatype.INSTANCE);
    }

}
