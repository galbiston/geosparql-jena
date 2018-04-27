/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension.simplefeatures_query_functions;

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
public class SfContainsTest {

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
     * Contains returns t (TRUE) if the second geometry is completely contained
     * by the first geometry.
     */
    @Test
    public void boundPostiveTest() {

        System.out.println("Bound Positive");
        String expResult = "http://example.org/Geometry#PointA";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:sfContains", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void boundNegativeTest() {

        System.out.println("Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:sfContains", "http://example.org/Geometry#PointC");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void unboundPostiveTest() {

        System.out.println("Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PointA");
        expResult.add("http://example.org/Geometry#LineStringA");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonH", "geof:sfContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void unboundNegativeTest() {

        System.out.println("Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geof:sfContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
