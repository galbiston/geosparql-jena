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
public class EhMeetTest {

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
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void ehMeetBoundPostiveTest() {

        System.out.println("ehMeet Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonJ";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonI", "geo:ehMeet", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehMeet has same functionality with sfTouches since they have same
     * intersection matrix.
     */
    @Test
    public void ehMeetBoundPostiveTest2() {

        System.out.println("ehMeet Bound Positive2");
        String expResult = "http://example.org/Geometry#PointB";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehMeet", "http://example.org/Geometry#PointB");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehMeet has same functionality with sfTouches since they have same
     * intersection matrix.
     */
    @Test
    public void ehMeetBoundNegativeTest() {

        System.out.println("ehMeet Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehMeet", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehMeet has same functionality with sfTouches since they have same
     * intersection matrix.
     */
    @Test
    public void ehMeetUnboundPostiveTest() {

        System.out.println("ehMeet Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geo:ehMeet");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehMeet has same functionality with sfTouches since they have same
     * intersection matrix.
     */
    @Test
    public void ehMeetUnboundNegativeTest() {

        System.out.println("ehMeet Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonK", "geo:ehMeet");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
