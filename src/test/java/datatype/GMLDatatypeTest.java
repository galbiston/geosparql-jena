/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import implementation.support.GeoSerialisationEnum;
import implementation.datatype.GMLDatatype;
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
public class GMLDatatypeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GMLDatatypeTest.class);

    public GMLDatatypeTest() {
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
     * Test of unparse method, of class GMLDatatype.
     */
    @Test
    public void testUnparse() {
        System.out.println("unparse");

        //JTS GMLWriter needs changing to GeoTools writer if possible. Version 3. http://gis.stackexchange.com/questions/3940/how-to-write-gml-with-geotools
        String expResult = "<gml:Point xmlns:gml=\'http://www.opengis.net/ont/gml\' srsName=\'http://www.opengis.net/def/crs/OGC/1.3/CRS84\'><gml:pos>-83.38 33.95</gml:pos></gml:Point>";

        GMLDatatype instance = GMLDatatype.theGMLDatatype;

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point point = geometryFactory.createPoint(coord);
        String srsURI = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";
        GeometryWrapper geometry = new GeometryWrapper(point, srsURI, GeoSerialisationEnum.GML);

        String result = instance.unparse(geometry);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);

        assertEquals(expResult, result);

    }

    /**
     * Test of parse method, of class GMLDatatype.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String lexicalForm = "<gml:Point srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";

        GMLDatatype instance = GMLDatatype.theGMLDatatype;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoSerialisationEnum.GML);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class GMLDatatype.
     */
    @Test
    public void testParseNotEqual() {
        System.out.println("parseNotEqual");
        String lexicalForm = "<gml:Point srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";

        GMLDatatype instance = GMLDatatype.theGMLDatatype;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/EPSG/0/4326";

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoSerialisationEnum.GML);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of parse method, of class GMLDatatype.
     */
    @Test
    public void testParseNotEqual2() {
        System.out.println("parseNotEqual2");
        String lexicalForm = "<gml:Point srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";

        GMLDatatype instance = GMLDatatype.theGMLDatatype;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoSerialisationEnum.GML);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

        assertThat(expResult, not(result));
    }

}
