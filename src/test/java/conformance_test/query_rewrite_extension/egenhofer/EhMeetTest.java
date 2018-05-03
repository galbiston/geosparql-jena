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
public class EhMeetTest {

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
     * ehMeet has same functionality with ehMeet since they have same
     * intersection matrix.
     */
    @Test
    public void ehMeetBothBoundTest() {

        System.out.println("ehMeet Both Bound");
        String expResult = "http://example.org/Geometry#PolygonJ";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PolygonI", "geo:ehMeet", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehMeet has same functionality with ehMeet since they have same
     * intersection matrix.
     */
    @Test
    public void ehMeetUnboundSubjectTest() {

        System.out.println("ehMeet Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PolygonJ");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:ehMeet", "http://example.org/Geometry#PolygonI");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehMeet has same functionality with ehMeet since they have same
     * intersection matrix.
     */
    @Test
    public void ehMeetUnboundObjectTest() {

        System.out.println("ehMeet Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PolygonJ", "geo:ehMeet");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehMeet has same functionality with ehMeet since they have same
     * intersection matrix.
     */
    @Ignore
    @Test
    public void ehMeetBothUnboundTest() {

        System.out.println("ehMeet Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:ehMeet");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehMeet has same functionality with ehMeet since they have same
     * intersection matrix.
     */
    @Test
    public void ehMeetAssertTest() {

        System.out.println("ehMeet Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:ehMeet");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
