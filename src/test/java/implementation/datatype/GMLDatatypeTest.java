/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import implementation.DimensionInfo;
import implementation.GeoSPARQLSupport;
import implementation.GeometryWrapper;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomGeometryFactory;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.not;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 */
public class GMLDatatypeTest {

    public final String URN_SRS_NAMESPACE = "urn:ogc:def:crs:EPSG::27700";
    public final String URL_SRS_NAMESPACE = "http://www.opengis.net/def/crs/EPSG/0/27700";
    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();
    private static final GMLDatatype GML_DATATYPE = GMLDatatype.INSTANCE;

    public GMLDatatypeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        GeoSPARQLSupport.loadFunctionsNoIndex();
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

        String expResult = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";

        GMLDatatype instance = GMLDatatype.INSTANCE;

        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point point = GEOMETRY_FACTORY.createPoint(coord);
        String srsURI = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 2);

        GeometryWrapper geometry = new GeometryWrapper(point, srsURI, GeoDatatypeEnum.GML, dimensionInfo);

        String result = instance.unparse(geometry);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class GMLDatatype.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String lexicalForm = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";

        GMLDatatype instance = GMLDatatype.INSTANCE;
        GeometryWrapper result = instance.parse(lexicalForm);

        Coordinate coord = new Coordinate(-83.38, 33.95);
        Point expGeometry = GEOMETRY_FACTORY.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 0);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoDatatypeEnum.GML, dimensionInfo);

        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of parse method, of class GMLDatatype.
     */
    @Test
    public void testParseNotEqual() {
        System.out.println("parseNotEqual");
        String lexicalForm = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";

        GMLDatatype instance = GMLDatatype.INSTANCE;

        GeometryWrapper result = instance.parse(lexicalForm);

        Coordinate coord = new Coordinate(-88.38, 33.95);
        Point expGeometry = GEOMETRY_FACTORY.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/EPSG/0/4326";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 2);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoDatatypeEnum.GML, dimensionInfo);

        assertThat(expResult, not(result));
    }

    /**
     * Test of parse method, of class GMLDatatype.
     */
    @Test

    public void testParseNotEqual2() {
        System.out.println("parseNotEqual2");
        String lexicalForm = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";

        GMLDatatype instance = GMLDatatype.INSTANCE;

        GeometryWrapper result = instance.parse(lexicalForm);

        Coordinate coord = new Coordinate(33.95, -88.38);
        Point expGeometry = GEOMETRY_FACTORY.createPoint(coord);

        String expSRSName = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

        DimensionInfo dimensionInfo = new DimensionInfo(2, 2, 2);

        GeometryWrapper expResult = new GeometryWrapper(expGeometry, expSRSName, GeoDatatypeEnum.GML, dimensionInfo);

        assertThat(expResult, not(result));
    }

    @Test
    public void testPointURL() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:Point srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.4 34.4</gml:pos></gml:Point>");
        Geometry test = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "-83.4 34.4"));
        GeometryWrapper expResult = new GeometryWrapper(test, URL_SRS_NAMESPACE, GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 0));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testPointURN() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:Point srsName=\"urn:ogc:def:crs:EPSG::27700\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.4 34.4</gml:pos></gml:Point>");
        Geometry test = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "-83.4 34.4"));
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 0));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testLineString() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:LineString srsName=\"urn:ogc:def:crs:EPSG::27700\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:posList srsDimension=\"2\">-83.4 34.0 -83.3 34.3</gml:posList></gml:LineString>");
        Geometry test = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "-83.4 34.0, -83.3 34.3"));
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 1));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testPolygon() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">30 10 40 40 20 40 10 20 30 10</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>");
        Geometry test = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 10, 40 40, 20 40, 10 20, 30 10"));
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 2));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testPolygon2() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">30 10 40 40 20 40 10 20 30 10</gml:posList></gml:LinearRing></gml:exterior><gml:interior><gml:LinearRing><gml:posList srsDimension=\"2\">20 30 35 35 30 20 20 30</gml:posList></gml:LinearRing></gml:interior></gml:Polygon>");
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 10, 40 40, 20 40, 10 20, 30 10"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "20 30, 35 35, 30 20, 20 30"))};
        Geometry test = GEOMETRY_FACTORY.createPolygon(shell, holes);
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 2));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testMultiPoint() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:MultiPoint xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:PointMember><gml:Point><gml:pos>10 40</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point><gml:pos>40 30</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point><gml:pos>20 20</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point><gml:pos>30 10</gml:pos></gml:Point></gml:PointMember></gml:MultiPoint>");
        Geometry test = GEOMETRY_FACTORY.createMultiPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "10 40, 40 30, 20 20, 30 10"));
        GeometryWrapper expResult = new GeometryWrapper(test, "urn:ogc:def:crs:EPSG::27700", GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 0));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testMultiLineString() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:MultiLineString xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:LineStringMember><gml:LineString><gml:posList srsDimension=\"2\">10 10 20 20 10 40</gml:posList></gml:LineString></gml:LineStringMember><gml:LineStringMember><gml:LineString><gml:posList srsDimension=\"2\">40 40 30 30 40 20 30 10</gml:posList></gml:LineString></gml:LineStringMember></gml:MultiLineString>");
        LineString[] lineStrings = new LineString[2];
        lineStrings[0] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "10 10, 20 20, 10 40"));
        lineStrings[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "40 40, 30 30, 40 20, 30 10"));
        Geometry test = GEOMETRY_FACTORY.createMultiLineString(lineStrings);
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 1));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testMultiPolygon() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:MultiPolygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:PolygonMember><gml:Polygon srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">40 40 20 45 45 30 40 40</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon></gml:PolygonMember><gml:PolygonMember><gml:Polygon srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">20 35 10 30 10 10 30 5 45 20 20 35</gml:posList></gml:LinearRing></gml:exterior><gml:interior><gml:LinearRing><gml:posList srsDimension=\"2\">30 20 20 15 20 25 30 20</gml:posList></gml:LinearRing></gml:interior></gml:Polygon></gml:PolygonMember></gml:MultiPolygon>");
        Polygon[] polygons = new Polygon[2];
        polygons[0] = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "40 40, 20 45, 45 30, 40 40"));
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "20 35, 10 30, 10 10, 30 5, 45 20, 20 35"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 20, 20 15, 20 25, 30 20"))};
        polygons[1] = GEOMETRY_FACTORY.createPolygon(shell, holes);
        Geometry test = GEOMETRY_FACTORY.createMultiPolygon(polygons);
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 2));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testGeometryCollection() throws JDOMException, IOException {
        GeometryWrapper geo = GML_DATATYPE.read("<gml:GeometryCollection xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:GeometryMember><gml:Point srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:pos>4 6</gml:pos></gml:Point></gml:GeometryMember><gml:GeometryMember><gml:LineString srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:posList srsDimension=\"2\">4 6 7 10</gml:posList></gml:LineString></gml:GeometryMember></gml:GeometryCollection>");
        Geometry[] geometries = new Geometry[2];
        geometries[0] = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "4 6"));
        geometries[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "4 6,7 10"));
        Geometry test = GEOMETRY_FACTORY.createGeometryCollection(geometries);
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoDatatypeEnum.GML, new DimensionInfo(2, 2, 1));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

}
