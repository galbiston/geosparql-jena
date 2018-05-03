/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.simple_features;

import conformance_test.topology_vocabulary_extension.PropertyTestMethods;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 *
 * A.2.1.1 /conf/topology-vocab-extension/sf-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/sf-spatial-relations
 * Implementations shall allow the properties geo:sfEquals, geo:sfDisjoint,
 * geo:sfIntersects, geo:sfTouches, geo:sfCrosses, geo:sfWithin, geo:sfContains,
 * geo:sfOverlaps to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 7.2 Req 4
 *
 * d.) Test Type: Capabilities
 */
public class SfTouchesTest {

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
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void sfTouchesBoundPostiveTest() {

        System.out.println("sfTouches Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonJ";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonI", "geo:sfTouches", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void sfTouchesBoundPostiveTest2() {

        System.out.println("sfTouches Bound Positive2");
        String expResult = "http://example.org/Geometry#PointB";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfTouches", "http://example.org/Geometry#PointB");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void sfTouchesBoundNegativeTest() {

        System.out.println("sfTouches Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfTouches", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void sfTouchesUnboundPostiveTest() {

        System.out.println("sfTouches Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geo:sfTouches");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void sfTouchesUnboundNegativeTest() {

        System.out.println("sfTouches Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonK", "geo:sfTouches");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
