/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.query_rewrite_extension.simple_features;

import conformance_test.query_rewrite_extension.QueryRewriteTestMethods;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 *
 *
 * A.6.1.1 /conf/query-rewrite-extension/sf-query-rewrite
 *
 * Requirement: /req/query-rewrite-extension/sf-query-rewrite Basic graph
 * pattern matching shall use the semantics defined by the RIF Core Entailment
 * Regime [W3C SPARQL Entailment] for the RIF rules [W3C RIF Core]
 * geor:sfEquals, geor:sfDisjoint, geor:sfIntersects, geor:sfTouches,
 * geor:sfCrosses, geor:sfWithin, geor:sfContains, geor:sfOverlaps.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving the following query
 * transformation rules return the correct result for a test dataset when using
 * the specified serialization and version: geor:sfEquals, geor:sfDisjoint,
 * geor:sfIntersects, geor:sfTouches, geor:sfCrosses, geor:sfWithin,
 * geor:sfContains and geor:sfOverlaps.
 *
 * c.) Reference: Clause 11.2 Req 28
 *
 * d.) Test Type: Capabilities
 */
public class SfDisjointTest {

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
     * Geometries that do not have a point in common. A set of disconnected
     * geometries.
     */
    @Test
    public void sfDisjointBothBoundTest() {

        System.out.println("sfDisjoint Both Bound");
        String expResult = "http://example.org/Geometry#PointC";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfDisjoint", "http://example.org/Geometry#PointC");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Geometries that do not have a point in common. A set of disconnected
     * geometries.
     */
    @Test
    public void sfDisjointUnboundSubjectTest() {

        System.out.println("sfDisjoint Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#B");
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Feature#D");
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#Empty");
        expResult.add("http://example.org/Feature#F");
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Feature#K");
        expResult.add("http://example.org/Feature#L");
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringF");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PointB");
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");
        expResult.add("http://example.org/Geometry#PointEmpty");
        expResult.add("http://example.org/Geometry#PolygonI");
        expResult.add("http://example.org/Geometry#PolygonJ");
        expResult.add("http://example.org/Geometry#PolygonK");
        expResult.add("http://example.org/Geometry#PolygonL");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:sfDisjoint", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Geometries that do not have a point in common. A set of disconnected
     * geometries.
     */
    @Test
    public void sfDisjointUnboundObjectTest() {

        System.out.println("sfDisjoint Unbound Object");
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

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PolygonH", "geo:sfDisjoint");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Geometries that do not have a point in common. A set of disconnected
     * geometries.
     */
    @Ignore
    @Test
    public void sfDisjointBothUnboundTest() {

        System.out.println("sfDisjoint Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:sfDisjoint");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Geometries that do not have a point in common. A set of disconnected
     * geometries.
     */
    @Test
    public void sfDisjointAssertTest() {

        System.out.println("sfDisjoint Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:sfDisjoint");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
