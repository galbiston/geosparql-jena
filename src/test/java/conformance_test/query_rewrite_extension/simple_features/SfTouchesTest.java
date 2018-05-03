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
public class SfTouchesTest {

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
    public void sfTouchesBothBoundTest() {

        System.out.println("sfTouches Both Bound");
        String expResult = "http://example.org/Geometry#PolygonJ";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PolygonI", "geo:sfTouches", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void sfTouchesUnboundSubjectTest() {

        System.out.println("sfTouches Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PolygonJ");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:sfTouches", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void sfTouchesUnboundObjectTest() {

        System.out.println("sfTouches Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PolygonJ", "geo:sfTouches");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Ignore
    @Test
    public void sfTouchesBothUnboundTest() {

        System.out.println("sfTouches Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:sfTouches");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Touch returns t (TRUE) if none of the points common to both geometries
     * intersect the interiors of both geometries, At least one geometry must be
     * a line string, polygon, multi line string, or multi polygon.
     */
    @Test
    public void sfTouchesAssertTest() {

        System.out.println("sfTouches Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:sfTouches");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
