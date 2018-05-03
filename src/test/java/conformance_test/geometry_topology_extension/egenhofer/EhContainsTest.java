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
public class EhContainsTest {

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
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsBoundPostiveTest() {

        System.out.println("ehContains Bound Positive");
        String expResult = "http://example.org/Geometry#PointA";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:ehContains", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsBoundPostiveTest2() {

        System.out.println("ehContains Bound Positive2");
        String expResult = "http://example.org/Geometry#PolygonL";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonJ", "geof:ehContains", "http://example.org/Geometry#PolygonL");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsBoundNegativeTest() {

        System.out.println("ehContains Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geof:ehContains", "http://example.org/Geometry#PointC");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsUnboundPostiveTest() {

        System.out.println("ehContains Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#PointA");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonH", "geof:ehContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsUnboundNegativeTest() {

        System.out.println("ehContains Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geof:ehContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
