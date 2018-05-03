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
public class SfOverlapsTest {

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
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Test
    public void sfOverlapsBothBoundTest() {

        System.out.println("sfOverlaps Both Bound");
        String expResult = "http://example.org/Geometry#PolygonI";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PolygonH", "geo:sfOverlaps", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Test
    public void sfOverlapsUnboundSubjectTest() {

        System.out.println("sfOverlaps Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Geometry#PolygonH");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:sfOverlaps", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Test
    public void sfOverlapsUnboundObjectTest() {

        System.out.println("sfOverlaps Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PolygonH", "geo:sfOverlaps");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Ignore
    @Test
    public void sfOverlapsBothUnboundTest() {

        System.out.println("sfOverlaps Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:sfOverlaps");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Overlap compares two geometries of the same dimension and returns t
     * (TRUE) if their intersection set results in a geometry different from
     * both but of the same dimension.
     */
    @Test
    public void sfOverlapsAssertTest() {

        System.out.println("sfOverlaps Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:sfOverlaps");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
