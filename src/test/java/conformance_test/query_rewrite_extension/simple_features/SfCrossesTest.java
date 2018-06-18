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
public class SfCrossesTest {

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
     * Crosses returns t (TRUE) if the intersection results in a geometry whose
     * dimension is one less than the maximum dimension of the two source
     * geometries and the intersection set is interior to both source
     * geometries, Cross returns t (TRUE) for only multipoint/polygon,
     * multipoint/linestring, linestring/linestring, linestring/polygon, and
     * linestring/multipolygon comparisons.
     */
    @Test
    public void sfCrossesBothBoundTest() {

        System.out.println("sfCrosses Both Bound");
        String expResult = "http://example.org/Geometry#LineStringE";
        String result = QueryRewriteTestMethods.runBothBoundQuery("http://example.org/Geometry#LineStringG", "geo:sfCrosses", "http://example.org/Geometry#LineStringE");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Crosses returns t (TRUE) if the intersection results in a geometry whose
     * dimension is one less than the maximum dimension of the two source
     * geometries and the intersection set is interior to both source
     * geometries, Cross returns t (TRUE) for only multipoint/polygon,
     * multipoint/linestring, linestring/linestring, linestring/polygon, and
     * linestring/multipolygon comparisons.
     */
    @Test
    public void sfCrossesUnboundSubjectTest() {

        System.out.println("sfCrosses Unbound Subject");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Geometry#LineStringG");

        List<String> result = QueryRewriteTestMethods.runUnboundSubjectQuery("geo:sfCrosses", "http://example.org/Geometry#LineStringE");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Crosses returns t (TRUE) if the intersection results in a geometry whose
     * dimension is one less than the maximum dimension of the two source
     * geometries and the intersection set is interior to both source
     * geometries, Cross returns t (TRUE) for only multipoint/polygon,
     * multipoint/linestring, linestring/linestring, linestring/polygon, and
     * linestring/multipolygon comparisons.
     */
    @Test
    public void sfCrossesUnboundObjectTest() {

        System.out.println("sfCrosses Unbound Object");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#PolygonH");

        List<String> result = QueryRewriteTestMethods.runUnboundObjectQuery("http://example.org/Geometry#LineStringG", "geo:sfCrosses");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Crosses returns t (TRUE) if the intersection results in a geometry whose
     * dimension is one less than the maximum dimension of the two source
     * geometries and the intersection set is interior to both source
     * geometries, Cross returns t (TRUE) for only multipoint/polygon,
     * multipoint/linestring, linestring/linestring, linestring/polygon, and
     * linestring/multipolygon comparisons.
     */
    @Ignore
    @Test
    public void sfCrossesBothUnboundTest() {

        System.out.println("sfCrosses Both Unbound");
        List<String> expResult = new ArrayList<>();

        List<String> result = QueryRewriteTestMethods.runBothUnboundQuery("geo:sfCrosses");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Crosses returns t (TRUE) if the intersection results in a geometry whose
     * dimension is one less than the maximum dimension of the two source
     * geometries and the intersection set is interior to both source
     * geometries, Cross returns t (TRUE) for only multipoint/polygon,
     * multipoint/linestring, linestring/linestring, linestring/polygon, and
     * linestring/multipolygon comparisons.
     */
    @Test
    public void sfCrossesAssertTest() {

        System.out.println("sfCrosses Assert Test");

        Boolean expResult = true;
        Boolean result = QueryRewriteTestMethods.runAssertQuery("geo:sfCrosses");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
