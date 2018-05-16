/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import java.lang.invoke.MethodHandles;
import org.apache.jena.datatypes.RDFDatatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class DatatypeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final WKTDatatype WKT_DATATYPE = WKTDatatype.INSTANCE;
    private static final String WKT_DATATYPE_URI = WKTDatatype.INSTANCE.getURI();
    private static final GMLDatatype GML_DATATYPE = GMLDatatype.INSTANCE;
    private static final String GML_DATATYPE_URI = GMLDatatype.INSTANCE.getURI();

    public static final GeometryDatatype getDatatype(GeoDatatypeEnum datatypeEnum) {
        switch (datatypeEnum) {
            case GML:
                return GMLDatatype.INSTANCE;
            case WKT:
                return WKTDatatype.INSTANCE;
            default:
                LOGGER.error("Unrecognised DatatypeEnum: {}", datatypeEnum);
                throw new AssertionError("Unrecognised DatatypeEnum: " + datatypeEnum);
        }
    }

    public static final GeometryDatatype getDatatype(RDFDatatype rdfDatatype) {
        if (rdfDatatype.equals(WKTDatatype.INSTANCE)) {
            return WKTDatatype.INSTANCE;
        } else if (rdfDatatype.equals(GMLDatatype.INSTANCE)) {
            return GMLDatatype.INSTANCE;
        } else {
            LOGGER.error("Unrecognised datatype: {}", rdfDatatype.getURI());
            throw new AssertionError("Unrecognised datatype: " + rdfDatatype.getURI());
        }
    }

    public static final GeometryDatatype getDatatype(String datatypeURI) {

        if (datatypeURI.equals(WKT_DATATYPE_URI)) {
            return WKTDatatype.INSTANCE;
        } else if (datatypeURI.equals(GML_DATATYPE_URI)) {
            return GMLDatatype.INSTANCE;
        } else {
            LOGGER.error("Unrecognised datatype URI: {}", datatypeURI);
            throw new AssertionError("Unrecognised datatype URI: " + datatypeURI);
        }
    }

    public static final boolean checkGeometryDatatypeURI(String datatypeURI) {
        return datatypeURI.equals(WKT_DATATYPE_URI) || datatypeURI.equals(GML_DATATYPE_URI);
    }

    public static final boolean checkGeometryDatatype(RDFDatatype datatype) {
        return datatype.equals(WKT_DATATYPE) || datatype.equals(GML_DATATYPE);
    }
}
