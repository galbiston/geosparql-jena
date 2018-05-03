/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension.egenhofer;

import conformance_test.geometry_topology_extension.FilterTestMethods;
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
 * A.4.3.1 /conf/geometry-topology-extension/eh-query-functions
 *
 * Requirement: /req/geometry-topology-extension/eh-query-functions
 * Implementations shall support geof:ehEquals, geof:ehDisjoint, geof:ehMeet,
 * geof:ehOverlap, geof:ehCovers, geof:ehCoveredBy, geof:ehInside,
 * geof:ehContains as SPARQL extension functions, consistent with their
 * corresponding DE-9IM intersection patterns, as defined by Simple Features
 * [ISO 19125- 1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each of the
 * following functions returns the correct result for a test dataset when using
 * the specified serialization and version: geof:ehEquals, geof:ehDisjoint,
 * geof:ehMeet, geof:ehOverlap, geof:ehCovers, geof:ehCoveredBy, geof:ehInside,
 * geof:ehContains.
 *
 * c.) Reference: Clause 9.4 Req 23
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
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:ehOverlap", "http://example.org/Geometry#PolygonI");

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
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:ehOverlap", "http://example.org/Geometry#LineStringF");

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
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonH", "geof:ehOverlap");

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
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringF", "geof:ehOverlap");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
