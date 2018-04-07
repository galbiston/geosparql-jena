/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.parsers.wkt;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import implementation.CRSRegistry;
import implementation.CustomGeometryFactory;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.jts.CustomCoordinateSequence;
import implementation.support.GeoSerialisationEnum;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 */
public class WKTReaderTest {

    public WKTReaderTest() {
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

    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadPoint() {
        System.out.println("readPoint");
        String wktLiteral = "POINT ZM (11.0 12.0 8.0 5.0)";
        WKTReader instance = new WKTReader();
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "11.0 12.0 8.0 5.0"));
        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 0));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadPoint2() {
        System.out.println("readPoint2");
        String wktLiteral = "<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT ZM (11.0 12.0 8.0 5.0)";
        WKTReader instance = new WKTReader();
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "11.0 12.0 8.0 5.0"));
        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 0));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadLineString() {
        System.out.println("readLineString");
        String wktLiteral = "LINESTRING ZM (11 12.1 8 5, 3 4 6 2)";
        WKTReader instance = new WKTReader();
        Geometry geometry = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "11 12.1 8 5, 3 4 6 2"));
        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 1));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadPolygon() {
        System.out.println("readPolygon");
        String wktLiteral = "POLYGON ZM ((30 10 0 1, 40 40 0 1, 20 40 0 1, 10 20 0 1, 30 10 0 1))";
        WKTReader instance = new WKTReader();
        Geometry geometry = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "30 10 0 1, 40 40 0 1, 20 40 0 1, 10 20 0 1, 30 10 0 1"));
        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 2));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadPolygon2() {
        System.out.println("readPolygon2");
        String wktLiteral = "POLYGON ZM ((30 10 0 1, 40 40 0 1, 20 40 0 1, 10 20 0 1, 30 10 0 1), (20 30 0 1, 35 35 0 1, 30 20 0 1, 20 30 0 1))";
        WKTReader instance = new WKTReader();

        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "30 10 0 1, 40 40 0 1, 20 40 0 1, 10 20 0 1, 30 10 0 1"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "20 30 0 1, 35 35 0 1, 30 20 0 1, 20 30 0 1"))};
        Geometry geometry = GEOMETRY_FACTORY.createPolygon(shell, holes);

        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 2));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadMultiPoint() {
        System.out.println("readMultiPoint");
        String wktLiteral = "MULTIPOINT ZM ((10 40 0 1), (40 30 0 1), (20 20 0 1), (30 10 0 1))";
        WKTReader instance = new WKTReader();
        Geometry geometry = GEOMETRY_FACTORY.createMultiPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "10 40 0 1, 40 30 0 1, 20 20 0 1, 30 10 0 1"));
        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 0));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadMultiLineString() {
        System.out.println("readMultiLineString");
        String wktLiteral = "MULTILINESTRING ZM ((10 10 0 1, 20 20 0 1, 10 40 0 1), (40 40 0 1, 30 30 0 1, 40 20 0 1, 30 10 0 1))";
        WKTReader instance = new WKTReader();

        LineString[] lineStrings = new LineString[2];
        lineStrings[0] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "10 10 0 1, 20 20 0 1, 10 40 0 1"));
        lineStrings[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "40 40 0 1, 30 30 0 1, 40 20 0 1, 30 10 0 1"));
        Geometry geometry = GEOMETRY_FACTORY.createMultiLineString(lineStrings);

        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 1));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadMultiPolygon() {
        System.out.println("readMultiPolygon");
        String wktLiteral = "MULTIPOLYGON ZM (((40 40 0 1, 20 45 0 1, 45 30 0 1, 40 40 0 1)), ((20 35 0 1, 10 30 0 1, 10 10 0 1, 30 5 0 1, 45 20 0 1, 20 35 0 1), (30 20 0 1, 20 15 0 1, 20 25 0 1, 30 20 0 1)))";
        WKTReader instance = new WKTReader();

        Polygon[] polygons = new Polygon[2];
        polygons[0] = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "40 40 0 1, 20 45 0 1, 45 30 0 1, 40 40 0 1"));
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "20 35 0 1, 10 30 0 1, 10 10 0 1, 30 5 0 1, 45 20 0 1, 20 35 0 1"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "30 20 0 1, 20 15 0 1, 20 25 0 1, 30 20 0 1"))};
        polygons[1] = GEOMETRY_FACTORY.createPolygon(shell, holes);
        Geometry geometry = GEOMETRY_FACTORY.createMultiPolygon(polygons);

        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 2));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class WKTReader.
     */
    @Test
    public void testReadGeometryCollection() {
        System.out.println("readGeometryCollection");
        String wktLiteral = "GEOMETRYCOLLECTION ZM (POINT ZM (4 6 0 1), LINESTRING ZM (4 6 0 1, 7 10 0 1))";
        WKTReader instance = new WKTReader();

        Geometry[] geometries = new Geometry[2];
        geometries[0] = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "4 6 0 1"));
        geometries[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XYZM, "4 6 0 1,7 10 0 1"));
        Geometry geometry = GEOMETRY_FACTORY.createGeometryCollection(geometries);

        GeometryWrapper expResult = new GeometryWrapper(geometry, CRSRegistry.DEFAULT_WKT_CRS, GeoSerialisationEnum.WKT, new DimensionInfo(4, 3, 1));
        GeometryWrapper result = instance.read(wktLiteral);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

}
