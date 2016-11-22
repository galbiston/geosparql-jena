/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.support.GeoSerialisationEnum;
import static org.hamcrest.CoreMatchers.not;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class WKTDatatypeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKTDatatypeTest.class);

    public WKTDatatypeTest() {
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
     * Test of unparse method, of class WKTDatatype.
     */
    @Test
    public void testUnparse() {
        System.out.println("unparse");

        String expResult = "<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT (-83.38 33.95)";

        WKTDatatype instance = WKTDatatype.THE_WKT_DATATYPE;

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point point = geometryFactory.createPoint(coord);
        String srsURI = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 0);

        GeometryWrapper geometry = new GeometryWrapper(point, srsURI, GeoSerialisationEnum.WKT, dimensionInfo);

        String result = instance.unparse(geometry);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);

        assertEquals(expResult, result);

    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseNoSRS() {
        System.out.println("parseNoSRS");
        String lexicalForm = "POINT(-83.38 33.95)";
        WKTDatatype instance = WKTDatatype.THE_WKT_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);
        String expSRSURI = WKTDatatype.DEFAULT_WKT_CRS_URI;

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 0);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT, dimensionInfo);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseNoSRSNotEqual() {
        System.out.println("parseNoSRSNotEqual");
        String lexicalForm = "POINT(-83.38 33.95)";
        WKTDatatype instance = WKTDatatype.THE_WKT_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = WKTDatatype.DEFAULT_WKT_CRS_URI;

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 0);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT, dimensionInfo);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseNoSRSNotEqual2() {
        System.out.println("parseNoSRSNotEqual2");
        String lexicalForm = "POINT(-83.38 33.95)";
        WKTDatatype instance = WKTDatatype.THE_WKT_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = "http://www.opengis.net/def/crs/EPSG/0/4326";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 0);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT, dimensionInfo);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseSRS() {
        System.out.println("parseSRS");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(33.95 -88.38)";
        WKTDatatype instance = WKTDatatype.THE_WKT_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = "http://www.opengis.net/def/crs/EPSG/0/4326";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 0);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT, dimensionInfo);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseSRSNotEqual() {
        System.out.println("parseSRSNotEqual");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(33.95 -88.38)";
        WKTDatatype instance = WKTDatatype.THE_WKT_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = "http://www.opengis.net/def/crs/EPSG/0/4326";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 0);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT, dimensionInfo);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseSRSNotEqual2() {
        System.out.println("parseSRSNotEqual2");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(33.95 -88.38)";
        WKTDatatype instance = WKTDatatype.THE_WKT_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = WKTDatatype.DEFAULT_WKT_CRS_URI;

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 0);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT, dimensionInfo);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of findCoordinateDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindCoordinateDimension0() throws ParseException {
        System.out.println("findCoordinateDimension0");
        String lexicalForm = "POINT EMPTY";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 0;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findCoordinateDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindCoordinateDimension2() throws ParseException {
        System.out.println("findCoordinateDimension2");
        String lexicalForm = "POINT (1 1)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 2;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findCoordinateDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindCoordinateDimension2b() throws ParseException {
        System.out.println("findCoordinateDimension2b");
        String lexicalForm = "POINT(1 1)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 2;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findCoordinateDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindCoordinateDimension2c() throws ParseException {
        System.out.println("findCoordinateDimension2c");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(1 1)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read("POINT(1 1)");

        int expResult = 2;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findCoordinateDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindCoordinateDimension3() throws ParseException {
        System.out.println("findCoordinateDimension3");
        String lexicalForm = "POINT Z (1 1 5)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 3;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findCoordinateDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindCoordinateDimension3b() throws ParseException {
        System.out.println("findCoordinateDimension3b");
        String lexicalForm = "POINT M (1 1 60)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 3;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findCoordinateDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindCoordinateDimension4() throws ParseException {
        System.out.println("findCoordinateDimension4");
        String lexicalForm = "POINT ZM (1 1 5 60)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 4;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findSpatialDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindSpatialDimension2() throws ParseException {
        System.out.println("findSpatialDimension2");
        String lexicalForm = "POINT (1 1)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 2;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getSpatial());
    }

    /**
     * Test of findSpatialDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindSpatialDimension2b() throws ParseException {
        System.out.println("findSpatialDimension2b");
        String lexicalForm = "POINT(1 1)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 2;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getSpatial());
    }

    /**
     * Test of findSpatialDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindSpatialDimension2c() throws ParseException {
        System.out.println("findSpatialDimension2c");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(1 1)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read("POINT(1 1)");

        int expResult = 2;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getSpatial());
    }

    /**
     * Test of findSpatialDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindSpatialDimension0() throws ParseException {
        System.out.println("findSpatialDimension0");
        String lexicalForm = "POINT EMPTY";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 0;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getSpatial());
    }

    /**
     * Test of findSpatialDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindSpatialDimension3() throws ParseException {
        System.out.println("findSpatialDimension3");
        String lexicalForm = "POINT Z (1 1 5)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 3;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getSpatial());
    }

    /**
     * Test of findSpatialDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindSpatialDimension3b() throws ParseException {
        System.out.println("findSpatialDimension3b");
        String lexicalForm = "POINT M (1 1 60)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 2;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getSpatial());
    }

    /**
     * Test of findSpatialDimensions method, of class WKTDatatype.
     *
     * @throws com.vividsolutions.jts.io.ParseException
     */
    @Test
    public void testFindSpatialDimension4() throws ParseException {
        System.out.println("findSpatialDimension4");
        String lexicalForm = "POINT ZM (1 1 5 60)";
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(lexicalForm);

        int expResult = 3;
        DimensionInfo result = WKTDatatype.extractDimensionInfo(lexicalForm, geometry);
        assertEquals(expResult, result.getSpatial());
    }
}
