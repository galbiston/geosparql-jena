/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 *
 * description: WktDatatype class allows the URI "geo:gmlLiteral" to be used as
 * a datatype and it will parse that datatype to a JTL Geometry.
 */
public class WktDatatype extends BaseDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(WktDatatype.class);

    /**
     * The default WKT type URI.
     */
    public static final String theTypeURI = "http://www.opengis.net/ont/geosparql#wktLiteral";

    /**
     * A static instance of WktDatatype.
     */
    public static final WktDatatype theWktDatatype = new WktDatatype();

    /**
     * private constructor - single global instance.
     */
    private WktDatatype() {
        super(theTypeURI);
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
        Geometry geom = (Geometry) geometry;

        WKTWriter wktWriter = new WKTWriter();
        wktWriter.setFormatted(true);
        String wkt = wktWriter.write(geom);
        return wkt;

    }

    /**
     * This method Parses the WKT literal to the JTS Geometry
     *
     * @param lexicalForm - the WKT literal to be parsed
     * @return geometry - if the WKT literal is valid.
     * <br> empty geometry - if the WKT literal is empty.
     * <br> null - if the WKT literal is invalid.
     */
    @Override
    public Geometry parse(String lexicalForm) throws DatatypeFormatException {
        WKTReader wktReader = new WKTReader();
        try {
            if (lexicalForm.contains("http")) {
                /**
                 * If the lexicalForm contains the SRS URI, need to retrieve
                 * that URI.
                 */

                String SRID = lexicalForm.substring(lexicalForm.indexOf("<") + 1, lexicalForm.indexOf(">"));
                String wktLiteral = lexicalForm.replaceAll("<" + SRID + ">", "");
                Geometry geometry = wktReader.read(wktLiteral);
                geometry.setUserData(SRID);
                return geometry;
            } else {
                /**
                 * If the RDF data does not specify a spatial reference system
                 * id, then, "http://www.opengis.net/def/crs/OGC/1.3/CRS84" will
                 * be assigned to the geometry.
                 */

                String SRID = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";
                Geometry geometry = wktReader.read(lexicalForm);
                geometry.setUserData(SRID);
                return geometry;
            }
        } catch (ParseException ex) {
            LOGGER.error("Illegal WKT literal: {}", lexicalForm);
            return null;
        }
    }
}
