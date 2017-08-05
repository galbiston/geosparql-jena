/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.gml2.GMLReader;
import com.vividsolutions.jts.io.gml2.GMLWriter;
import implementation.CRSRegistry;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.datatype.parsers.wkt.WKTGeometryBuilder;
import implementation.support.GeoSerialisationEnum;
import static implementation.support.Prefixes.GEO_URI;
import java.io.IOException;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
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
        GeometryWrapper geom = (GeometryWrapper) geometry;
        GMLWriter gmlWriter = new GMLWriter(false);
        String srsName = geom.getSrsURI();
        gmlWriter.setSrsName(srsName);
        gmlWriter.setPrefix(GML_PREFIX);
        gmlWriter.setNamespace(true);

        //Change emitted Namespace to that in GeoSPARQL standard 11-052r4
        String gmlString = gmlWriter.write(geom.getParsingGeometry());
        gmlString = gmlString.replace("net/gml", "net/ont/gml");
        gmlString = gmlString.replace("'", "\"");

        return (gmlString);
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
        GMLReader gmlReader = new GMLReader();
        try {
            Geometry geometry = gmlReader.read(lexicalForm, null);
            String srsURI = findSrsURI(lexicalForm);

            DimensionInfo dimensionInfo = extractDimensionInfo(geometry);

            GeometryWrapper geom = new GeometryWrapper(geometry, srsURI, GeoSerialisationEnum.GML, dimensionInfo);
            return geom;
        } catch (IOException | ParserConfigurationException | org.xml.sax.SAXException ex) {
            LOGGER.error("Exception: {} Illegal GML literal: {}", ex.getMessage(), lexicalForm);
            return null;
        } catch (NullPointerException ex) {

            //Currently handling of empty GML.
            String srsURI = findSrsURI(lexicalForm);
            int spaceIndex = lexicalForm.indexOf(" ");
            String shape = lexicalForm.substring(5, spaceIndex).toLowerCase();
            WKTGeometryBuilder builder = new WKTGeometryBuilder(shape, "", "");
            DimensionInfo dimensionInfo = builder.getDimensionInfo();
            Geometry geometry = builder.getGeometry();

            return new GeometryWrapper(geometry, srsURI, GeoSerialisationEnum.GML, dimensionInfo);
        }
    }

    public static final String findSrsURI(String lexicalForm) {

        //Find srsName attribute
        String srsNameAttribute = "srsName=\"";
        String srsName;
        int srsIndex = lexicalForm.indexOf(srsNameAttribute);

        if (srsIndex > -1) {
            int start = srsIndex + srsNameAttribute.length();
            srsName = lexicalForm.substring(start);

            //Find end of first element
            int closeIndex = srsName.indexOf("\"");
            srsName = srsName.substring(0, closeIndex);

        } else {
            srsName = CRSRegistry.DEFAULT_WKT_CRS;
        }

        return srsName;
    }

    public static final DimensionInfo extractDimensionInfo(Geometry geometry) {

        Double z = geometry.getCoordinate().z;
        Double nullValue = Coordinate.NULL_ORDINATE;
        int coordinate = (Objects.equals(nullValue, z)) ? 2 : 3;
        int topological = geometry.getDimension();

        //Assuming that the spatial and coordinate dimensions are the same. i.e. no linear reference systems.
        return new DimensionInfo(coordinate, coordinate, topological);
    }

}
