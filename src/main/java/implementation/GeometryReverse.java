/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author Greg
 */
public class GeometryReverse {

    /**
     * Checks the CRS for y,x and reverses the supplied geometry coordinates.
     *
     * @param geometry
     * @param crs
     * @return
     */
    public static final Geometry check(Geometry geometry, CoordinateReferenceSystem crs) {

        Geometry finalGeometry;
        if (CRS.getAxisOrder(crs).equals(CRS.AxisOrder.NORTH_EAST)) {
            finalGeometry = reverseGeometry(geometry);
        } else {
            finalGeometry = geometry;
        }
        return finalGeometry;
    }

    /**
     * Reverses coordinate order of the supplied geometry and produces a new
     * geometry.
     *
     * @param geometry
     * @return
     */
    private static Geometry reverseGeometry(Geometry geometry) {

        GeometryFactory factory = geometry.getFactory();
        Geometry finalGeometry;
        Coordinate[] coordinates;

        String type = geometry.getGeometryType();

        switch (type) {
            case "LineString":
                coordinates = getReversedCoordinates(geometry);
                finalGeometry = factory.createLineString(coordinates);
                break;
            case "LinearRing":
                coordinates = getReversedCoordinates(geometry);
                finalGeometry = factory.createLinearRing(coordinates);
                break;
            case "MultiPoint":
                coordinates = getReversedCoordinates(geometry);
                finalGeometry = factory.createMultiPoint(coordinates);
                break;
            case "Polygon":
                coordinates = getReversedCoordinates(geometry);
                finalGeometry = factory.createPolygon(coordinates);
                break;
            case "Point":
                coordinates = getReversedCoordinates(geometry);
                finalGeometry = factory.createPoint(coordinates[0]);
                break;
            case "MultiPolygon":
                Polygon[] polygons = unpackPolygons((GeometryCollection) geometry);
                finalGeometry = factory.createMultiPolygon(polygons);
                break;
            case "MultiLineString":
                LineString[] lineString = unpackLineString((GeometryCollection) geometry);
                finalGeometry = factory.createMultiLineString(lineString);
                break;
            case "GeometryCollection":
                Geometry[] geometries = unpackGeometryCollection((GeometryCollection) geometry);
                finalGeometry = factory.createGeometryCollection(geometries);
                break;
            default:
                finalGeometry = geometry;
                break;
        }

        return finalGeometry;
    }

    private static Coordinate[] getReversedCoordinates(Geometry geometry) {

        Coordinate[] original = geometry.getCoordinates();
        Coordinate[] reversed = new Coordinate[original.length];

        for (int i = 0; i < original.length; i++) {
            reversed[i] = new Coordinate(original[i].y, original[i].x);
        }

        return reversed;

    }

    private static Polygon[] unpackPolygons(GeometryCollection geoCollection) {

        GeometryFactory factory = geoCollection.getFactory();

        int count = geoCollection.getNumGeometries();
        Polygon[] polygons = new Polygon[count];

        for (int i = 0; i < count; i++) {
            Geometry geometry = geoCollection.getGeometryN(i);
            Coordinate[] coordinates = getReversedCoordinates(geometry);
            Polygon polygon = factory.createPolygon(coordinates);
            polygons[i] = polygon;
        }

        return polygons;
    }

    private static LineString[] unpackLineString(GeometryCollection geoCollection) {

        GeometryFactory factory = geoCollection.getFactory();

        int count = geoCollection.getNumGeometries();
        LineString[] lineStrings = new LineString[count];

        for (int i = 0; i < count; i++) {
            Geometry geometry = geoCollection.getGeometryN(i);
            Coordinate[] coordinates = getReversedCoordinates(geometry);
            LineString lineString = factory.createLineString(coordinates);
            lineStrings[i] = lineString;
        }

        return lineStrings;
    }

    private static Geometry[] unpackGeometryCollection(GeometryCollection geoCollection) {

        int count = geoCollection.getNumGeometries();
        Geometry[] geometries = new Geometry[count];

        for (int i = 0; i < count; i++) {
            Geometry geometry = geoCollection.getGeometryN(i);
            geometries[i] = reverseGeometry(geometry);
        }

        return geometries;
    }

}
