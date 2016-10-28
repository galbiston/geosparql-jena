/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
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

        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point geometry = geometryFactory.createPoint(coord);
        GeometryDatatype.setSRSName(geometry, WKTDatatype.DEFAULT_SRS_NAME);

        String result = instance.unparse(geometry);

        System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result);

    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseNoSRS() {
        System.out.println("parseNoSRS");
        String lexicalForm = "POINT(-83.38 33.95)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        Geometry geometry = instance.parse(lexicalForm);
        String srsName = GeometryDatatype.getSRSName(geometry);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = WKTDatatype.DEFAULT_SRS_NAME;

        boolean result = (geometry.equals(expGeometry)) && (srsName.equals(expSRSName));
        boolean expResult = true;

        System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseNoSRSNotEqual() {
        System.out.println("parseNoSRSNotEqual");
        String lexicalForm = "POINT(-83.38 33.95)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        Geometry geometry = instance.parse(lexicalForm);
        String srsName = GeometryDatatype.getSRSName(geometry);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = WKTDatatype.DEFAULT_SRS_NAME;

        boolean result = (geometry.equals(expGeometry)) && (srsName.equals(expSRSName));
        boolean expResult = false;

        System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseNoSRSNotEqual2() {
        System.out.println("parseNoSRSNotEqual2");
        String lexicalForm = "POINT(-83.38 33.95)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        Geometry geometry = instance.parse(lexicalForm);
        String srsName = GeometryDatatype.getSRSName(geometry);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/EPSG/0/4326";

        boolean result = (geometry.equals(expGeometry)) && (srsName.equals(expSRSName));
        boolean expResult = false;

        System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseSRS() {
        System.out.println("parseSRS");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(33.95 -88.38)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        Geometry geometry = instance.parse(lexicalForm);
        String srsName = GeometryDatatype.getSRSName(geometry);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/EPSG/0/4326";

        boolean result = (geometry.equals(expGeometry)) && (srsName.equals(expSRSName));
        boolean expResult = true;

        System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseSRSNotEqual() {
        System.out.println("parseSRSNotEqual");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(33.95 -88.38)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        Geometry geometry = instance.parse(lexicalForm);
        String srsName = GeometryDatatype.getSRSName(geometry);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/EPSG/0/4326";

        boolean result = (geometry.equals(expGeometry)) && (srsName.equals(expSRSName));
        boolean expResult = false;

        System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseSRSNotEqual2() {
        System.out.println("parseSRSNotEqual2");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(33.95 -88.38)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        Geometry geometry = instance.parse(lexicalForm);
        String srsName = GeometryDatatype.getSRSName(geometry);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = WKTDatatype.DEFAULT_SRS_NAME;

        boolean result = (geometry.equals(expGeometry)) && (srsName.equals(expSRSName));
        boolean expResult = false;

        System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result);
    }

}
