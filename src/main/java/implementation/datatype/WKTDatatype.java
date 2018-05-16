/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import com.vividsolutions.jts.geom.Geometry;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.index.GeometryLiteralIndex;
import implementation.parsers.wkt.WKTReader;
import implementation.parsers.wkt.WKTWriter;
import static implementation.vocabulary.Prefixes.GEO_URI;
import implementation.vocabulary.SRS_URI;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WKTDatatype class allows the URI "geo:wktLiteral" to be used as a datatype
 * and it will parse that datatype to a JTS Geometry.
 */
public class WKTDatatype extends GeometryDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKTDatatype.class);

    /**
     * The default WKT type URI.
     */
    public static final String URI = GEO_URI + "wktLiteral";

    /**
     * A static instance of WKTDatatype.
     */
    public static final WKTDatatype INSTANCE = new WKTDatatype();

    /**
     * private constructor - single global instance.
     */
    private WKTDatatype() {
        super(URI);
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

        if (geometry instanceof GeometryWrapper) {
            GeometryWrapper geometryWrapper = (GeometryWrapper) geometry;
            return WKTWriter.write(geometryWrapper);
        } else {
            throw new AssertionError("Object passed to WKTDatatype is not a GeometryWrapper: " + geometry);
        }
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
            return GeometryLiteralIndex.retrieve(lexicalForm, this);
        } catch (ParseException | IllegalArgumentException ex) {
            LOGGER.error("{} - Illegal WKT literal: {} ", ex.getMessage(), lexicalForm);
            throw new DatatypeFormatException(ex.getMessage() + " - Illegal WKT literal: " + lexicalForm);
        }
    }

    @Override
    public GeometryWrapper read(String geometryLiteral) {
        WKTTextSRS wktTextSRS = new WKTTextSRS(geometryLiteral);

        WKTReader wktReader = WKTReader.extract(wktTextSRS.getWktText());

        Geometry geometry = wktReader.getGeometry();
        DimensionInfo dimensionInfo = wktReader.getDimensionInfo();

        return new GeometryWrapper(geometry, wktTextSRS.getSrsURI(), GeoDatatypeEnum.WKT, dimensionInfo);
    }

    private class WKTTextSRS {

        private final String wktText;
        private final String srsURI;

        public WKTTextSRS(String wktLiteral) {
            int startSRS = wktLiteral.indexOf("<");
            int endSRS = wktLiteral.indexOf(">");

            //Check that both chevrons are located and extract SRS_URI name, otherwise default.
            if (startSRS != -1 && endSRS != -1) {
                srsURI = wktLiteral.substring(startSRS + 1, endSRS);
                wktText = wktLiteral.substring(endSRS + 1);

            } else {
                srsURI = SRS_URI.DEFAULT_WKT_CRS84;
                wktText = wktLiteral;
            }
        }

        public String getWktText() {
            return wktText;
        }

        public String getSrsURI() {
            return srsURI;
        }

    }

    @Override
    public String toString() {
        return "WKTDatatype{" + URI + '}';
    }

}
