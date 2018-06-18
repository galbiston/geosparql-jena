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
public class EhDisjointTest {

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
     * Disjoint returns t (TRUE) if the intersection of the two geometries is an
     * empty set.
     */
    @Test
    public void ehDisjointBoundPostiveTest() {

        System.out.println("ehDisjoint Bound Positive");
        String expResult = "http://example.org/Geometry#PointC";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehDisjoint", "http://example.org/Geometry#PointC");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Disjoint returns t (TRUE) if the intersection of the two geometries is an
     * empty set.
     */
    @Test
    public void ehDisjointBoundNegativeTest() {

        System.out.println("ehDisjoint Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehDisjoint", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Disjoint returns t (TRUE) if the intersection of the two geometries is an
     * empty set.
     */
    @Test
    public void ehDisjointUnboundPostiveTest() {

        System.out.println("ehDisjoint Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Feature#Empty");
        expResult.add("http://example.org/Feature#F");
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Feature#L");
        expResult.add("http://example.org/Geometry#LineStringF");
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");
        expResult.add("http://example.org/Geometry#PointEmpty");
        expResult.add("http://example.org/Geometry#PolygonJ");
        expResult.add("http://example.org/Geometry#PolygonL");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonH", "geo:ehDisjoint");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Disjoint returns t (TRUE) if the intersection of the two geometries is an
     * empty set.
     */
    @Test
    public void ehDisjointUnboundNegativeTest() {

        System.out.println("ehDisjoint Unbound Negative");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");
        expResult.add("http://example.org/Feature#B");
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Feature#D");
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#Empty");
        expResult.add("http://example.org/Feature#F");
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Feature#K");
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringF");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PointA");
        expResult.add("http://example.org/Geometry#PointB");
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");
        expResult.add("http://example.org/Geometry#PointEmpty");
        expResult.add("http://example.org/Geometry#PolygonH");
        expResult.add("http://example.org/Geometry#PolygonK");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geo:ehDisjoint");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
