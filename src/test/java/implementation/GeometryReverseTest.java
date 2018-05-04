/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.registry.CRSRegistry;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 *
 */
public class GeometryReverseTest {

    public GeometryReverseTest() {
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
     * Test of check method, of class GeometryReverse.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testCheckPoint() throws FactoryException {
        System.out.println("checkPoint");

        WKTReader reader = new WKTReader();
        try {
            Point geometry = (Point) reader.read("POINT(2 0)");
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");

            Geometry expResult = reader.read("POINT(0 2)");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }

    /**
     * Test of check method, of class GeometryReverse.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testCheckLineString() throws FactoryException {
        System.out.println("checkLineString");

        WKTReader reader = new WKTReader();
        try {
            LineString geometry = (LineString) reader.read("LINESTRING(0 0, 2 0, 5 0)");
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");

            Geometry expResult = reader.read("LINESTRING(0 0, 0 2, 0 5)");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }

    /**
     * Test of check method, of class GeometryReverse.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testCheckPolygon() throws FactoryException {
        System.out.println("checkPolygon");

        WKTReader reader = new WKTReader();
        try {
            Polygon geometry = (Polygon) reader.read("POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))");
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");

            Geometry expResult = reader.read("POLYGON ((10 30, 40 40, 40 20, 20 10, 10 30))");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }

    /**
     * Test of check method, of class GeometryReverse.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testCheckPolygonHoled() throws FactoryException {
        System.out.println("checkPolygonHoled");

        WKTReader reader = new WKTReader();
        try {
            Polygon geometry = (Polygon) reader.read("POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35 35, 30 20, 20 30))");
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");

            Geometry expResult = reader.read("POLYGON ((10 35, 45 45, 40 15, 20 10, 10 35),(30 20, 35 35, 20 30, 30 20))");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }

    /**
     * Test of check method, of class GeometryReverse.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testCheckMultiPoint() throws FactoryException {
        System.out.println("checkMultiPoint");

        WKTReader reader = new WKTReader();
        try {
            MultiPoint geometry = (MultiPoint) reader.read("MULTIPOINT (10 40, 40 30, 20 20, 30 10)");
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");

            Geometry expResult = reader.read("MULTIPOINT (40 10, 30 40, 20 20, 10 30)");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }

    /**
     * Test of check method, of class GeometryReverse.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testCheckMultiPolygon() throws FactoryException {
        System.out.println("checkMultiPolygon");

        WKTReader reader = new WKTReader();
        try {
            MultiPolygon geometry = (MultiPolygon) reader.read("MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))");
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");

            Geometry expResult = reader.read("MULTIPOLYGON (((20 30, 40 45, 40 10, 20 30)),((5 15, 10 40, 20 10, 10 5, 5 15)))");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }

    /**
     * Test of check method, of class GeometryReverse.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testCheckMultiLineString() throws FactoryException {
        System.out.println("checkMultiLineString");

        WKTReader reader = new WKTReader();
        try {
            MultiLineString geometry = (MultiLineString) reader.read("MULTILINESTRING ((10 10, 20 30, 10 40),(40 45, 30 35, 40 20, 30 10))");
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");

            Geometry expResult = reader.read("MULTILINESTRING ((10 10, 30 20, 40 10),(45 40, 35 30, 20 40, 10 30))");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }

    /**
     * Test of check method, of class GeometryReverse.
     */
    @Test
    public void testCheckLineStringNotReversed() {
        System.out.println("checkLineStringNotReversed");

        WKTReader reader = new WKTReader();
        try {
            LineString geometry = (LineString) reader.read("LINESTRING(0 0, 2 0, 5 0)");
            CoordinateReferenceSystem crs = CRSRegistry.getCRS(implementation.vocabulary.CRS_URI.DEFAULT_WKT_CRS84);
            Geometry expResult = reader.read("LINESTRING(0 0, 2 0, 5 0)");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }

    /**
     * Test of check method, of class GeometryReverse.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testCheckMultiPolygonHoled() throws FactoryException {
        System.out.println("checkMultiPolygonHoled");

        WKTReader reader = new WKTReader();
        try {
            MultiPolygon geometry = (MultiPolygon) reader.read("MULTIPOLYGON (((35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35 35, 30 20, 20 30)),((15 5, 40 10, 10 20, 5 10, 15 5)))");
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");

            Geometry expResult = reader.read("MULTIPOLYGON (((10 35, 45 45, 40 15, 20 10, 10 35),(30 20, 35 35, 20 30, 30 20)),((5 15, 10 40, 20 10, 10 5, 5 15)))");
            Geometry result = GeometryReverse.check(geometry, crs);

            //System.out.println("Expected: " + expResult);
            //System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }

    }
}
