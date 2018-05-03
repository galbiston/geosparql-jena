/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension.simple_features;

import conformance_test.geometry_topology_extension.*;
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
 * A.4.2.1 /conf/geometry-topology-extension/sf-query-functions
 *
 * Requirement: /req/geometry-topology-extension/sf-query-functions
 * Implementations shall support geof:sfEquals, geof:sfDisjoint,
 * geof:sfIntersects, geof:sfTouches, geof:sfCrosses, geof:sfWithin,
 * geof:sfContains, geof:sfOverlaps as SPARQL extension functions, consistent
 * with their corresponding DE-9IM intersection patterns, as defined by Simple
 * Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each of the
 * following functions returns the correct result for a test dataset when using
 * the specified serialization and version: geof:sfEquals, geof:sfDisjoint,
 * geof:sfIntersects, geof:sfTouches, geof:sfCrosses, geof:sfWithin,
 * geof:sfContains, geof:sfOverlaps.
 *
 * c.) Reference: Clause 9.3 Req 22
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
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#LineStringG", "geof:sfCrosses", "http://example.org/Geometry#LineStringE");

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
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#LineStringG", "geof:sfCrosses", "http://example.org/Geometry#LineStringF");

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
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#PolygonH");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringG", "geof:sfCrosses");

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
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#LineStringF", "geof:sfCrosses");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
