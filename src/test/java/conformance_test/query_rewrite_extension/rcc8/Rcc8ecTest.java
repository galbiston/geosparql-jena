/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.query_rewrite_extension.rcc8;

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
 * A.6.3.1 /conf/query-rewrite-extension/rcc8-query-rewrite
 *
 * Requirement: /req/query-rewrite-extension/rcc8-query-rewrite Basic graph
 * pattern matching shall use the semantics defined by the RIF Core Entailment
 * Regime [W3C SPARQL Entailment] for the RIF rules [W3C RIF Core] geor:rcc8eq,
 * geor:rcc8dc, geor:rcc8ec, geor:rcc8po, geor:rcc8tppi, geor:rcc8tpp,
 * geor:rcc8ntpp, geor:rcc8ntppi.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving the following query
 * transformation rules return the correct result for a test dataset when using
 * the specified serialization and version: geor:rcc8eq, geor:rcc8dc,
 * geor:rcc8ec, geor:rcc8po, geor:rcc8tppi, geor:rcc8tpp, geor:rcc8ntpp,
 * geor:rcc8ntppi.
 *
 * c.) Reference: Clause 11.4 Req 30
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8ecTest {

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
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Test
    public void rcc8ecBothBoundTest() {

        System.out.println("rcc8ec Both Bound");
        String expResult = "http://example.org/Geometry#PolygonJ";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PolygonI", "geo:rcc8ec", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Test
    public void rcc8ecUnboundSubjectTest() {

        System.out.println("rcc8ec Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Geometry#PolygonI");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:rcc8ec", "http://example.org/Geometry#PolygonJ");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Test
    public void rcc8ecUnboundObjectTest() {

        System.out.println("rcc8ec Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Geometry#PolygonJ");

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PolygonI", "geo:rcc8ec");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Ignore
    @Test
    public void rcc8ecBothUnboundTest() {

        System.out.println("rcc8ec Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:rcc8ec");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * rcc8ec has similar functionality with sfTouches and ehMeet.
     */
    @Test
    public void rcc8ecAssertTest() {

        System.out.println("rcc8ec Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:rcc8ec");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
