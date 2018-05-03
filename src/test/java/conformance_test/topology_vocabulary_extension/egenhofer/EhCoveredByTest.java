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
public class EhCoveredByTest {

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
     * Every point of a is a point of b, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void ehCoveredByBoundPostiveTest() {

        System.out.println("ehCoveredBy Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonH";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonK", "geo:ehCoveredBy", "http://example.org/Geometry#PolygonH");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of a is a point of b, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void ehCoveredByBoundNegativeTest() {

        System.out.println("ehCoveredBy Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehCoveredBy", "http://example.org/Geometry#PointC");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of a is a point of b, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void ehCoveredByUnboundPostiveTest() {

        System.out.println("ehCoveredBy Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Geometry#PolygonH");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonK", "geo:ehCoveredBy");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of a is a point of b, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void ehCoveredByUnboundNegativeTest() {

        System.out.println("ehCoveredBy Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geo:ehCoveredBy");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
