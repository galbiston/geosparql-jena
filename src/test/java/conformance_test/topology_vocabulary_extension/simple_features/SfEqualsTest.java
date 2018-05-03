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
public class SfEqualsTest {

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
    public void sfEqualsBoundPostiveTest() {

        System.out.println("sfEquals Bound Positive");
        String expResult = "http://example.org/Geometry#PointC2";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PointC", "geo:sfEquals", "http://example.org/Geometry#PointC2");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void sfEqualsBoundNegativeTest() {

        System.out.println("sfEquals Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PointC", "geo:sfEquals", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void sfEqualsUnboundPostiveTest() {

        System.out.println("sfEquals Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PointC", "geo:sfEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void sfEqualsUnboundNegativeTest() {

        System.out.println("sfEquals Unbound Negative");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Geometry#PolygonJ");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geo:sfEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
