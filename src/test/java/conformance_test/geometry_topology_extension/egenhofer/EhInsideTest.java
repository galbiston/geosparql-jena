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
public class EhInsideTest {

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
     * ehInside is slightly different from the sfWithin, which will not return
     * the same instance while the sfWithin will return the same instance.
     */
    @Test
    public void ehInsideBoundPostiveTest() {

        System.out.println("ehInside Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonJ";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonL", "geof:ehInside", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehInside is slightly different from the sfWithin, which will not return
     * the same instance while the sfWithin will return the same instance.
     */
    @Test
    public void ehInsideBoundNegativeTest() {

        System.out.println("ehInside Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:ehInside", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehInside is slightly different from the sfWithin, which will not return
     * the same instance while the sfWithin will return the same instance.
     */
    @Test
    public void ehInsideUnboundPostiveTest() {

        System.out.println("sfContains Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonJ");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonL", "geof:ehInside");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehInside is slightly different from the sfWithin, which will not return
     * the same instance while the sfWithin will return the same instance.
     */
    @Test
    public void ehInsideUnboundNegativeTest() {

        System.out.println("ehInside Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geof:ehInside");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
