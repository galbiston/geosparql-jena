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
public class EhCoversTest {

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
     * Every point of b is a point of a, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void ehCoversBothBoundTest() {

        System.out.println("ehCovers Both Bound");
        String expResult = "http://example.org/Geometry#PolygonK";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehCovers", "http://example.org/Geometry#PolygonK");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of b is a point of a, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void ehCoversUnboundSubjectTest() {

        System.out.println("ehCovers Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Geometry#PolygonH");
        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:ehCovers", "http://example.org/Geometry#PolygonK");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of b is a point of a, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void ehCoversUnboundObjectTest() {

        System.out.println("ehCovers Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#K");
        expResult.add("http://example.org/Geometry#PolygonK");
        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PolygonH", "geo:ehCovers");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of b is a point of a, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Ignore
    @Test
    public void ehCoversBothUnboundTest() {

        System.out.println("ehCovers Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:ehCovers");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Every point of b is a point of a, and the interiors of the two geometries
     * have at least one point in common.
     */
    @Test
    public void ehCoversAssertTest() {

        System.out.println("ehCovers Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:ehCovers");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
