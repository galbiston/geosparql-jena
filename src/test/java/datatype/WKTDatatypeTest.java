/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import implementation.datatype.GeoSerialisationEnum;
import implementation.datatype.WKTDatatype;
import implementation.datatype.GeometryWrapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
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

        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point point = geometryFactory.createPoint(coord);
        String srsURI = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        GeometryWrapper geometry = new GeometryWrapper(point, srsURI, GeoSerialisationEnum.WKT);

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

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);
        String expSRSURI = WKTDatatype.DEFAULT_SRS_URI;

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

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

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = WKTDatatype.DEFAULT_SRS_URI;

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseNoSRSNotEqual2() {
        System.out.println("parseNoSRSNotEqual2");
        String lexicalForm = "POINT(-83.38 33.95)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = "http://www.opengis.net/def/crs/EPSG/0/4326";

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseSRS() {
        System.out.println("parseSRS");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(33.95 -88.38)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = "http://www.opengis.net/def/crs/EPSG/0/4326";

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

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

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = "http://www.opengis.net/def/crs/EPSG/0/4326";

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParseSRSNotEqual2() {
        System.out.println("parseSRSNotEqual2");
        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(33.95 -88.38)";
        WKTDatatype instance = WKTDatatype.theWKTDatatype;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSURI = WKTDatatype.DEFAULT_SRS_URI;

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSURI, GeoSerialisationEnum.WKT);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

        assertThat(expResult, not(result));
    }

}
