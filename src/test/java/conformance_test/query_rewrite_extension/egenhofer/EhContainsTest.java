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
public class EhContainsTest {

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
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsBothBoundTest() {

        System.out.println("ehContains Both Bound");
        String expResult = "http://example.org/Geometry#PointA";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PolygonH", "geo:ehContains", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsUnboundSubjectTest() {

        System.out.println("ehContains Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Geometry#PolygonH");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:ehContains", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsUnboundObjectTest() {

        System.out.println("ehContains Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");
        expResult.add("http://example.org/Feature#D");
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#PointA");

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PolygonH", "geo:ehContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Ignore
    @Test
    public void ehContainsBothUnboundTest() {

        System.out.println("ehContains Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:ehContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * ehContains is slightly different from the sfContains, which will not
     * return the same instance while the sfContains will return the same
     * instance.
     */
    @Test
    public void ehContainsAssertTest() {

        System.out.println("ehContains Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:ehContains");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
