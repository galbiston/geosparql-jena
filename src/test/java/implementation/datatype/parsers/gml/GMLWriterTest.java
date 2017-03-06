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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 */
public class GMLWriterTest {

    public GMLWriterTest() {
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
    public void testWrite() {
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "11.0 12.1"));
        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, "urn:ogc:def:crs:EPSG::27700", GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        String result = GMLWriter.write(geometryWrapper);
        System.out.println("Result: " + result);

    }

}
