/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.gml;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequenceFactory;
import implementation.support.GeoSerialisationEnum;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * 
 */
public class GMLWriterTest {

    public final String GML_SRS_NAMESPACE = "urn:ogc:def:crs:EPSG::27700";

    public GMLWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    @Test
    public void testPoint() {
        System.out.println("writePoint");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "11.0 12.1"));
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, GML_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        String expResult = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:coordinates>11 12.1</gml:coordinates></gml:Point>";

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult.trim(), result.trim());

    }

    @Test
    public void testLineString() {
        System.out.println("writeLineString");
        Geometry geometry = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "11.0 12.1, 15.0 8.0"));
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, GML_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        String expResult = "<gml:LineString xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:coordinates>11 12.1 15 8</gml:coordinates></gml:LineString>";

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult.trim(), result.trim());

    }

    @Test
    public void testPolygon() {
        System.out.println("writePolygon");
        Geometry geometry = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 10, 40 40, 20 40, 10 20, 30 10"));
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, GML_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        String expResult = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:Exterior><gml:PosList>30 10 40 40 20 40 10 20 30 10</gml:PosList></gml:Exterior></gml:Polygon>";

        System.out.println("Expected: " + expResult);
        System.out.println("  Result: " + result);
        assertEquals(expResult.trim(), result.trim());
    }

    @Test
    public void testPolygon2() {
        System.out.println("writePolygon2");
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 10, 40 40, 20 40, 10 20, 30 10"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "20 30, 35 35, 30 20, 20 30"))};
        Geometry geometry = GEOMETRY_FACTORY.createPolygon(shell, holes);
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, GML_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        String expResult = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:Exterior><gml:PosList>30 10 40 40 20 40 10 20 30 10</gml:PosList></gml:Exterior><gml:Interior><gml:PosList>20 30 35 35 30 20 20 30</gml:PosList></gml:Interior></gml:Polygon>";

        System.out.println("Expected: " + expResult);
        System.out.println("  Result: " + result);
        assertEquals(expResult.trim(), result.trim());
    }

    @Test
    public void testMultiPoint() {
        System.out.println("writeMultiPoint");
        Geometry geometry = GEOMETRY_FACTORY.createMultiPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "10 40, 40 30, 20 20, 30 10"));
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, GML_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        String expResult = "<gml:MultiPoint xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:PointMember><gml:Point>10 40</gml:Point></gml:PointMember><gml:PointMember><gml:Point>40 30</gml:Point></gml:PointMember><gml:PointMember><gml:Point>20 20</gml:Point></gml:PointMember><gml:PointMember><gml:Point>30 10</gml:Point></gml:PointMember></gml:MultiPoint>";

        System.out.println("Expected: " + expResult);
        System.out.println("  Result: " + result);
        assertEquals(expResult.trim(), result.trim());
    }

    @Test
    public void testMultiLineString() {
        System.out.println("writeMultiLineString");
        LineString[] lineStrings = new LineString[2];
        lineStrings[0] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "10 10, 20 20, 10 40"));
        lineStrings[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "40 40, 30 30, 40 20, 30 10"));
        Geometry geometry = GEOMETRY_FACTORY.createMultiLineString(lineStrings);
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, GML_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        String expResult = "<gml:MultiLineString xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:LineStringMember><gml:LineString>10 10 20 20 10 40</gml:LineString></gml:LineStringMember><gml:LineStringMember><gml:LineString>40 40 30 30 40 20 30 10</gml:LineString></gml:LineStringMember></gml:MultiLineString>";

        System.out.println("Expected: " + expResult);
        System.out.println("  Result: " + result);
        assertEquals(expResult.trim(), result.trim());
    }

    @Test
    public void testMultiPolygon() {
        System.out.println("writeMultiPolygon");
        Polygon[] polygons = new Polygon[2];
        polygons[0] = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "40 40, 20 45, 45 30, 40 40"));
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "20 35, 10 30, 10 10, 30 5, 45 20, 20 35"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 20, 20 15, 20 25, 30 20"))};
        polygons[1] = GEOMETRY_FACTORY.createPolygon(shell, holes);
        Geometry geometry = GEOMETRY_FACTORY.createMultiPolygon(polygons);
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, GML_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        String expResult = "<gml:MultiPolygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:PolygonMember><gml:Polygon srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:Exterior><gml:PosList>40 40 20 45 45 30 40 40</gml:PosList></gml:Exterior></gml:Polygon></gml:PolygonMember><gml:PolygonMember><gml:Polygon srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:Exterior><gml:PosList>20 35 10 30 10 10 30 5 45 20 20 35</gml:PosList></gml:Exterior><gml:Interior><gml:PosList>30 20 20 15 20 25 30 20</gml:PosList></gml:Interior></gml:Polygon></gml:PolygonMember></gml:MultiPolygon>";

        System.out.println("Expected: " + expResult);
        System.out.println("  Result: " + result);
        assertEquals(expResult.trim(), result.trim());
    }

    @Test
    public void testGeometryCollection() {
        System.out.println("writeGeometryCollection");
        Geometry[] geometries = new Geometry[2];
        geometries[0] = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "4 6"));
        geometries[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "4 6,7 10"));
        Geometry geometry = GEOMETRY_FACTORY.createGeometryCollection(geometries);
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, GML_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        String expResult = "<gml:GeometryCollection xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:GeometryMember><gml:Point srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:coordinates>4 6</gml:coordinates></gml:Point></gml:GeometryMember><gml:GeometryMember><gml:LineString srsName=\"urn:ogc:def:crs:EPSG::27700\" srsDimension=\"2\"><gml:coordinates>4 6 7 10</gml:coordinates></gml:LineString></gml:GeometryMember></gml:GeometryCollection>";

        System.out.println("Expected: " + expResult);
        System.out.println("  Result: " + result);
        assertEquals(expResult.trim(), result.trim());
    }
}
