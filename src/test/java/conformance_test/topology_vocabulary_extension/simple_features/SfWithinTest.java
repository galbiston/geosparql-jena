/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.simple_features;

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
 * A.2.1.1 /conf/topology-vocab-extension/sf-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/sf-spatial-relations
 * Implementations shall allow the properties geo:sfEquals, geo:sfDisjoint,
 * geo:sfIntersects, geo:sfTouches, geo:sfCrosses, geo:sfWithin, geo:sfContains,
 * geo:sfOverlaps to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 7.2 Req 4
 *
 * d.) Test Type: Capabilities
 */
public class SfWithinTest {

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
     * Within returns t (TRUE) if the first geometry is completely within the
     * second geometry, Within tests for the exact opposite result of contains.
     */
    @Test
    public void sfWithinBoundPostiveTest() {

        System.out.println("sfWithin Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonJ";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonL", "geo:sfWithin", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Within returns t (TRUE) if the first geometry is completely within the
     * second geometry, Within tests for the exact opposite result of contains.
     */
    @Test
    public void sfWithinBoundNegativeTest() {

        System.out.println("sfWithin Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfWithin", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Within returns t (TRUE) if the first geometry is completely within the
     * second geometry, Within tests for the exact opposite result of contains.
     */
    @Test
    public void sfWithinUnboundPostiveTest() {

        System.out.println("sfContains Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Feature#L");
        expResult.add("http://example.org/Geometry#PolygonJ");
        expResult.add("http://example.org/Geometry#PolygonL");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonL", "geo:sfWithin");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Within returns t (TRUE) if the first geometry is completely within the
     * second geometry, Within tests for the exact opposite result of contains.
     */
    @Test
    public void sfWithinUnboundNegativeTest() {

        System.out.println("sfWithin Unbound Negative");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geo:sfWithin");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
