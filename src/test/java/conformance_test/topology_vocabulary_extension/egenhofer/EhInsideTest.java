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
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonL", "geo:ehInside", "http://example.org/Geometry#PolygonJ");

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
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehInside", "http://example.org/Geometry#PolygonI");

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
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Geometry#PolygonJ");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonL", "geo:ehInside");

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
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geo:ehInside");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
