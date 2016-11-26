/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.datatype.WKTDatatype;
import implementation.support.GeoSerialisationEnum;
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
public class WKTParserTest {

    public WKTParserTest() {
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

    /**
     * Test of read method, of class WKTParser.
     */
    @Test
    public void testRead() {
        System.out.println("read");
        String wktLiteral = "POINT ZM (11.0 12.0 8.0 5.0)";
        WKTParser instance = new WKTParser();
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.Dimensions.XYZM, "11.0 12.0 8.0 5.0"));
        GeometryWrapper expResult = new GeometryWrapper(geometry, WKTDatatype.DEFAULT_WKT_CRS_URI, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 0));
        GeometryWrapper result = instance.read(wktLiteral);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTParser.
     */
    @Test
    public void testWritePoint() {
        System.out.println("writePoint");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.Dimensions.XYZM, "11.0 12.1 8.0 5.0"));

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, WKTDatatype.DEFAULT_WKT_CRS_URI, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 0));
        WKTParser instance = new WKTParser();
        String expResult = "<" + WKTDatatype.DEFAULT_WKT_CRS_URI + "> POINT ZM (11 12.1 8 5)";
        String result = instance.write(geometryWrapper);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class WKTParser.
     */
    @Test
    public void testWriteLineString() {
        System.out.println("writeLineString");
        Geometry geometry = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.Dimensions.XYZM, "11.0 12.1 8.0 5.0, 3 4 6 2"));

        GeometryWrapper geometryWrapper = new GeometryWrapper(geometry, WKTDatatype.DEFAULT_WKT_CRS_URI, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 0));
        WKTParser instance = new WKTParser();
        String expResult = "<" + WKTDatatype.DEFAULT_WKT_CRS_URI + "> LINESTRING ZM (11 12.1 8 5, 3 4 6 2)";
        String result = instance.write(geometryWrapper);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

}
