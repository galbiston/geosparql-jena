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
public class SfOverlapsTest {

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
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Test
    public void sfOverlapsBoundPostiveTest() {

        System.out.println("sfOverlaps Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonI";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfOverlaps", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Test
    public void sfOverlapsBoundNegativeTest() {

        System.out.println("sfOverlaps Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfOverlaps", "http://example.org/Geometry#LineStringF");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Test
    public void sfOverlapsUnboundPostiveTest() {

        System.out.println("sfOverlaps Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonH", "geo:sfOverlaps");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Test
    public void sfOverlapsUnboundNegativeTest() {

        System.out.println("sfOverlaps Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringF", "geo:sfOverlaps");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
