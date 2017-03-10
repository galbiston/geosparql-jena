/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.gml;

import com.jcabi.xml.XMLDocument;
import com.vividsolutions.jts.geom.*;
import implementation.CRSRegistry;
import implementation.DimensionInfo;
import implementation.datatype.parsers.ParseException;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequenceFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author haozhechen
 */
public class GMLGeometryBuilder {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    private final CustomCoordinateSequence.CoordinateSequenceDimensions coordinateSequenceDimensions;
    private final Geometry geometry;

    //Geometry attributes
    private static String srsName;
    private static int srsDimension;

    public GMLGeometryBuilder(String shape, int dimension, Node gmlNode) {
        this.coordinateSequenceDimensions = convertDimensions(dimension);
        this.geometry = buildGeometry(shape, gmlNode);
    }

    public static String getSrsName() {
        return srsName;
    }

    public static void setSrsName(String srsName) {
        GMLGeometryBuilder.srsName = srsName;
    }

    public static int getSrsDimension() {
        return srsDimension;
    }

    public static void setSrsDimension(int srsDimension) {
        GMLGeometryBuilder.srsDimension = srsDimension;
    }

    private static CustomCoordinateSequence.CoordinateSequenceDimensions convertDimensions(int dimension) {

        CustomCoordinateSequence.CoordinateSequenceDimensions dims;
        if (dimension == 3) {
            dims = CustomCoordinateSequence.CoordinateSequenceDimensions.XYZ;
        } else {
            dims = CustomCoordinateSequence.CoordinateSequenceDimensions.XY;
        }
        return dims;
    }

    // In this case, the coordinate dimension, spatial dimension, and topological dimension will be same
    public DimensionInfo getDimensionInfo() {
        return new DimensionInfo(getDimension(), getDimension(), geometry.getDimension());
    }

    public int getDimension() {

        switch (coordinateSequenceDimensions) {
            case XYZ:
                return 3;
            default:
                return 2;
        }
    }

    public Geometry getGeometry() {
        return geometry;
    }

    private Geometry buildGeometry(String shape, Node gmlNode) {

        Geometry geo;
        switch (shape) {
            case "Point":
                CustomCoordinateSequence pointSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(gmlNode.getFirstChild().getTextContent()));
                geo = GEOMETRY_FACTORY.createPoint(pointSequence);
                break;
            case "LineString":
                CustomCoordinateSequence lineSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(gmlNode.getFirstChild().getTextContent()));
                geo = GEOMETRY_FACTORY.createLineString(lineSequence);
                break;
            case "Polygon":
                geo = buildPolygon(gmlNode);
                break;
            case "MultiPoint":
                geo = buildMultiPoint(gmlNode);
                break;
            case "MultiLineString":
                geo = buildMultiLineString(gmlNode);
                break;
            case "MultiPolygon":
                geo = buildMultiPolygon(gmlNode);
                break;
            case "GeometryCollection":
                geo = buildGeometryCollection(gmlNode);
                break;
            default:
                throw new ParseException("Geometry shape not supported: " + shape);
        }
        return geo;
    }

    private String rebuildCoordinates(String unclean) {
        StringBuilder sb = new StringBuilder("");
        String[] coordinates = unclean.trim().split(" ");
        int dimension = getSrsDimension();
        for (int i = 0; i < coordinates.length; i++) {
            if ((i + 1) % dimension == 0 && (i + 1) != coordinates.length) {
                sb.append(" ").append(coordinates[i]).append(",");
            } else if ((i + 1) == coordinates.length) {
                sb.append(" ").append(coordinates[i]);
            } else {
                sb.append(" ").append(coordinates[i]);
            }
        }
        return sb.toString();
    }

    private Geometry buildGeometryCollection(Node gmlNode) {

        NodeList nodeList = gmlNode.getChildNodes();
        Geometry[] geometries = new Geometry[nodeList.getLength()];

        for (int i = 0; i < nodeList.getLength(); i++) {
            String shape = nodeList.item(i).getFirstChild().getLocalName();
            Node geometryNode = nodeList.item(i).getFirstChild();
            geometries[i] = buildGeometry(shape, geometryNode);
        }

        return GEOMETRY_FACTORY.createGeometryCollection(geometries);
    }

    private Geometry buildMultiPoint(Node gmlNode) {

        NodeList nodeList = gmlNode.getChildNodes();
        Point[] points = new Point[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            String coordinates = nodeList.item(i).getTextContent();
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(coordinates));

            points[i] = GEOMETRY_FACTORY.createPoint(sequence);
        }
        return GEOMETRY_FACTORY.createMultiPoint(points);
    }

    private Geometry buildMultiLineString(Node gmlNode) {

        NodeList nodeList = gmlNode.getChildNodes();
        LineString[] lineStrings = new LineString[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            String coordinates = nodeList.item(i).getTextContent();
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(coordinates));

            lineStrings[i] = GEOMETRY_FACTORY.createLineString(sequence);
        }
        return GEOMETRY_FACTORY.createMultiLineString(lineStrings);
    }

    private Geometry buildMultiPolygon(Node gmlNode) {

        NodeList nodeList = gmlNode.getChildNodes();
        Polygon[] polygons = new Polygon[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node polygonNode = nodeList.item(i).getFirstChild();
            polygons[i] = buildPolygon(polygonNode);
        }

        return GEOMETRY_FACTORY.createMultiPolygon(polygons);
    }

    private Polygon buildPolygon(Node gmlNode) {

        Polygon polygon;

        NodeList nodeList = gmlNode.getChildNodes();

        if (nodeList.getLength() == 1) {
            String coordinates = nodeList.item(0).getTextContent();
            CustomCoordinateSequence shellSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(coordinates));
            polygon = GEOMETRY_FACTORY.createPolygon(shellSequence);
        } else {
            List<LinearRing> linearList = new ArrayList<>();
            CustomCoordinateSequence shellSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(nodeList.item(0).getTextContent()));
            LinearRing shellLinearRing = GEOMETRY_FACTORY.createLinearRing(shellSequence);

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getLocalName().equals("Interior")) {
                    String coordinates = nodeList.item(i).getTextContent();
                    CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, rebuildCoordinates(coordinates));
                    LinearRing linearRing = GEOMETRY_FACTORY.createLinearRing(sequence);
                    linearList.add(linearRing);
                }
            }
            LinearRing[] holesLinearRing = linearList.toArray(new LinearRing[linearList.size()]);
            polygon = GEOMETRY_FACTORY.createPolygon(shellLinearRing, holesLinearRing);
        }

        return polygon;
    }

    public static GMLGeometryBuilder extract(String gmlText) {

        XMLDocument xmlDoc = new XMLDocument(gmlText);
        NamedNodeMap attributes = xmlDoc.node().getFirstChild().getAttributes();
        setSrsName(attributes.getNamedItem("srsName").getNodeValue());

        Node dimensionNode = attributes.getNamedItem("srsDimension");
        if (dimensionNode != null) {
            String nodeValue = dimensionNode.getNodeValue();
            setSrsDimension(Integer.parseInt(nodeValue));

        } else {  //srsDimension attribute is missing so extract from the srsURI.

            CoordinateReferenceSystem crs = CRSRegistry.getCRS(getSrsName());
            setSrsDimension(crs.getCoordinateSystem().getDimension());
        }
        String shape = xmlDoc.node().getFirstChild().getLocalName();
        Node gmlNode = xmlDoc.node().getFirstChild();

        return new GMLGeometryBuilder(shape, srsDimension, gmlNode);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.coordinateSequenceDimensions);
        hash = 97 * hash + Objects.hashCode(this.geometry);
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
        if (this.coordinateSequenceDimensions != other.coordinateSequenceDimensions) {
            return false;
        }
        if (!Objects.equals(this.geometry, other.geometry)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GMLInfo{" + "coordinateSequenceDimensions=" + coordinateSequenceDimensions + ", geometry=" + geometry + '}';
    }

}
