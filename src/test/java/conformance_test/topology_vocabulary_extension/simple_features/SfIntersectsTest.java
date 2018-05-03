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
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PointB", "geo:sfIntersects", "http://example.org/Geometry#LineStringE");

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
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#LineStringG", "geo:sfIntersects", "http://example.org/Geometry#LineStringE");

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
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#LineStringG", "geo:sfIntersects", "http://example.org/Geometry#PolygonI");

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
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PointB", "geo:sfIntersects", "http://example.org/Geometry#LineStringD");

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
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#LineStringF", "geo:sfIntersects", "http://example.org/Geometry#LineStringD");

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
        expResult.add("http://example.org/Feature#D");
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PolygonH");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringG", "geo:sfIntersects");

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
        expResult.add("http://example.org/Feature#F");
        expResult.add("http://example.org/Geometry#LineStringF");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringF", "geo:sfIntersects");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
