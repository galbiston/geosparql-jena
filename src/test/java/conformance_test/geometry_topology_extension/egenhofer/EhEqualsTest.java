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
public class EhEqualsTest {

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
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void ehEqualsBoundPostiveTest() {

        System.out.println("ehEquals Bound Positive");
        String expResult = "http://example.org/Geometry#PointC2";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PointC", "geof:ehEquals", "http://example.org/Geometry#PointC2");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void ehEqualsBoundNegativeTest() {

        System.out.println("ehEquals Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PointC", "geof:ehEquals", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void ehEqualsUnboundPostiveTest() {

        System.out.println("ehEquals Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PointC", "geof:ehEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void ehEqualsUnboundNegativeTest() {

        System.out.println("ehEquals Unbound Negative");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonJ");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geof:ehEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
