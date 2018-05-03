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
public class Rcc8ntppiTest {

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
     * Every point of b is a point of a, but boundaries do not touch.
     */
    @Test
    public void rcc8nttpiBoundPostiveTest() {

        System.out.println("rcc8nttpi Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonL";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonJ", "geof:rcc8ntppi", "http://example.org/Geometry#PolygonL");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of b is a point of a, but boundaries do not touch.
     */
    @Test
    public void rcc8nttpiBoundNegativeTest() {

        System.out.println("rcc8nttpi Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonJ", "geof:rcc8ntppi", "http://example.org/Geometry#PolygonK");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of b is a point of a, but boundaries do not touch.
     */
    @Test
    public void rcc8nttpiUnboundPostiveTest() {

        System.out.println("rcc8nttpi Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonL");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geof:rcc8ntppi");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of b is a point of a, but boundaries do not touch.
     */
    @Test
    public void rcc8nttpiUnboundNegativeTest() {

        System.out.println("rcc8nttpi Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geof:rcc8ntppi");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
