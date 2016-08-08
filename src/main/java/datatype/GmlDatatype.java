/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.gml2.GMLReader;
import com.vividsolutions.jts.io.gml2.GMLWriter;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author haozhechen
 *
 * description: GmlDatatype class allows the URI "geo:gmlLiteral" to be used as
 * a datatype and it will parse that datatype to a JTL Geometry.
 */
public class GmlDatatype extends BaseDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmlDatatype.class);
    public static final String theTypeURI = "geo:gmlLiteral";
    public static final RDFDatatype theGmlDatatype = new GmlDatatype();

    public static final String GMLPrefix = "gml";
    public static final String GMLSRSNAme = "urn:ogc:def:crs:EPSG::27700";

    /**
     * private constructor - single global instance.
     */
    private GmlDatatype() {
        super(theTypeURI);
    }

    /**
     * Convert a value of this datatype out to lexical form.
     */
    @Override
    public String unparse(Object geometry) {
        Geometry geom = (Geometry) geometry;
        GMLWriter gmlWriter = new GMLWriter();
        gmlWriter.setNamespace(true);
        gmlWriter.setSrsName(GMLSRSNAme);
        gmlWriter.setPrefix(GMLPrefix);
        String gml = gmlWriter.write(geom);
        return gml;
    }

    /**
     * Parse a lexical form of this datatype to a value
     *
     * @throws DatatypeFormatException if the lexical form is not legal
     */
    @Override
    public Geometry parse(String lexicalForm) throws DatatypeFormatException {

        String gmlString = lexicalForm.toString();
        GMLReader gmlReader = new GMLReader();
        try {
            Geometry geometry = gmlReader.read(gmlString, null);
            return geometry;
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            LOGGER.error("Illegal GML literal: {}", gmlString);
            return null;
        }
    }

}
