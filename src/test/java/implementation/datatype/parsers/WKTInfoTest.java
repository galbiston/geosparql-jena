/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import implementation.DimensionInfo;
import implementation.datatype.parsers.CustomCoordinateSequence.Dimensions;
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
public class WKTInfoTest {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    public WKTInfoTest() {
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
     * Test of getDimensionInfo method, of class WKTInfo.
     */
    @Test
    public void testGetDimensionInfo0() {
        System.out.println("getDimensionInfo0");
        WKTInfo instance = new WKTInfo("point", "", "");
        DimensionInfo expResult = new DimensionInfo(2, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class WKTInfo.
     */
    @Test
    public void testGetDimensionInfo2() {
        System.out.println("getDimensionInfo");
        WKTInfo instance = new WKTInfo("point", "", "(11.0 12.0)");
        DimensionInfo expResult = new DimensionInfo(2, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class WKTInfo.
     */
    @Test
    public void testGetDimensionInfo3a() {
        System.out.println("getDimensionInfo3a");
        WKTInfo instance = new WKTInfo("point", "z", "(11.0 12.0 7.0)");
        DimensionInfo expResult = new DimensionInfo(3, 3, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class WKTInfo.
     */
    @Test
    public void testGetDimensionInfo3b() {
        System.out.println("getDimensionInfo3b");
        WKTInfo instance = new WKTInfo("point", "m", "(11.0 12.0 7.0)");
        DimensionInfo expResult = new DimensionInfo(3, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class WKTInfo.
     */
    @Test
    public void testGetDimensionInfo4() {
        System.out.println("getDimensionInfo4");
        WKTInfo instance = new WKTInfo("point", "zm", "(11.0 12.0 7.0 5.0)");
        DimensionInfo expResult = new DimensionInfo(4, 3, 0);
        DimensionInfo result = instance.getDimensionInfo();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeometry method, of class WKTInfo.
     */
    @Test
    public void testGetGeometryPoint() {
        System.out.println("getGeometryPoint");
        WKTInfo instance = new WKTInfo("point", "", "(11.0 12.0)");
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(Dimensions.XY, "11.0 12.0"));
        Geometry result = instance.getGeometry();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeometry method, of class WKTInfo.
     */
    @Test
    public void testGetGeometryPointZ() {
        System.out.println("getGeometryPointZ");
        WKTInfo instance = new WKTInfo("point", "z", "(11.0 12.0 8.0)");
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(Dimensions.XYZ, "11.0 12.0 8.0"));
        Geometry result = instance.getGeometry();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSpatialDimension method, of class WKTInfo.
     */
    @Test
    public void testGetSpatialDimension() {
        System.out.println("getSpatialDimension");
        WKTInfo instance = new WKTInfo("point", "", "(11.0 12.0)");
        int expResult = 2;
        int result = instance.getSpatialDimension();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCoordinateDimension method, of class WKTInfo.
     */
    @Test
    public void testGetCoordinateDimension() {
        System.out.println("getCoordinateDimension");
        WKTInfo instance = new WKTInfo("point", "", "(11.0 12.0)");
        int expResult = 2;
        int result = instance.getCoordinateDimension();

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractPoint2() {
        System.out.println("extractPoint2");
        String wktText = "POINT (11.0 12.0)";
        WKTInfo expResult = new WKTInfo("point", "", "(11.0 12.0)");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractPoint3() {
        System.out.println("extractPoint3");
        String wktText = "POINT Z (11.0 12.0 8.0)";
        WKTInfo expResult = new WKTInfo("point", "z", "(11.0 12.0 8.0)");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractPoint3b() {
        System.out.println("extractPoint3b");
        String wktText = "POINT M (11.0 12.0 5.0)";
        WKTInfo expResult = new WKTInfo("point", "m", "(11.0 12.0 5.0)");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractPoint4() {
        System.out.println("extractPoint4");
        String wktText = "POINT ZM (11.0 12.0 8.0 5.0)";
        WKTInfo expResult = new WKTInfo("point", "zm", "(11.0 12.0 8.0 5.0)");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractPolygon() {
        System.out.println("extractPolygon");
        String wktText = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
        WKTInfo expResult = new WKTInfo("polygon", "", "(30 10, 40 40, 20 40, 10 20, 30 10)");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractPolygonHole() {
        System.out.println("extractPolygonHole");
        String wktText = "POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35 35, 30 20, 20 30))";
        WKTInfo expResult = new WKTInfo("polygon", "", "(35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35 35, 30 20, 20 30)");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractLineString() {
        System.out.println("extractLineString");
        String wktText = "LINESTRING (30 10, 10 30, 40 40)";
        WKTInfo expResult = new WKTInfo("linestring", "", "(30 10, 10 30, 40 40)");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractMultiPoint() {
        System.out.println("extractMultiPoint");
        String wktText = "MULTIPOINT ((10 40), (40 30), (20 20), (30 10))";
        WKTInfo expResult = new WKTInfo("multipoint", "", "((10 40), (40 30), (20 20), (30 10))");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractMultiPoint2() {
        System.out.println("extractMultiPoint2");
        String wktText = "MULTIPOINT (10 40, 40 30, 20 20, 30 10)";
        WKTInfo expResult = new WKTInfo("multipoint", "", "(10 40, 40 30, 20 20, 30 10)");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractMutliLineString() {
        System.out.println("extractMultiLineString");
        String wktText = "MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))";
        WKTInfo expResult = new WKTInfo("multilinestring", "", "((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractMultiPolygon() {
        System.out.println("extractMultiPolygon");
        String wktText = "MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))";
        WKTInfo expResult = new WKTInfo("multipolygon", "", "(((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractMultiPolygon2() {
        System.out.println("extractMultiPolygon2");
        String wktText = "MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)),((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),(30 20, 20 15, 20 25, 30 20)))";
        WKTInfo expResult = new WKTInfo("multipolygon", "", "(((40 40, 20 45, 45 30, 40 40)),((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),(30 20, 20 15, 20 25, 30 20)))");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class WKTInfo.
     */
    @Test
    public void testExtractGeometryCollection() {
        System.out.println("extractGeometryCollection");
        String wktText = "GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10), MULTIPOINT((6 8),(2 3)))";
        WKTInfo expResult = new WKTInfo("geometrycollection", "", "(POINT(4 6),LINESTRING(4 6,7 10), MULTIPOINT((6 8),(2 3)))");
        WKTInfo result = WKTInfo.extract(wktText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }
}
