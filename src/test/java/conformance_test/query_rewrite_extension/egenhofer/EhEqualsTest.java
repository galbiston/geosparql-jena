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
public class EhEqualsTest {

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
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void ehEqualsBothBoundTest() {

        System.out.println("ehEquals Both Bound");
        String expResult = "http://example.org/Geometry#PointC2";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#PointC", "geo:ehEquals", "http://example.org/Geometry#PointC2");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void ehEqualsUnboundSubjectTest() {

        System.out.println("ehEquals Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:ehEquals", "http://example.org/Geometry#PointC");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void ehEqualsUnboundObjectTest() {

        System.out.println("ehEquals Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#PointC", "geo:ehEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Ignore
    @Test
    public void ehEqualsBothUnboundTest() {

        System.out.println("ehEquals Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:ehEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void ehEqualsAssertTest() {

        System.out.println("ehEquals Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:ehEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
