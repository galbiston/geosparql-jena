/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.simple_features;

import conformance_test.topology_vocabulary_extension.PropertyTestMethods;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 *
 * A.2.1.1 /conf/topology-vocab-extension/sf-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/sf-spatial-relations
 * Implementations shall allow the properties geo:sfEquals, geo:sfDisjoint,
 * geo:sfIntersects, geo:sfTouches, geo:sfCrosses, geo:sfWithin, geo:sfContains,
 * geo:sfOverlaps to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 7.2 Req 4
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
    public void sfCrossesBoundPostiveTest() {

        System.out.println("sfCrosses Bound Positive");
        String expResult = "http://example.org/Geometry#LineStringE";
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#LineStringG", "geo:sfCrosses", "http://example.org/Geometry#LineStringE");

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
    public void sfCrossesBoundNegativeTest() {

        System.out.println("sfCrosses Bound Negative");
        String expResult = null;
        String result = PropertyTestMethods.runBoundQuery("http://example.org/Geometry#LineStringG", "geo:sfCrosses", "http://example.org/Geometry#LineStringF");

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
    public void sfCrossesUnboundPostiveTest() {

        System.out.println("sfCrosses Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#PolygonH");
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringG", "geo:sfCrosses");

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
    public void sfCrossesUnboundNegativeTest() {

        System.out.println("sfCrosses Unbound Negative");
        List<String> expResult = new ArrayList<>();
        List<String> result = PropertyTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringF", "geo:sfCrosses");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
