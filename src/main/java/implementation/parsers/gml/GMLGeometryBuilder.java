/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.parsers.gml;

import com.vividsolutions.jts.geom.*;
import implementation.CRSRegistry;
import implementation.CustomGeometryFactory;
import implementation.DimensionInfo;
import implementation.datatype.ParseException;
import implementation.jts.CustomCoordinateSequence;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GMLGeometryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    //Geometry attributes
    private final CustomCoordinateSequence.CoordinateSequenceDimensions coordinateSequenceDimensions;
    private final Geometry geometry;
    private final String srsName;
    private final CoordinateReferenceSystem crs;
    private final int srsDimension;
    private final DimensionInfo dimensionInfo;

    private static final Namespace GML_NAMESPACE = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");

    /**
     * Based on GML Simple Features Profile 2.0: 10-100R3. All point geometries
     * must use <gml:pos> child element and all other geometries <gml:posList>.
     * "srsDimension" attribute found on the <gml:posList> element. Supporting
     * the same geometries found as in WKT: Point, LineString and Polygon.
     *
     * @link https://en.wikipedia.org/wiki/Geography_Markup_Language#GML_Simple_Features_Profile
     * @link https://portal.opengeospatial.org/files/?artifact_id=42729
     *
     * @param gmlElement
     * @throws ParseException
     */
    public GMLGeometryBuilder(Element gmlElement) throws ParseException {
        this.srsName = getSRSName(gmlElement);
        this.crs = CRSRegistry.getCRS(srsName);
        this.srsDimension = getSRSDimension(gmlElement, crs);
        this.coordinateSequenceDimensions = convertDimensions(srsDimension);
        String shape = gmlElement.getName();
        this.geometry = buildGeometry(shape, gmlElement);
        this.dimensionInfo = new DimensionInfo(srsDimension, srsDimension, geometry.getDimension());
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getSrsName() {
        return srsName;
    }

    public CustomCoordinateSequence.CoordinateSequenceDimensions getCoordinateSequenceDimensions() {
        return coordinateSequenceDimensions;
    }

    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    public int getSrsDimension() {
        return srsDimension;
    }

    public DimensionInfo getDimensionInfo() {
        return dimensionInfo;
    }

    private static Boolean isSRSNameWarningIssued = false;

    private String getSRSName(Element gmlElement) {
        String srsNameURI = gmlElement.getAttributeValue("srsName");
        if (srsNameURI == null) {
            srsNameURI = CRSRegistry.DEFAULT_WKT_CRS84;
            if (!isSRSNameWarningIssued) {
                LOGGER.warn("GML Literal with no srsName. Defaulting to CRS84 {} used as WKT default CRS. This warning will be issued once.", srsNameURI);
                isSRSNameWarningIssued = true;
            }

        }
        return srsNameURI;
    }

    private Integer getSRSDimension(Element gmlElement, CoordinateReferenceSystem crs) {
        //Get from pos list or CRS.
        Element posList = gmlElement.getChild("posList", GML_NAMESPACE);
        Integer srsDim = null;
        if (posList != null) {
            String srsDimensionStr = posList.getAttributeValue("srsDimension");
            if (srsDimensionStr != null) {
                srsDim = Integer.parseInt(srsDimensionStr);
            }
        }

        //Fall back to CRS coordinate reference system.
        if (srsDim == null) {
            srsDim = crs.getCoordinateSystem().getDimension();
        }

        return srsDim;
    }

    private static CustomCoordinateSequence.CoordinateSequenceDimensions convertDimensions(int dimension) {

        if (dimension == 3) {
            return CustomCoordinateSequence.CoordinateSequenceDimensions.XYZ;
        } else {
            return CustomCoordinateSequence.CoordinateSequenceDimensions.XY;
        }
    }

    private Geometry buildGeometry(String shape, Element gmlElement) throws ParseException {
        System.out.println(shape + " - " + gmlElement);
        Geometry geo;
        switch (shape) {
            case "Point":
                geo = buildPoint(gmlElement);
                break;
            case "LineString":
                geo = buildLineString(gmlElement);
                break;
            case "Polygon":
                geo = buildPolygon(gmlElement);
                break;
            case "MultiPoint":
                geo = buildMultiPoint(gmlElement);
                break;
            case "MultiLineString":
                geo = buildMultiLineString(gmlElement);
                break;
            case "MultiPolygon":
                geo = buildMultiPolygon(gmlElement);
                break;
            case "GeometryCollection":
                geo = buildGeometryCollection(gmlElement);
                break;
            default:
                throw new ParseException("Geometry shape not supported: " + shape);
        }
        return geo;
    }

    private CustomCoordinateSequence extractPos(Element gmlElement) {
        String coordinates = gmlElement.getChildTextNormalize("pos", GML_NAMESPACE);
        if (coordinates == null) {
            coordinates = "";
        }
        return new CustomCoordinateSequence(coordinateSequenceDimensions, coordinates);
    }

    private CustomCoordinateSequence extractPosList(Element gmlElement) {
        String posList = gmlElement.getChildTextNormalize("posList", GML_NAMESPACE);
        return new CustomCoordinateSequence(coordinateSequenceDimensions, convertPosList(posList));
    }

    private String convertPosList(String originalCoordinates) {
        StringBuilder sb = new StringBuilder("");
        String[] coordinates = originalCoordinates.trim().split(" ");

        int mod = coordinates.length % srsDimension;
        if (mod != 0) {
            LOGGER.error("GML Pos List does not divide into srs dimension: {} divide {} remainder {}.", coordinates.length, srsDimension, mod);
            throw new ParseException("GML Pos List does not divide into srs dimension: " + coordinates.length + " divide " + srsDimension + " remainder " + mod + ".");
        }

        int finalCoordinate = coordinates.length - 1;
        for (int i = 0; i < coordinates.length; i++) {
            if (i != 0 & i % srsDimension == 0) {
                sb.append(",");
            }
            String coordinate = coordinates[i];
            sb.append(coordinate);
            if (i != finalCoordinate) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    private Point buildPoint(Element gmlElement) {
        CustomCoordinateSequence coordinateSequence = extractPos(gmlElement);
        return GEOMETRY_FACTORY.createPoint(coordinateSequence);
    }

    private LineString buildLineString(Element gmlElement) {
        CustomCoordinateSequence coordinateSequence = extractPosList(gmlElement);
        return GEOMETRY_FACTORY.createLineString(coordinateSequence);
    }

    private Polygon buildPolygon(Element gmlElement) {
        //Following Polygon structure from: http://www.gdal.org/drv_gml.html
        Polygon polygon;

        //Exterior shell
        Element exteriorElement = gmlElement.getChild("exterior", GML_NAMESPACE);
        if (exteriorElement == null) {
            exteriorElement = gmlElement.getChild("outerBoundaryIs", GML_NAMESPACE);
        }
        Element exteriorLinearRingElement = exteriorElement.getChild("LinearRing", GML_NAMESPACE);
        CustomCoordinateSequence exteriorSequence = extractPosList(exteriorLinearRingElement);

        //Interior shell - that may not be present.
        List<Element> interiorElements = gmlElement.getChildren("interior", GML_NAMESPACE);
        if (interiorElements == null) {
            interiorElements = gmlElement.getChildren("innerBoundaryIs", GML_NAMESPACE);
        }
        List<LinearRing> interiorLinearRingList = new ArrayList<>();
        for (Element interiorElement : interiorElements) {
            Element interiorLinearRingElement = interiorElement.getChild("LinearRing", GML_NAMESPACE);
            CustomCoordinateSequence interiorSequence = extractPosList(interiorLinearRingElement);
            LinearRing linearRing = GEOMETRY_FACTORY.createLinearRing(interiorSequence);
            interiorLinearRingList.add(linearRing);
        }

        //Build the polygon depending on whether interior shells were found.
        if (interiorLinearRingList.isEmpty()) {
            polygon = GEOMETRY_FACTORY.createPolygon(exteriorSequence);
        } else {
            LinearRing exteriorLinearRing = GEOMETRY_FACTORY.createLinearRing(exteriorSequence);
            LinearRing[] interiorLinerRings = interiorLinearRingList.toArray(new LinearRing[interiorLinearRingList.size()]);
            polygon = GEOMETRY_FACTORY.createPolygon(exteriorLinearRing, interiorLinerRings);
        }

        return polygon;
    }

    private Geometry buildMultiPoint(Element gmlElement) {

        List<Element> children = gmlElement.getChildren();
        Point[] points = new Point[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            Element point = child.getChild("Point", GML_NAMESPACE);
            CustomCoordinateSequence sequence = extractPos(point);

            points[i] = GEOMETRY_FACTORY.createPoint(sequence);
        }
        return GEOMETRY_FACTORY.createMultiPoint(points);
    }

    private Geometry buildMultiLineString(Element gmlElement) {

        List<Element> children = gmlElement.getChildren();
        LineString[] lineStrings = new LineString[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            Element lineString = child.getChild("LineString", GML_NAMESPACE);
            CustomCoordinateSequence sequence = extractPosList(lineString);

            lineStrings[i] = GEOMETRY_FACTORY.createLineString(sequence);
        }
        return GEOMETRY_FACTORY.createMultiLineString(lineStrings);
    }

    private Geometry buildMultiPolygon(Element gmlElement) {

        List<Element> children = gmlElement.getChildren();
        Polygon[] polygons = new Polygon[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            polygons[i] = buildPolygon(child.getChild("Polygon", GML_NAMESPACE));
        }

        return GEOMETRY_FACTORY.createMultiPolygon(polygons);
    }

    private Geometry buildGeometryCollection(Element gmlElement) {

        List<Element> children = gmlElement.getChildren();
        Geometry[] geometries = new Geometry[children.size()];

        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);

            //Geometry Members
            for (Element grandChild : child.getChildren()) {
                String shape = grandChild.getName();
                geometries[i] = buildGeometry(shape, grandChild);
            }
        }

        return GEOMETRY_FACTORY.createGeometryCollection(geometries);
    }

    public static GMLGeometryBuilder extract(String gmlText) throws JDOMException, IOException {

        SAXBuilder jdomBuilder = new SAXBuilder();
        InputStream stream = new ByteArrayInputStream(gmlText.getBytes("UTF-8"));
        Document xmlDoc = jdomBuilder.build(stream);

        Element gmlElement = xmlDoc.getRootElement();

        return new GMLGeometryBuilder(gmlElement);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.coordinateSequenceDimensions);
        hash = 61 * hash + Objects.hashCode(this.geometry);
        hash = 61 * hash + Objects.hashCode(this.srsName);
        hash = 61 * hash + Objects.hashCode(this.crs);
        hash = 61 * hash + this.srsDimension;
        hash = 61 * hash + Objects.hashCode(this.dimensionInfo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GMLGeometryBuilder other = (GMLGeometryBuilder) obj;
        if (this.srsDimension != other.srsDimension) {
            return false;
        }
        if (!Objects.equals(this.srsName, other.srsName)) {
            return false;
        }
        if (this.coordinateSequenceDimensions != other.coordinateSequenceDimensions) {
            return false;
        }
        if (!Objects.equals(this.geometry, other.geometry)) {
            return false;
        }
        if (!Objects.equals(this.crs, other.crs)) {
            return false;
        }
        return Objects.equals(this.dimensionInfo, other.dimensionInfo);
    }

    @Override
    public String toString() {
        return "GMLGeometryBuilder{" + "coordinateSequenceDimensions=" + coordinateSequenceDimensions + ", geometry=" + geometry + ", srsName=" + srsName + ", crs=" + crs + ", srsDimension=" + srsDimension + ", dimensionInfo=" + dimensionInfo + '}';
    }

}
