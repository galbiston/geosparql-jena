/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.io.gml2.GMLWriter;
import static datatype.GmlDatatype.GMLPrefix;
import static datatype.GmlDatatype.GMLSRSNAme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class GeneralDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralDatatype.class);

    public GeneralDatatype() {

    }

    /**
     * This boolean tag will indicate whether the GML datatype or the WKT
     * datatype is in use for the latest query.
     */
    public static boolean isGML;

    /**
     * This method Un-parses the JTS Geometry to the WKT or GML literal
     *
     * @param geometry - the JTS Geometry to be un-parsed
     * @return WKT or GML - the returned WKT or GML Literal.
     * <br> Notice that the Spatial Reference System is not specified in
     * returned WKT literal.
     * <br> For the returned GML literal, a spatial reference system
     * "urn:ogc:def:crs:EPSG::27700" is predefined.
     *
     */
    public String unparse(Object geometry) {
        Geometry geom = (Geometry) geometry;
        if (isGML) {
            LOGGER.info("GML literal is unparsed.");
            GMLWriter gmlWriter = new GMLWriter();
            gmlWriter.setNamespace(true);
            gmlWriter.setSrsName(GMLSRSNAme);
            gmlWriter.setPrefix(GMLPrefix);
            String gml = gmlWriter.write(geom);
            return gml;
        } else {
            LOGGER.info("WKT literal is unparsed.");
            WKTWriter wktWriter = new WKTWriter();
            wktWriter.setFormatted(true);
            String wkt = wktWriter.write(geom);
            return wkt;
        }
    }

    /**
     * This method Parses the WKT or GML literal to the JTS Geometry
     *
     * @param lexicalForm - the input literal to be parsed.
     * @return geometry - if the input literal is valid.
     * <br> empty geometry - if the input literal is empty.
     * <br> null - if the input literal is invalid.
     */
    public Geometry parse(String lexicalForm) {

        if (lexicalForm.isEmpty()) {
            /**
             * If the lexicalForm is empty, an empty geometry will be created.
             */
            LOGGER.info("Empty input lexical.");
            GeometryFactory geomFactory = new GeometryFactory();

            return geomFactory.createPoint(new Coordinate());
        } else if (lexicalForm.contains("gml")) {
            /**
             * If the lexicalForm contains XML tag "gml", which will indicate
             * this is a GML literal.
             */
            LOGGER.info("The GML input: {}", lexicalForm);
            isGML = true;
            GmlDatatype gmlDatatype = (GmlDatatype) GmlDatatype.theGmlDatatype;
            Geometry geometry = gmlDatatype.parse(lexicalForm);
            return geometry;
        } else {
            /**
             * Otherwise, the literal is WKT literal, However, if the input
             * literal cannot be recognized from this step, an error will be
             * thrown.
             */
            LOGGER.info("The XML input: {}", lexicalForm);
            isGML = false;
            WktDatatype wktDatatype = WktDatatype.theWktDatatype;
            Geometry geometry = wktDatatype.parse(lexicalForm);
            return geometry;
        }
    }

}
