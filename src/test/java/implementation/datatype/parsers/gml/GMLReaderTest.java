/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.gml;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
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
 * @author haozhechen
 */
public class GMLReaderTest {

    public GMLReaderTest() {
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
    public void testPoint1() {
        GeometryWrapper geo = GMLReader.read("<gml:Point srsName='urn:ogc:def:crs:EPSG::27700' xmlns:gml='http://www.opengis.net/ont/gml'><gml:coordinates>-83.4 34.4</gml:coordinates></gml:Point>");
        Geometry test = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "-83.4 34.4"));
        GeometryWrapper expResult = new GeometryWrapper(test, "urn:ogc:def:crs:EPSG::27700", GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testLineString2() {
        GeometryWrapper geo = GMLReader.read("<gml:LineString srsName='urn:ogc:def:crs:EPSG::27700' xmlns:gml='http://www.opengis.net/ont/gml'><gml:coordinates>-83.4 34.0, -83.3 34.3</gml:coordinates></gml:LineString>");
        Geometry test = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "-83.4 34.0, -83.3 34.3"));
        GeometryWrapper expResult = new GeometryWrapper(test, "urn:ogc:def:crs:EPSG::27700", GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 1));

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

}
