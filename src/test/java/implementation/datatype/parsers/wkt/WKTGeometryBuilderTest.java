/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.wkt;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import implementation.DimensionInfo;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import implementation.jts.CustomCoordinateSequenceFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Greg
 */
public class WKTGeometryBuilderTest {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    public WKTGeometryBuilderTest() {
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

    /**
     * Test of getDimensionInfo method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetDimensionInfo0() {
        System.out.println("getDimensionInfo0");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "", "");
        DimensionInfo expResult = new DimensionInfo(2, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetDimensionInfo2() {
        System.out.println("getDimensionInfo");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "", "(11.0 12.0)");
        DimensionInfo expResult = new DimensionInfo(2, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetDimensionInfo3a() {
        System.out.println("getDimensionInfo3a");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "z", "(11.0 12.0 7.0)");
        DimensionInfo expResult = new DimensionInfo(3, 3, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetDimensionInfo3b() {
        System.out.println("getDimensionInfo3b");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "m", "(11.0 12.0 7.0)");
        DimensionInfo expResult = new DimensionInfo(3, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetDimensionInfo4() {
        System.out.println("getDimensionInfo4");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "zm", "(11.0 12.0 7.0 5.0)");
        DimensionInfo expResult = new DimensionInfo(4, 3, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeometry method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetGeometryPoint() {
        System.out.println("getGeometryPoint");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "", "(11.0 12.0)");
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "11.0 12.0"));
        Geometry result = instance.getGeometry();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeometry method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetGeometryPointZ() {
        System.out.println("getGeometryPointZ");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "z", "(11.0 12.0 8.0)");
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CoordinateSequenceDimensions.XYZ, "11.0 12.0 8.0"));
        Geometry result = instance.getGeometry();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSpatialDimension method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetSpatialDimension() {
        System.out.println("getSpatialDimension");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "", "(11.0 12.0)");
        int expResult = 2;
        int result = instance.getSpatialDimension();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCoordinateDimension method, of class WKTGeometryBuilder.
     */
    @Test
    public void testGetCoordinateDimension() {
        System.out.println("getCoordinateDimension");
        WKTGeometryBuilder instance = new WKTGeometryBuilder("point", "", "(11.0 12.0)");
        int expResult = 2;
        int result = instance.getCoordinateDimension();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractPoint2() {
        System.out.println("extractPoint2");
        String wktText = "POINT (11.0 12.0)";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("point", "", "(11.0 12.0)");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractPoint3() {
        System.out.println("extractPoint3");
        String wktText = "POINT Z (11.0 12.0 8.0)";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("point", "z", "(11.0 12.0 8.0)");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractPoint3b() {
        System.out.println("extractPoint3b");
        String wktText = "POINT M (11.0 12.0 5.0)";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("point", "m", "(11.0 12.0 5.0)");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractPoint4() {
        System.out.println("extractPoint4");
        String wktText = "POINT ZM (11.0 12.0 8.0 5.0)";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("point", "zm", "(11.0 12.0 8.0 5.0)");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractPolygon() {
        System.out.println("extractPolygon");
        String wktText = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("polygon", "", "(30 10, 40 40, 20 40, 10 20, 30 10)");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractPolygonHole() {
        System.out.println("extractPolygonHole");
        String wktText = "POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35 35, 30 20, 20 30))";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("polygon", "", "(35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35 35, 30 20, 20 30)");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractLineString() {
        System.out.println("extractLineString");
        String wktText = "LINESTRING (30 10, 10 30, 40 40)";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("linestring", "", "(30 10, 10 30, 40 40)");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractMultiPoint() {
        System.out.println("extractMultiPoint");
        String wktText = "MULTIPOINT ((10 40), (40 30), (20 20), (30 10))";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("multipoint", "", "((10 40), (40 30), (20 20), (30 10))");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractMultiPoint2() {
        System.out.println("extractMultiPoint2");
        String wktText = "MULTIPOINT (10 40, 40 30, 20 20, 30 10)";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("multipoint", "", "(10 40, 40 30, 20 20, 30 10)");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractMutliLineString() {
        System.out.println("extractMultiLineString");
        String wktText = "MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("multilinestring", "", "((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractMultiPolygon() {
        System.out.println("extractMultiPolygon");
        String wktText = "MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("multipolygon", "", "(((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractMultiPolygon2() {
        System.out.println("extractMultiPolygon2");
        String wktText = "MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)),((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),(30 20, 20 15, 20 25, 30 20)))";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("multipolygon", "", "(((40 40, 20 45, 45 30, 40 40)),((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),(30 20, 20 15, 20 25, 30 20)))");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTGeometryBuilder.
     */
    @Test
    public void testExtractGeometryCollection() {
        System.out.println("extractGeometryCollection");
        String wktText = "GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10), MULTIPOINT((6 8),(2 3)))";
        WKTGeometryBuilder expResult = new WKTGeometryBuilder("geometrycollection", "", "(POINT(4 6),LINESTRING(4 6,7 10), MULTIPOINT((6 8),(2 3)))");
        WKTGeometryBuilder result = WKTGeometryBuilder.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildPointEmpty method, of class WKTGeometryBuilder.
     */
    @Test
    public void testBuildPointEmpty() {
        System.out.println("buildPointEmpty");
        WKTGeometryBuilder instance = WKTGeometryBuilder.extract("POINT EMPTY");
        Geometry result = instance.getGeometry();

        CustomCoordinateSequence pointSequence = new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "");
        Geometry expResult = GEOMETRY_FACTORY.createPoint(pointSequence);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildLineStringEmpty method, of class WKTGeometryBuilder.
     */
    @Test
    public void testBuildLineStringEmpty() {
        System.out.println("buildLineEmpty");
        WKTGeometryBuilder instance = WKTGeometryBuilder.extract("LINESTRING EMPTY");
        Geometry result = instance.getGeometry();

        CustomCoordinateSequence pointSequence = new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "");
        Geometry expResult = GEOMETRY_FACTORY.createLineString(pointSequence);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildPolygonEmpty method, of class WKTGeometryBuilder.
     */
    @Test
    public void testBuildPolygonEmpty() {
        System.out.println("buildPolygonEmpty");
        WKTGeometryBuilder instance = WKTGeometryBuilder.extract("POLYGON EMPTY");
        Geometry result = instance.getGeometry();

        CustomCoordinateSequence pointSequence = new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "");
        Geometry expResult = GEOMETRY_FACTORY.createPolygon(pointSequence);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildMultiPointEmpty method, of class WKTGeometryBuilder.
     */
    @Test
    public void testBuildMultiPointEmpty() {
        System.out.println("buildMultiPointEmpty");
        WKTGeometryBuilder instance = WKTGeometryBuilder.extract("MULTIPOINT EMPTY");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createMultiPoint(new Point[0]);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildMultiLineString method, of class WKTGeometryBuilder.
     */
    @Test
    public void testBuildMultiLineStringEmpty() {
        System.out.println("buildMultiLineStringEmpty");
        WKTGeometryBuilder instance = WKTGeometryBuilder.extract("MULTILINESTRING EMPTY");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createMultiLineString(new LineString[0]);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildMultiPolygonEmpty method, of class WKTGeometryBuilder.
     */
    @Test
    public void testBuildMultiPolygonEmpty() {
        System.out.println("buildMultiPolygonEmpty");
        WKTGeometryBuilder instance = WKTGeometryBuilder.extract("MULTIPOLYGON EMPTY");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createMultiPolygon(new Polygon[0]);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildGeometryCollectionEmpty method, of class WKTGeometryBuilder.
     */
    @Test
    public void testBuildGeometryCollectionEmpty() {
        System.out.println("buildGeometryCollectionEmpty");
        WKTGeometryBuilder instance = WKTGeometryBuilder.extract("GEOMETRYCOLLECTION EMPTY");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createGeometryCollection(new Geometry[0]);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

}
