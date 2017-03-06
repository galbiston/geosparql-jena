/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.gml;

import com.jcabi.xml.XMLDocument;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import implementation.CRSRegistry;
import implementation.DimensionInfo;
import implementation.datatype.parsers.ParseException;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequenceFactory;
import java.util.Arrays;
import java.util.Objects;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author haozhechen
 */
public class GMLGeometryBuilder {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    private final CustomCoordinateSequence.CoordinateSequenceDimensions coordinateSequenceDimensions;
    private final Geometry geometry;

    //Geometry attributes
    public static String srsName;
    public static int srsDimension;

    public GMLGeometryBuilder(String shape, int dimension, String coordinates) {
        this.coordinateSequenceDimensions = convertDimensions(dimension);
        this.geometry = buildGeometry(shape, coordinates);
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

    private Geometry buildGeometry(String shape, String coordinates) {

        Geometry geo;
        switch (shape) {
            case "Point":
                CustomCoordinateSequence pointSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                geo = GEOMETRY_FACTORY.createPoint(pointSequence);
                break;
            case "LineString":
                CustomCoordinateSequence lineSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                geo = GEOMETRY_FACTORY.createLineString(lineSequence);
                break;
            case "Polygon":
                geo = buildPolygon(coordinates);
                break;
            case "MultiPoint":
                CustomCoordinateSequence multiPointSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                geo = GEOMETRY_FACTORY.createMultiPoint(multiPointSequence);
                break;
            case "MultiLineString":
                String[] splitCoordinates = splitCoordinates(coordinates);
                LineString[] lineStrings = splitLineStrings(splitCoordinates);
                geo = GEOMETRY_FACTORY.createMultiLineString(lineStrings);
                break;
            case "MultiPolygon":
                geo = buildMultiPolygon(coordinates);
                break;
            case "GeometryCollection":
                geo = buildGeometryCollection(coordinates);
                break;
            default:
                throw new ParseException("Geometry shape not supported: " + shape);
        }
        return geo;
    }

    private String clean(String unclean) {
        int spaceIndex = unclean.trim().indexOf(" ");
        int commaIndex = unclean.trim().indexOf(",");

        if (commaIndex < spaceIndex && spaceIndex != -1) { //In this case, coordinates is in the pattern is like: "1,1 2,2"
            return unclean.replace(",", ";").replace(" ", ",").replace(";", " ").trim();
        } else if (spaceIndex == -1) { //spaceIndex == -1 means there's only one point and its pattern is like: "1,1"
            return unclean.replace(",", " ").trim();
        } else {
            return unclean.replace(")", "").replace("(", "").trim();
        }
    }

    private Geometry buildGeometryCollection(String coordinates) {
        //Split coordinates

        String tidied = coordinates.substring(1, coordinates.length() - 1);
        tidied = tidied.replaceAll("[\\ ]?,[\\ ]?", ","); //Remove spaces around commas
        String[] partCoordinates = tidied.split("\\),(?=[^\\(])"); //Split whenever there is a ), but not ),(

        Geometry[] geometries = new Geometry[partCoordinates.length];

        for (int i = 0; i < partCoordinates.length; i++) {
            GMLGeometryBuilder partGMLInfo = extract(partCoordinates[i]);
            geometries[i] = partGMLInfo.geometry;
        }
        return GEOMETRY_FACTORY.createGeometryCollection(geometries);
    }

    private Geometry buildMultiPolygon(String coordinates) {
        String trimmed = coordinates.replace(")) ,", ")),");
        String[] multiCoordinates = trimmed.split("\\)\\),");
        Polygon[] polygons = new Polygon[multiCoordinates.length];
        for (int i = 0; i < multiCoordinates.length; i++) {
            polygons[i] = buildPolygon(multiCoordinates[i]);
        }

        return GEOMETRY_FACTORY.createMultiPolygon(polygons);
    }

    private Polygon buildPolygon(String coordinates) {

        Polygon polygon;

        String[] splitCoordinates = splitCoordinates(coordinates);
        if (splitCoordinates.length == 1) { //Polygon without holes.
            CustomCoordinateSequence shellSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
            polygon = GEOMETRY_FACTORY.createPolygon(shellSequence);
        } else {    //Polygon with holes
            String shellCoordinates = splitCoordinates[0];

            CustomCoordinateSequence shellSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(shellCoordinates));
            LinearRing shellLinearRing = GEOMETRY_FACTORY.createLinearRing(shellSequence);

            String[] splitHoleCoordinates = Arrays.copyOfRange(splitCoordinates, 1, splitCoordinates.length);
            LinearRing[] holesLinearRing = splitLinearRings(splitHoleCoordinates);

            polygon = GEOMETRY_FACTORY.createPolygon(shellLinearRing, holesLinearRing);

        }
        return polygon;
    }

    private String[] splitCoordinates(String coordinates) {

        String trimmed = coordinates.replace(") ,", "),");
        return trimmed.split("\\),");

    }

    private LineString[] splitLineStrings(String[] splitCoordinates) {

        LineString[] lineStrings = new LineString[splitCoordinates.length];

        for (int i = 0; i < splitCoordinates.length; i++) {
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(splitCoordinates[i]));
            LineString lineString = GEOMETRY_FACTORY.createLineString(sequence);
            lineStrings[i] = lineString;
        }

        return lineStrings;

    }

    private LinearRing[] splitLinearRings(String[] splitCoordinates) {

        LinearRing[] linearRings = new LinearRing[splitCoordinates.length];

        for (int i = 0; i < splitCoordinates.length; i++) {
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(splitCoordinates[i]));
            LinearRing linearRing = GEOMETRY_FACTORY.createLinearRing(sequence);
            linearRings[i] = linearRing;
        }

        return linearRings;

    }

    public static GMLGeometryBuilder extract(String gmlText) {

        XMLDocument xmlDoc = new XMLDocument(gmlText);
        NamedNodeMap attributes = xmlDoc.node().getFirstChild().getAttributes();
        srsName = attributes.getNamedItem("srsName").getNodeValue();

        Node dimensionNode = attributes.getNamedItem("srsDimension");
        if (dimensionNode != null) {
            String nodeValue = dimensionNode.getNodeValue();
            srsDimension = Integer.parseInt(nodeValue);

        } else {  //srsDimension attribute is missing so extract from the srsURI.

            CoordinateReferenceSystem crs = CRSRegistry.getCRS(srsName);
            srsDimension = crs.getCoordinateSystem().getDimension();
        }
        String shape = xmlDoc.node().getChildNodes().item(0).getLocalName();
        String coordinates = xmlDoc.node().getChildNodes().item(0).getTextContent();
        return new GMLGeometryBuilder(shape, srsDimension, coordinates);
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
