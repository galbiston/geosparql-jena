/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.gml2.GMLReader;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.support.GeoSerialisationEnum;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

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

        GMLDatatype instance = GMLDatatype.THE_GML_DATATYPE;

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point point = geometryFactory.createPoint(coord);
        String srsURI = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 2);

        GeometryWrapper geometry = new GeometryWrapper(point, srsURI, GeoSerialisationEnum.GML, dimensionInfo);

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

        GMLDatatype instance = GMLDatatype.THE_GML_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 2);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoSerialisationEnum.GML, dimensionInfo);

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

        GMLDatatype instance = GMLDatatype.THE_GML_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/EPSG/0/4326";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 2);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoSerialisationEnum.GML, dimensionInfo);

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

        GMLDatatype instance = GMLDatatype.THE_GML_DATATYPE;

        GeometryWrapper result = instance.parse(lexicalForm);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = geometryFactory.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 2);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoSerialisationEnum.GML, dimensionInfo);

        System.out.println("Expected: " + expResult);
        System.out.println(" Result: " + result);

        assertThat(expResult, not(result));
    }

    /**
     * Test of findSrsURI method, of class GMLDatatype.
     */
    @Test
    public void testFindSrsURI() {
        System.out.println("findSrsURI");

        XML xml = new XMLDocument("<gml:Point srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>");
        Node node = xml.node();

        NamedNodeMap attributes = xml.node().getAttributes();

        String expResult = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";
        String result = GMLDatatype.findSrsURI(attributes);
        assertEquals(expResult, result);
    }

    /**
     * Test of findCoordinateDimension method, of class GMLDatatype.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    @Test
    public void testFindCoordinateDimension2() throws SAXException, IOException, ParserConfigurationException {
        System.out.println("findCoordinateDimension2");
        String lexicalForm = "<gml:Point srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";
        XML xml = new XMLDocument(lexicalForm);
        NamedNodeMap attributes = xml.node().getAttributes();
        String srsName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        GMLReader gmlReader = new GMLReader();
        Geometry geometry = gmlReader.read(lexicalForm, null);

        int expResult = 2;
        DimensionInfo result = GMLDatatype.extractDimensionInfo(attributes, srsName, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findCoordinateDimension method, of class GMLDatatype.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    @Test
    public void testFindCoordinateDimension3() throws SAXException, IOException, ParserConfigurationException {
        System.out.println("findCoordinateDimension3");
        String lexicalForm = "<gml:Point srsDimension=\"3\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.38 33.95 10.0</gml:pos></gml:Point>";
        XML xml = new XMLDocument(lexicalForm);
        NamedNodeMap attributes = xml.node().getAttributes();
        String srsName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        GMLReader gmlReader = new GMLReader();
        Geometry geometry = gmlReader.read(lexicalForm, null);

        int expResult = 3;
        DimensionInfo result = GMLDatatype.extractDimensionInfo(attributes, srsName, geometry);
        assertEquals(expResult, result.getCoordinate());
    }

    /**
     * Test of findSpatialDimension method, of class GMLDatatype.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    @Test
    public void testFindSpatialDimension2() throws SAXException, IOException, ParserConfigurationException {
        System.out.println("findSpatialDimension2");
        String lexicalForm = "<gml:Point srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";
        XML xml = new XMLDocument(lexicalForm);
        NamedNodeMap attributes = xml.node().getAttributes();
        String srsName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        GMLReader gmlReader = new GMLReader();
        Geometry geometry = gmlReader.read(lexicalForm, null);

        int expResult = 2;
        DimensionInfo result = GMLDatatype.extractDimensionInfo(attributes, srsName, geometry);
        assertEquals(expResult, result.getSpatial());
    }

    /**
     * Test of findSpatialDimension method, of class GMLDatatype.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    @Test
    public void testFindSpatialDimension3() throws SAXException, IOException, ParserConfigurationException {
        System.out.println("findSpatialDimension3");
        String lexicalForm = "<gml:Point srsDimension=\"3\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.38 33.95 10.0</gml:pos></gml:Point>";
        XML xml = new XMLDocument(lexicalForm);
        NamedNodeMap attributes = xml.node().getAttributes();
        String srsName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        GMLReader gmlReader = new GMLReader();
        Geometry geometry = gmlReader.read(lexicalForm, null);

        int expResult = 3;
        DimensionInfo result = GMLDatatype.extractDimensionInfo(attributes, srsName, geometry);
        assertEquals(expResult, result.getSpatial());
    }

}
