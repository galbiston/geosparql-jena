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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 *
 */
public class GMLGeometryBuilder {

    /*
    TODO: decide whether to replace with GDAL/OGR library for Geometry which supports WKT and GML formats. Doesn't have DE-9IM intersection matrix support though.
     */
    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    //Geometry attributes
    private final CustomCoordinateSequence.CoordinateSequenceDimensions coordinateSequenceDimensions;
    private final Geometry geometry;
    private final String srsName;
    private final int srsDimension;
    private final DimensionInfo dimensionInfo;

    private static final Namespace GML_NAMESPACE = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");

    public GMLGeometryBuilder(String shape, Element gmlElement, String srsName, int srsDimension) throws ParseException {
        this.srsName = srsName;
        this.srsDimension = srsDimension;
        this.coordinateSequenceDimensions = convertDimensions(srsDimension);
        this.geometry = buildGeometry(shape, gmlElement);
        this.dimensionInfo = new DimensionInfo(srsDimension, srsDimension, geometry.getDimension());
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getSrsName() {
        return srsName;
    }

    public int getSrsDimension() {
        return srsDimension;
    }

    public DimensionInfo getDimensionInfo() {
        return dimensionInfo;
    }

    private static CustomCoordinateSequence.CoordinateSequenceDimensions convertDimensions(int dimension) {

        if (dimension == 3) {
            return CustomCoordinateSequence.CoordinateSequenceDimensions.XYZ;
        } else {
            return CustomCoordinateSequence.CoordinateSequenceDimensions.XY;
        }
    }

    private Geometry buildGeometry(String shape, Element gmlElement) throws ParseException {

        Geometry geo;
        switch (shape) {
            case "Point":
            case "LineString":
                Element coordinates = gmlElement.getChild("coordinates", GML_NAMESPACE);
                String coordinateString = coordinates.getTextNormalize();
                String rebuildCoordinates = rebuildCoordinates(coordinateString);
                CustomCoordinateSequence coordinateSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates);
                geo = buildBasic(shape, coordinateSequence);
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

    private Geometry buildBasic(String shape, CustomCoordinateSequence coordinateSequence) throws ParseException {
        switch (shape) {
            case "Point":
                return GEOMETRY_FACTORY.createPoint(coordinateSequence);
            case "LineString":
                return GEOMETRY_FACTORY.createLineString(coordinateSequence);
            default:
                throw new ParseException("Geometry shape not supported: " + shape);
        }
    }

    private String rebuildCoordinates(String unclean) {
        //GML uses ' ' to seperate coordinate pairs and ',' as delimiter while WKT and JTS is reverse.
        StringBuilder sb = new StringBuilder("");
        String[] coordinates = unclean.trim().split(" ");

        for (int i = 0; i < coordinates.length; i++) {
            if ((i + 1) % srsDimension == 0 && (i + 1) != coordinates.length) {
                sb.append(" ").append(coordinates[i]).append(",");
            } else if ((i + 1) == coordinates.length) {
                sb.append(" ").append(coordinates[i]);
            } else {
                sb.append(" ").append(coordinates[i]);
            }
        }
        return sb.toString();
    }

    private Polygon buildPolygon(Element gmlElement) {

        Polygon polygon;

        //Exterior shell
        Element exterior = gmlElement.getChild("Exterior", GML_NAMESPACE);
        String coordinates = exterior.getChildText("PosList", GML_NAMESPACE);
        CustomCoordinateSequence exteriorSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(coordinates));

        //Interior shell - that may not be present.
        List<Element> interiors = gmlElement.getChildren("Interior", GML_NAMESPACE);
        List<LinearRing> interiorLinearRingList = new ArrayList<>();
        for (Element interior : interiors) {
            String interiorCoordinates = interior.getChildText("PosList", GML_NAMESPACE);
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(interiorCoordinates));
            LinearRing linearRing = GEOMETRY_FACTORY.createLinearRing(sequence);
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
            String coordinates = child.getChild("Point", GML_NAMESPACE).getTextNormalize();
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(coordinates));

            points[i] = GEOMETRY_FACTORY.createPoint(sequence);
        }
        return GEOMETRY_FACTORY.createMultiPoint(points);
    }

    private Geometry buildMultiLineString(Element gmlElement) {

        List<Element> children = gmlElement.getChildren();
        LineString[] lineStrings = new LineString[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            String coordinates = child.getChild("LineString", GML_NAMESPACE).getTextNormalize();
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(coordinates));

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

        String srsName = gmlElement.getAttributeValue("srsName");
        String dimension = gmlElement.getAttributeValue("srsDimension");

        int srsDimension;
        if (dimension == null) {
            //srsDimension attribute is missing so extract from the srsURI.
            CoordinateReferenceSystem crs = CRSRegistry.getCRS(srsName);
            srsDimension = crs.getCoordinateSystem().getDimension();
        } else {
            srsDimension = Integer.parseInt(dimension);
        }
        String shape = gmlElement.getName();

        return new GMLGeometryBuilder(shape, gmlElement, srsName, srsDimension);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.coordinateSequenceDimensions);
        hash = 89 * hash + Objects.hashCode(this.geometry);
        hash = 89 * hash + Objects.hashCode(this.srsName);
        hash = 89 * hash + this.srsDimension;
        hash = 89 * hash + Objects.hashCode(this.dimensionInfo);
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
        return Objects.equals(this.dimensionInfo, other.dimensionInfo);
    }

    @Override
    public String toString() {
        return "GMLGeometryBuilder{" + "coordinateSequenceDimensions=" + coordinateSequenceDimensions + ", geometry=" + geometry + ", srsName=" + srsName + ", srsDimension=" + srsDimension + ", dimensionInfo=" + dimensionInfo + '}';
    }

}
