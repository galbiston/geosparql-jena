/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package implementation.jts;

import implementation.datatype.ParseException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GeometryTransformation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final Geometry transform(Geometry sourceGeometry, MathTransform transform) throws TransformException {

        String geometryType = sourceGeometry.getGeometryType().toLowerCase();
        switch (geometryType) {
            case "point":
                return transformPoint(sourceGeometry, transform);
            case "linestring":
                return transformLineString(sourceGeometry, transform);
            case "linearring":
                return transformLinearRing(sourceGeometry, transform);
            case "polygon":
                return transformPolygon(sourceGeometry, transform);
            case "multipoint":
                return transformMultiPoint(sourceGeometry, transform);
            case "multilinestring":
                return transformMultiLineString(sourceGeometry, transform);
            case "multipolygon":
                return transformMultiPolygon(sourceGeometry, transform);
            case "geometrycollection":
                return transformGeometryCollection(sourceGeometry, transform);
            default:
                throw new ParseException("Geometry type not supported: " + geometryType);
        }
    }

    private static Point transformPoint(Geometry sourceGeometry, MathTransform transform) throws TransformException {
        GeometryFactory geometryFactory = sourceGeometry.getFactory();

        Point point = (Point) sourceGeometry;
        CoordinateSequence coordSeq = point.getCoordinateSequence();
        CoordinateSequence transformCoordSeq = transformCoordSeq(coordSeq, transform);

        return geometryFactory.createPoint(transformCoordSeq);
    }

    private static LineString transformLineString(Geometry sourceGeometry, MathTransform transform) throws TransformException {
        GeometryFactory geometryFactory = sourceGeometry.getFactory();

        LineString lineString = (LineString) sourceGeometry;
        CoordinateSequence coordSeq = lineString.getCoordinateSequence();
        CoordinateSequence transformCoordSeq = transformCoordSeq(coordSeq, transform);

        return geometryFactory.createLineString(transformCoordSeq);
    }

    private static LinearRing transformLinearRing(Geometry sourceGeometry, MathTransform transform) throws TransformException {
        GeometryFactory geometryFactory = sourceGeometry.getFactory();

        LinearRing linearRing = (LinearRing) sourceGeometry;
        CoordinateSequence coordSeq = linearRing.getCoordinateSequence();
        CoordinateSequence transformCoordSeq = transformCoordSeq(coordSeq, transform);

        return geometryFactory.createLinearRing(transformCoordSeq);
    }

    private static Polygon transformPolygon(Geometry sourceGeometry, MathTransform transform) throws TransformException {
        GeometryFactory geometryFactory = sourceGeometry.getFactory();

        Polygon polygon = (Polygon) sourceGeometry;
        LinearRing exterior = transformLinearRing(polygon.getExteriorRing(), transform);

        int interiorsNumber = polygon.getNumInteriorRing();
        ArrayList<LinearRing> interiors = new ArrayList<>(interiorsNumber);
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            LinearRing interior = transformLinearRing(polygon.getInteriorRingN(i), transform);
            interiors.add(interior);
        }

        return geometryFactory.createPolygon(exterior, interiors.toArray(new LinearRing[interiors.size()]));
    }

    private static MultiPoint transformMultiPoint(Geometry sourceGeometry, MathTransform transform) throws TransformException {
        GeometryFactory geometryFactory = sourceGeometry.getFactory();

        MultiPoint multiPoint = (MultiPoint) sourceGeometry;

        int geometryNumber = multiPoint.getNumGeometries();
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < geometryNumber; i++) {
            Point point = transformPoint(multiPoint.getGeometryN(i), transform);
            points.add(point);
        }

        return geometryFactory.createMultiPoint(points.toArray(new Point[points.size()]));
    }

    private static MultiLineString transformMultiLineString(Geometry sourceGeometry, MathTransform transform) throws TransformException {
        GeometryFactory geometryFactory = sourceGeometry.getFactory();

        MultiLineString multiLineString = (MultiLineString) sourceGeometry;

        int geometryNumber = multiLineString.getNumGeometries();
        ArrayList<LineString> lineStrings = new ArrayList<>();
        for (int i = 0; i < geometryNumber; i++) {
            LineString lineString = transformLineString(multiLineString.getGeometryN(i), transform);
            lineStrings.add(lineString);
        }

        return geometryFactory.createMultiLineString(lineStrings.toArray(new LineString[lineStrings.size()]));
    }

    private static MultiPolygon transformMultiPolygon(Geometry sourceGeometry, MathTransform transform) throws TransformException {
        GeometryFactory geometryFactory = sourceGeometry.getFactory();

        MultiPolygon multiPolygon = (MultiPolygon) sourceGeometry;

        int geometryNumber = multiPolygon.getNumGeometries();
        ArrayList<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < geometryNumber; i++) {
            Polygon polygon = transformPolygon(multiPolygon.getGeometryN(i), transform);
            polygons.add(polygon);
        }

        return geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
    }

    private static GeometryCollection transformGeometryCollection(Geometry sourceGeometry, MathTransform transform) throws TransformException {
        GeometryFactory geometryFactory = sourceGeometry.getFactory();

        GeometryCollection geometryCollection = (GeometryCollection) sourceGeometry;

        int geometryNumber = geometryCollection.getNumGeometries();
        ArrayList<Geometry> geometries = new ArrayList<>();
        for (int i = 0; i < geometryNumber; i++) {
            Geometry geometry = transform(geometryCollection.getGeometryN(i), transform);
            geometries.add(geometry);
        }

        return geometryFactory.createGeometryCollection(geometries.toArray(new Geometry[geometries.size()]));
    }

    private static CoordinateSequence transformCoordSeq(CoordinateSequence coordSeq, MathTransform transform) throws TransformException {

            int size = coordSeq.size();
            int sourceDims = transform.getSourceDimensions();
            int targetDims = transform.getTargetDimensions();

            double[] sourcePts = new double[size * sourceDims];
            double[] targetPts = new double[size * targetDims];

            //Setup source array for transform.
            boolean isZSource = sourceDims > 2;
            for (int i = 0; i < size; i += sourceDims) {
                Coordinate coord = coordSeq.getCoordinate(i);
                sourcePts[i] = coord.getX();
                sourcePts[i + 1] = coord.getY();
                if (isZSource) {
                    sourcePts[i + 2] = coord.getZ();
                }
            }

            //Transform the ordinates.
            transform.transform(sourcePts, 0, targetPts, 0, size);

            //Extract into coordiante sequence.
            double[] x = new double[size];
            double[] y = new double[size];
            double[] z = new double[size];
            double[] m = new double[size];

            boolean isZTransformed = sourceDims > 2 && targetDims > 2;
            for (int i = 0; i < targetPts.length; i += targetDims) {
                Coordinate coord = coordSeq.getCoordinate(i);

                x[i] = targetPts[i];
                y[i] = targetPts[i + 1];
                if (isZTransformed) {
                    z[i] = targetPts[i + 2];
                } else {
                    if (coordSeq.hasZ()) {
                        z[i] = coord.getZ();
                    } else {
                        z[i] = Double.NaN;
                    }
                }
                if (coordSeq.hasM()) {
                    m[i] = coord.getM();
                } else {
                    m[i] = Double.NaN;
                }

            }

        return new CustomCoordinateSequence(x, y, z, m);
    }


}
