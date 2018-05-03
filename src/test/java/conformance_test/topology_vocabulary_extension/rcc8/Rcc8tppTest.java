/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.rcc8;

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
 * A.2.3.1 /conf/topology-vocab-extension/rcc8-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/rcc8-spatial-relations
 * Implementations shall allow the properties geo:rcc8eq, geo:rcc8dc,
 * geo:rcc8ec, geo:rcc8po, geo:rcc8tppi, geo:rcc8tpp, geo:rcc8ntpp,
 * geo:rcc8ntppi to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset
 *
 * c.) Reference: Clause 7.4 Req 6
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8tppTest {

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
    public void rcc8ntppBoundPostiveTest() {

        System.out.println("rcc8ntpp Bound Positive");
        String expResult = "http://example.org/Geometry#PolygonH";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonK", "geo:rcc8tpp", "http://example.org/Geometry#PolygonH");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of a is a point of b, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void rcc8ntppBoundNegativeTest() {

        System.out.println("rcc8ntpp Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:rcc8tpp", "http://example.org/Geometry#PolygonL");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of a is a point of b, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void rcc8ntppUnboundPostiveTest() {

        System.out.println("rcc8ntpp Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Geometry#PolygonH");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonK", "geo:rcc8tpp");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of a is a point of b, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void rcc8ntppUnboundNegativeTest() {

        System.out.println("rcc8ntpp Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geo:rcc8tpp");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
