/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.egenhofer;

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
 * A.2.2.1 /conf/topology-vocab-extension/eh-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/eh-spatial-relations
 * Implementations shall allow the properties geo:ehEquals, geo:ehDisjoint,
 * geo:ehMeet, geo:ehOverlap, geo:ehCovers, geo:ehCoveredBy, geo:ehInside,
 * geo:ehContains to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 7.3 Req 5
 *
 * d.) Test Type: Capabilities
 */
public class EhOverlapTest {

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
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Test
    public void ehOverlapBoundPostiveTest() {

        System.out.println("ehOverlap Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonI";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehOverlap", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Test
    public void ehOverlapBoundNegativeTest() {

        System.out.println("ehOverlap Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehOverlap", "http://example.org/Geometry#LineStringF");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Test
    public void ehOverlapUnboundPostiveTest() {

        System.out.println("ehOverlap Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonH", "geo:ehOverlap");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Test
    public void ehOverlapUnboundNegativeTest() {

        System.out.println("ehOverlap Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringF", "geo:ehOverlap");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
