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
public class Rcc8ecTest {

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
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Test
    public void rcc8ecBoundPostiveTest() {

        System.out.println("rcc8ec Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonJ";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonI", "geof:rcc8ec", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Test
    public void rcc8ecBoundNegativeTest() {

        System.out.println("rcc8ec Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:rcc8ec", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Test
    public void rcc8ecUnboundPostiveTest() {

        System.out.println("rcc8ec Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonJ");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geof:rcc8ec");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Test
    public void rcc8ecUnboundNegativeTest() {

        System.out.println("rcc8ec Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonL", "geof:rcc8ec");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
