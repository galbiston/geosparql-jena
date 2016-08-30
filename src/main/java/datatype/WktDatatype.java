/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
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
    public static final String theTypeURI = "geo:wktLiteral";
    public static final RDFDatatype theWktDatatype = new WktDatatype();

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
     * @return wkt - the returned wkt Literal
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

        GeometryFactory geomFactory = new GeometryFactory();

        WKTReader wktReader = new WKTReader();
        try {
            if (lexicalForm.isEmpty()) {
                //Return an empty Geometry Object
                return geomFactory.createPoint(new Coordinate());
            } else {
                Geometry geometry = wktReader.read(lexicalForm);
                return geometry;
            }
        } catch (ParseException ex) {
            LOGGER.error("Illegal WKT literal: {}", lexicalForm);
            return null;
        }
    }

}
