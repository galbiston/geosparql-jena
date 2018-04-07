/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import implementation.GeometryWrapper;
import implementation.parsers.wkt.WKTReader;
import implementation.parsers.wkt.WKTWriter;
import static implementation.support.Prefixes.GEO_URI;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 *
 * description: WKTDatatype class allows the URI "geo:gmlLiteral" to be used as
 * a datatype and it will parse that datatype to a JTL Geometry.
 */
public class WKTDatatype extends BaseDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKTDatatype.class);

    /**
     * The default WKT type URI.
     */
    public static final String THE_TYPE_URI = GEO_URI + "wktLiteral";

    /**
     * A static instance of WKTDatatype.
     */
    public static final WKTDatatype THE_WKT_DATATYPE = new WKTDatatype();

    /**
     * private constructor - single global instance.
     */
    private WKTDatatype() {
        super(THE_TYPE_URI);
    }

    /**
     * This method Un-parses the JTS Geometry to the WKT literal
     *
     * @param geometry - the JTS Geometry to be un-parsed
     * @return WKT - the returned WKT Literal.
     * <br> Notice that the Spatial Reference System is not specified in
     * returned WKT literal.
     *
     */
    @Override
    public String unparse(Object geometry) {

        GeometryWrapper geometryWrapper = (GeometryWrapper) geometry;
        return WKTWriter.write(geometryWrapper);
    }

    /**
     * This method Parses the WKT literal to the JTS Geometry
     *
     *
     * Req 10 All RDFS Literals of type geo:wktLiteral shall consist of an
     * optional URI identifying the coordinate reference system followed by
     * Simple Features Well Known Text (WKT) describing a geometric value. Valid
     * geo:wktLiterals are formed by concatenating a valid, absolute URI as
     * defined in [RFC 2396], one or more spaces (Unicode U+0020 character) as a
     * separator, and a WKT string as defined in Simple Features [ISO 19125-1].
     *
     * Req 11 The URI <http://www.opengis.net/def/crs/OGC/1.3/CRS84> shall be
     * assumed as the spatial reference system for geo:wktLiterals that do not
     * specify an explicit spatial reference system URI.
     *
     *
     * @param lexicalForm - the WKT literal to be parsed
     * @return geometry - if the WKT literal is valid. empty geometry - if the
     * WKT literal is empty. null - if the WKT literal is invalid.
     */
    @Override
    public GeometryWrapper parse(String lexicalForm) throws DatatypeFormatException {

        try {
            return WKTReader.read(lexicalForm);
        } catch (ParseException | IllegalArgumentException ex) {
            LOGGER.error("{} - Illegal WKT literal: {} ", ex.getMessage(), lexicalForm);
            throw new DatatypeFormatException(ex.getMessage() + " - Illegal WKT literal: " + lexicalForm);
        }

    }

}
