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
public class SfEqualsTest {

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
    public void sfEqualsBoundPostiveTest() {

        System.out.println("sfEquals Bound Positive");
        String expResult = "http://example.org/Geometry#PointC2";
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PointC", "geof:sfEquals", "http://example.org/Geometry#PointC2");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void sfEqualsBoundNegativeTest() {

        System.out.println("sfEquals Bound Negative");
        String expResult = null;
        String result = FilterTestMethods.runBoundQuery("http://example.org/Geometry#PointC", "geof:sfEquals", "http://example.org/Geometry#PointA");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void sfEqualsUnboundPostiveTest() {

        System.out.println("sfEquals Unbound Positive");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PointC", "geof:sfEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Equal returns t (TRUE) if two geometries of the same type have identical
     * X,Y coordinate values.
     */
    @Test
    public void sfEqualsUnboundNegativeTest() {

        System.out.println("sfEquals Unbound Negative");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonJ");
        List<String> result = FilterTestMethods.runUnboundQuery("http://example.org/Geometry#PolygonJ", "geof:sfEquals");

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
