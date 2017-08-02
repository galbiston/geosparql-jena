/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import com.jcabi.xml.XMLDocument;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.gml2.GMLReader;
import com.vividsolutions.jts.io.gml2.GMLWriter;
import implementation.CRSRegistry;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.support.GeoSerialisationEnum;
import static implementation.support.Prefixes.GEO_URI;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
        String gml = gmlWriter.write(geom.getParsingGeometry());

        return gml;
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
            XMLDocument xmlDoc = new XMLDocument(lexicalForm);
            NamedNodeMap attributes = xmlDoc.node().getAttributes();
            String srsURI = findSrsURI(attributes);

            DimensionInfo dimensionInfo = extractDimensionInfo(attributes, srsURI, geometry);

            GeometryWrapper geom = new GeometryWrapper(geometry, srsURI, GeoSerialisationEnum.GML, dimensionInfo);
            return geom;
        } catch (IOException | ParserConfigurationException | org.xml.sax.SAXException ex) {
            LOGGER.error("Illegal GML literal: {}", lexicalForm);
            return null;
        }
    }

    public static final String findSrsURI(NamedNodeMap attributes) {
        return attributes.getNamedItem("srsName").getNodeValue();
    }

    public static final DimensionInfo extractDimensionInfo(NamedNodeMap attributes, String srsURI, Geometry geometry) {

        //TODO need confirmation of spatial dimension in the case of coordinate dimension being 3.
        int coordinateDimension;
        int spatialDimension;

        Node dimensionNode = attributes.getNamedItem("srsDimension");
        if (dimensionNode != null) {
            String nodeValue = dimensionNode.getNodeValue();
            coordinateDimension = Integer.parseInt(nodeValue);

            switch (coordinateDimension) {
                case 2:
                    spatialDimension = coordinateDimension;
                    break;
                case 3:  //Retrieve spatialDimension from CRS as could be 2 or 3.
                    CoordinateReferenceSystem crs = CRSRegistry.getCRS(srsURI);
                    spatialDimension = crs.getCoordinateSystem().getDimension();
                    break;
                default:
                    spatialDimension = 3;
                    break;
            }

        } else {  //srsDimension attribute is missing so extract from the srsURI.

            CoordinateReferenceSystem crs = CRSRegistry.getCRS(srsURI);
            coordinateDimension = crs.getCoordinateSystem().getDimension();
            spatialDimension = coordinateDimension;  //Spatial dimension considered to be same as coordinate dimension.
        }

        return new DimensionInfo(coordinateDimension, spatialDimension, geometry.getDimension());
    }

}
