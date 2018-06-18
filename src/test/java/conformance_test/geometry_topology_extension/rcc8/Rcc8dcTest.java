/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension.rcc8;

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
 * A.4.4.1 /conf/geometry-topology-extension/rcc8-query-functions
 *
 * Requirement: /req/geometry-topology-extension/rcc8-query-functions
 * Implementations shall support geof:rcc8eq, geof:rcc8dc, geof:rcc8ec,
 * geof:rcc8po, geof:rcc8tppi, geof:rcc8tpp, geof:rcc8ntpp, geof:rcc8ntppi as
 * SPARQL extension functions, consistent with their corresponding DE-9IM
 * intersection patterns, as defined by Simple Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each of the
 * following functions returns the correct result for a test dataset when using
 * the specified serialization and version: geof:rcc8eq, geof:rcc8dc,
 * geof:rcc8ec, geof:rcc8po, geof:rcc8tppi, geof:rcc8tpp, geof:rcc8ntpp,
 * geof:rcc8ntppi.
 *
 * c.) Reference: Clause 9.5 Req 24
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8dcTest {

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
     * Disjoint returns t (TRUE) if the intersection of the two geometries is an
     * empty set.
     */
    @Test
    public void rcc8dcBoundPostiveTest() {

        System.out.println("rcc8dc Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonL";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:rcc8dc", "http://example.org/Geometry#PolygonL");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Disjoint returns t (TRUE) if the intersection of the two geometries is an
     * empty set.
     */
    @Test
    public void rcc8dcBoundNegativeTest() {

        System.out.println("rcc8dc Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:rcc8dc", "http://example.org/Geometry#PolygonK");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Disjoint returns t (TRUE) if the intersection of the two geometries is an
     * empty set.
     */
    @Test
    public void rcc8dcUnboundPostiveTest() {

        System.out.println("rcc8dc Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonJ");
        expResult.add("http://example.org/Geometry#PolygonL");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonH", "geof:rcc8dc");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Disjoint returns t (TRUE) if the intersection of the two geometries is an
     * empty set.
     */
    @Test
    public void rcc8dcUnboundNegativeTest() {

        System.out.println("rcc8dc Unbound Negative");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonH");
        expResult.add("http://example.org/Geometry#PolygonK");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geof:rcc8dc");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
