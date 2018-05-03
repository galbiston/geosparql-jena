/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension.simple_features;

import conformance_test.geometry_topology_extension.*;
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
 * A.4.2.1 /conf/geometry-topology-extension/sf-query-functions
 *
 * Requirement: /req/geometry-topology-extension/sf-query-functions
 * Implementations shall support geof:sfEquals, geof:sfDisjoint,
 * geof:sfIntersects, geof:sfTouches, geof:sfCrosses, geof:sfWithin,
 * geof:sfContains, geof:sfOverlaps as SPARQL extension functions, consistent
 * with their corresponding DE-9IM intersection patterns, as defined by Simple
 * Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each of the
 * following functions returns the correct result for a test dataset when using
 * the specified serialization and version: geof:sfEquals, geof:sfDisjoint,
 * geof:sfIntersects, geof:sfTouches, geof:sfCrosses, geof:sfWithin,
 * geof:sfContains, geof:sfOverlaps.
 *
 * c.) Reference: Clause 9.3 Req 22
 *
 * d.) Test Type: Capabilities
 */
public class SfIntersectsTest {

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
     * Intersects returns t (TRUE) if the intersection does not result in an
     * empty set, Intersects returns the exact opposite result of disjoint.
     */
    @Test
    public void sfIntersectsBoundPostiveTest() {

        System.out.println("sfIntersects Bound Positive");
        String expResult = "http://example.org/Geometry#LineStringE";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PointB", "geof:sfIntersects", "http://example.org/Geometry#LineStringE");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Intersects returns t (TRUE) if the intersection does not result in an
     * empty set, Intersects returns the exact opposite result of disjoint.
     */
    @Test
    public void sfIntersectsBoundPostiveTest2() {

        System.out.println("sfIntersects Bound Positive2");
        String expResult = "http://example.org/Geometry#LineStringE";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#LineStringG", "geof:sfIntersects", "http://example.org/Geometry#LineStringE");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Intersects returns t (TRUE) if the intersection does not result in an
     * empty set, Intersects returns the exact opposite result of disjoint.
     */
    @Test
    public void sfIntersectsBoundPostiveTest3() {

        System.out.println("sfIntersects Bound Positive3");
        String expResult = "http://example.org/Geometry#PolygonI";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#LineStringG", "geof:sfIntersects", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Intersects returns t (TRUE) if the intersection does not result in an
     * empty set, Intersects returns the exact opposite result of disjoint.
     */
    @Test
    public void sfIntersectsBoundNegativeTest() {

        System.out.println("sfIntersects Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PointB", "geof:sfIntersects", "http://example.org/Geometry#LineStringD");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Intersects returns t (TRUE) if the intersection does not result in an
     * empty set, Intersects returns the exact opposite result of disjoint.
     */
    @Test
    public void sfIntersectsBoundNegativeTest2() {

        System.out.println("sfIntersects Bound Negative2");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#LineStringF", "geof:sfIntersects", "http://example.org/Geometry#LineStringD");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Intersects returns t (TRUE) if the intersection does not result in an
     * empty set, Intersects returns the exact opposite result of disjoint.
     */
    @Test
    public void sfIntersectsUnboundPostiveTest() {

        System.out.println("sfIntersects Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PolygonH");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringG", "geof:sfIntersects");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Intersects returns t (TRUE) if the intersection does not result in an
     * empty set, Intersects returns the exact opposite result of disjoint.
     */
    @Test
    public void sfIntersectsUnboundNegativeTest() {

        System.out.println("sfIntersects Unbound Negative");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#LineStringF");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringF", "geof:sfIntersects");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
