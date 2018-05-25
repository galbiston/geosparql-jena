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
import implementation.parsers.gml.GMLReader;
import implementation.parsers.gml.GMLWriter;
import implementation.vocabulary.Geo;
import java.io.IOException;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 */
public class GMLDatatype extends GeometryDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(GMLDatatype.class);

    /**
     * The default GML type URI.
     */
    public static final String URI = Geo.GML;

    /**
     * A static instance of GMLDatatype.
     */
    public static final GMLDatatype INSTANCE = new GMLDatatype();

    /**
     * XML element tag "gml" is defined for the convenience of GML generation.
     */
    public static final String GML_PREFIX = "gml";

    /**
     * private constructor - single global instance.
     */
    public GMLDatatype() {
        super(URI);
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
        if (geometry instanceof GeometryWrapper) {
            GeometryWrapper geometryWrapper = (GeometryWrapper) geometry;
            return GMLWriter.write(geometryWrapper);
        } else {
            throw new AssertionError("Object passed to GMLDatatype is not a GeometryWrapper: " + geometry);
        }
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
        //Check the Geometry Literal Index to see if been previously read and cached.
        //DatatypeReader interface used to instruct index on how to obtain the GeometryWrapper.
        return GeometryLiteralIndex.retrieve(lexicalForm, this);
    }

    @Override
    public GeometryWrapper read(String geometryLiteral) {
        try {
            GMLReader gmlReader = GMLReader.extract(geometryLiteral);
            Geometry geometry = gmlReader.getGeometry();
            DimensionInfo dimensionInfo = gmlReader.getDimensionInfo();

            return new GeometryWrapper(geometry, gmlReader.getSrsName(), GeoDatatypeEnum.GML, dimensionInfo);
        } catch (JDOMException | IOException ex) {
            LOGGER.error("{} - Illegal GML literal: {} ", ex.getMessage(), geometryLiteral);
            throw new DatatypeFormatException("Illegal GML literal:" + geometryLiteral);
        }
    }

    @Override
    public String toString() {
        return "GMLDatatype{" + URI + '}';
    }

}
