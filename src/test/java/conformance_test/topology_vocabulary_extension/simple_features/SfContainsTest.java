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
public class SfContainsTest {

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
     * Contains returns t (TRUE) if the second geometry is completely contained
     * by the first geometry.
     */
    @Test
    public void sfContainsBoundPostiveTest() {

        System.out.println("sfContains Bound Positive");
        String expResult = "http://example.org/Geometry#PointA";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfContains", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Contains returns t (TRUE) if the second geometry is completely contained
     * by the first geometry.
     */
    @Test
    public void sfContainsBoundPostiveTest2() {

        System.out.println("sfContains Bound Positive2");
        String expResult = "http://example.org/Geometry#PolygonL";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonJ", "geo:sfContains", "http://example.org/Geometry#PolygonL");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Contains returns t (TRUE) if the second geometry is completely contained
     * by the first geometry. No results expected.
     */
    @Test
    public void sfContainsBoundNegativeTest() {

        System.out.println("sfContains Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfContains", "http://example.org/Geometry#PointC");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Contains returns t (TRUE) if the second geometry is completely contained
     * by the first geometry.
     */
    @Test
    public void sfContainsUnboundPostiveTest() {

        System.out.println("sfContains Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");
        expResult.add("http://example.org/Feature#D");
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Feature#K");
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#PointA");
        expResult.add("http://example.org/Geometry#PolygonH");
        expResult.add("http://example.org/Geometry#PolygonK");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonH", "geo:sfContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Contains returns t (TRUE) if the second geometry is completely contained
     * by the first geometry. Only itself should be returned.
     */
    @Test
    public void sfContainsUnboundNegativeTest() {

        System.out.println("sfContains Unbound Negative");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonI", "geo:sfContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
