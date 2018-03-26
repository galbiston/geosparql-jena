/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import implementation.GeometryWrapper;
import implementation.datatype.parsers.ParseException;
import implementation.datatype.parsers.gml.GMLReader;
import implementation.datatype.parsers.gml.GMLWriter;
import static implementation.support.Prefixes.GEO_URI;
import java.io.IOException;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 */
public class GMLDatatype extends BaseDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(GMLDatatype.class);

    /**
     * The default GML type URI.
     */
    public static final String THE_TYPE_URI = GEO_URI + "gmlLiteral";

    /**
     * A static instance of GMLDatatype.
     */
    public static final GMLDatatype THE_GML_DATATYPE = new GMLDatatype();

    /**
     * XML element tag "gml" is defined for the convenience of GML generation.
     */
    public static final String GML_PREFIX = "gml";

    /**
     * The spatial reference system "urn:ogc:def:crs:OGC::CRS84" is returned for
     * all generated GML literal.
     */
    public static final String GML_SRS_NAME = "urn:ogc:def:crs:OGC::CRS84";

    /**
     * private constructor - single global instance.
     */
    public GMLDatatype() {
        super(THE_TYPE_URI);
    }

    /**
     * This method Un-parses the JTS Geometry to the GML literal
     *
     * @param geometry - the JTS Geometry to be un-parsed
     * @return GML - the returned GML Literal.
     * <br> Notice that the Spatial Reference System
     * "urn:ogc:def:crs:OGC::CRS84" is predefined in the returned GML literal.
     */
    @Override
    public String unparse(Object geometry) {
        GeometryWrapper geometryWrapper = (GeometryWrapper) geometry;
        return GMLWriter.write(geometryWrapper);
    }

    /**
     * This method Parses the GML literal to the JTS Geometry
     *
     * @param lexicalForm - the GML literal to be parsed
     * @return geometry - if the GML literal is valid.
     * <br> empty geometry - if the GML literal is empty.
     * <br> null - if the GML literal is invalid.
     */
    @Override
    public GeometryWrapper parse(String lexicalForm) throws DatatypeFormatException {
        try {
            return GMLReader.read(lexicalForm);
        } catch (ParseException | IllegalArgumentException | JDOMException | IOException ex) {
            LOGGER.error("{} - Illegal GML literal: {} ", ex.getMessage(), lexicalForm);
            throw new DatatypeFormatException("Illegal GML literal:" + lexicalForm);
        }
    }

}
