/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.query_rewrite_extension.egenhofer;

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
 * A.6.2.1 /conf/query-rewrite-extension/eh-query-rewrite
 *
 * Requirement: /req/query-rewrite-extension/eh-query-rewrite Basic graph
 * pattern matching shall use the semantics defined by the RIF Core Entailment
 * Regime [W3C SPARQL Entailment] for the RIF rules [W3C RIF Core]
 * geor:ehEquals, geor:ehDisjoint, geor:ehMeet, geor:ehOverlap, geor:ehCovers,
 * geor:ehCoveredBy, geor:ehInside, geor:ehContains.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving the following query
 * transformation rules return the correct result for a test dataset when using
 * the specified serialization and version: geor:ehEquals, geor:ehDisjoint,
 * geor:ehMeet, geor:ehOverlap, geor:ehCovers, geor:ehCoveredBy, geor:ehInside,
 * geor:ehContains.
 *
 * c.) Reference: Clause 11.3 Req 29
 *
 * d.) Test Type: Capabilities
 */
public class EhOverlapTest {

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
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Test
    public void ehOverlapBothBoundTest() {

        System.out.println("ehOverlap Both Bound");
        String expResult = "http://example.org/Geometry#PolygonI";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehOverlap", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Test
    public void ehOverlapUnboundSubjectTest() {

        System.out.println("ehOverlap Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Geometry#PolygonH");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:ehOverlap", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Test
    public void ehOverlapUnboundObjectTest() {

        System.out.println("ehOverlap Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PolygonI");

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PolygonH", "geo:ehOverlap");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Ignore
    @Test
    public void ehOverlapBothUnboundTest() {

        System.out.println("ehOverlap Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:ehOverlap");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares two
     * geometries of the same dimension and returns TRUE if their intersection
     * set results in a geometry different from both but of the same dimension.
     * However, ehOverlap does not necessarily require the intersection of the
     * two geometries to be exact same dimension, therefore ehOverlap's
     * functionality can be seen as a combination of sfCrosses and sfOverlaps.
     */
    @Test
    public void ehOverlapAssertTest() {

        System.out.println("ehOverlap Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:ehOverlap");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
