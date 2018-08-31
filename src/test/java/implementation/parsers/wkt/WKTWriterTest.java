/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.parsers.wkt;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.datatype.WKTDatatype;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomGeometryFactory;
import implementation.vocabulary.SRS_URI;
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
public class WKTWriterTest {

    public WKTWriterTest() {
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

    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    /**
     * Test of write method, of class WKTReader.
     */
    @Test
    public void testWritePoint() {
        System.out.println("writePoint");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "11.0 12.1 8.0 5.0"));

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, SRS_URI.DEFAULT_WKT_CRS84, WKTDatatype.URI, new DimensionInfo(4, 3, 0));

        String expResult = "<" + SRS_URI.DEFAULT_WKT_CRS84 + "> POINT ZM(11 12.1 8 5)";
        String result = WKTWriter.write(geometryWrapper);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTReader.
     */
    @Test
    public void testWriteLineString() {
        System.out.println("writeLineString");
        Geometry geometry = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "11.0 12.1 8.0 5.0, 3 4 6 2"));

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, SRS_URI.DEFAULT_WKT_CRS84, WKTDatatype.URI, new DimensionInfo(4, 3, 0));

        String expResult = "<" + SRS_URI.DEFAULT_WKT_CRS84 + "> LINESTRING ZM(11 12.1 8 5, 3 4 6 2)";
        String result = WKTWriter.write(geometryWrapper);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTReader.
     */
    @Test
    public void testWritePolygon() {
        System.out.println("writePolygon");
        Geometry geometry = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "30 10 0 1, 40 40 0 1, 20 40 0 1, 10 20 0 1, 30 10 0 1"));

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, SRS_URI.DEFAULT_WKT_CRS84, WKTDatatype.URI, new DimensionInfo(4, 3, 0));

        String expResult = "<" + SRS_URI.DEFAULT_WKT_CRS84 + "> POLYGON ZM((30 10 0 1, 40 40 0 1, 20 40 0 1, 10 20 0 1, 30 10 0 1))";
        String result = WKTWriter.write(geometryWrapper);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTReader.
     */
    @Test
    public void testWritePolygon2() {
        System.out.println("writePolygon2");
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "30 10 0 1, 40 40 0 1, 20 40 0 1, 10 20 0 1, 30 10 0 1"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "20 30 0 1, 35 35 0 1, 30 20 0 1, 20 30 0 1"))};
        Geometry geometry = GEOMETRY_FACTORY.createPolygon(shell, holes);

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, SRS_URI.DEFAULT_WKT_CRS84, WKTDatatype.URI, new DimensionInfo(4, 3, 0));

        String expResult = "<" + SRS_URI.DEFAULT_WKT_CRS84 + "> POLYGON ZM((30 10 0 1, 40 40 0 1, 20 40 0 1, 10 20 0 1, 30 10 0 1), (20 30 0 1, 35 35 0 1, 30 20 0 1, 20 30 0 1))";
        String result = WKTWriter.write(geometryWrapper);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTReader.
     */
    @Test
    public void testWriteMultiPoint() {
        System.out.println("writeMultiPoint");
        Geometry geometry = GEOMETRY_FACTORY.createMultiPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "10 40 0 1, 40 30 0 1, 20 20 0 1, 30 10 0 1"));

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, SRS_URI.DEFAULT_WKT_CRS84, WKTDatatype.URI, new DimensionInfo(4, 3, 0));

        String expResult = "<" + SRS_URI.DEFAULT_WKT_CRS84 + "> MULTIPOINT ZM((10 40 0 1), (40 30 0 1), (20 20 0 1), (30 10 0 1))";
        String result = WKTWriter.write(geometryWrapper);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTReader.
     */
    @Test
    public void testWriteMultiLineString() {
        System.out.println("writeMultiLineString");

        LineString[] lineStrings = new LineString[2];
        lineStrings[0] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "10 10 0 1, 20 20 0 1, 10 40 0 1"));
        lineStrings[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "40 40 0 1, 30 30 0 1, 40 20 0 1, 30 10 0 1"));
        Geometry geometry = GEOMETRY_FACTORY.createMultiLineString(lineStrings);

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, SRS_URI.DEFAULT_WKT_CRS84, WKTDatatype.URI, new DimensionInfo(4, 3, 0));

        String expResult = "<" + SRS_URI.DEFAULT_WKT_CRS84 + "> MULTILINESTRING ZM((10 10 0 1, 20 20 0 1, 10 40 0 1), (40 40 0 1, 30 30 0 1, 40 20 0 1, 30 10 0 1))";
        String result = WKTWriter.write(geometryWrapper);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTReader.
     */
    @Test
    public void testWriteMultiPolygon() {
        System.out.println("writeMultiPolygon");

        Polygon[] polygons = new Polygon[2];
        polygons[0] = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "40 40 0 1, 20 45 0 1, 45 30 0 1, 40 40 0 1"));
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "20 35 0 1, 10 30 0 1, 10 10 0 1, 30 5 0 1, 45 20 0 1, 20 35 0 1"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "30 20 0 1, 20 15 0 1, 20 25 0 1, 30 20 0 1"))};
        polygons[1] = GEOMETRY_FACTORY.createPolygon(shell, holes);
        Geometry geometry = GEOMETRY_FACTORY.createMultiPolygon(polygons);

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, SRS_URI.DEFAULT_WKT_CRS84, WKTDatatype.URI, new DimensionInfo(4, 3, 0));

        String expResult = "<" + SRS_URI.DEFAULT_WKT_CRS84 + "> MULTIPOLYGON ZM(((40 40 0 1, 20 45 0 1, 45 30 0 1, 40 40 0 1)), ((20 35 0 1, 10 30 0 1, 10 10 0 1, 30 5 0 1, 45 20 0 1, 20 35 0 1), (30 20 0 1, 20 15 0 1, 20 25 0 1, 30 20 0 1)))";
        String result = WKTWriter.write(geometryWrapper);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTReader.
     */
    @Test
    public void testWriteGeometryCollection() {
        System.out.println("writeGeometryCollection");

        Geometry[] geometries = new Geometry[2];
        geometries[0] = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "4 6 0 1"));
        geometries[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "4 6 0 1,7 10 0 1"));
        Geometry geometry = GEOMETRY_FACTORY.createGeometryCollection(geometries);

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, SRS_URI.DEFAULT_WKT_CRS84, WKTDatatype.URI, new DimensionInfo(4, 3, 0));

        String expResult = "<" + SRS_URI.DEFAULT_WKT_CRS84 + "> GEOMETRYCOLLECTION ZM(POINT ZM(4 6 0 1), LINESTRING ZM(4 6 0 1, 7 10 0 1))";
        String result = WKTWriter.write(geometryWrapper);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

}
