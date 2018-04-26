/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.query_rewrite_extension.simplefeatures_query_rewrite;

import static conformance_test.ConformanceTestSuite.*;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
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
public class SfIntersectsTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */

        infModel = initWktModel();
    }
    private static InfModel infModel;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void featureFeatureTest() {
        System.out.println("Feature Feature Test: ");
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#A");
        expResult.add("http://example.org/ApplicationSchema#AExactGeom");
        expResult.add("http://example.org/ApplicationSchema#B");
        expResult.add("http://example.org/ApplicationSchema#BExactGeom");
        expResult.add("http://example.org/ApplicationSchema#C");
        expResult.add("http://example.org/ApplicationSchema#CExactGeom");
        expResult.add("http://example.org/ApplicationSchema#D");
        expResult.add("http://example.org/ApplicationSchema#DExactGeom");
        expResult.add("http://example.org/ApplicationSchema#E");
        expResult.add("http://example.org/ApplicationSchema#EExactGeom");
        expResult.add("http://example.org/ApplicationSchema#F");
        expResult.add("http://example.org/ApplicationSchema#FExactGeom");
        expResult.add("http://example.org/ApplicationSchema#G");
        expResult.add("http://example.org/ApplicationSchema#GExactGeom");

        ArrayList<String> result = resourceQuery(featureFeatureQuery("ex:C", "geo:sfIntersects"), infModel);
        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);

    }

    @Test
    public void featureGeometryTest() {
        System.out.println("Feature Geometry Test: ");
        ArrayList<String> expResult = new ArrayList<>();

        expResult.add("http://example.org/ApplicationSchema#A");
        expResult.add("http://example.org/ApplicationSchema#AExactGeom");
        expResult.add("http://example.org/ApplicationSchema#B");
        expResult.add("http://example.org/ApplicationSchema#BExactGeom");
        expResult.add("http://example.org/ApplicationSchema#C");
        expResult.add("http://example.org/ApplicationSchema#CExactGeom");
        expResult.add("http://example.org/ApplicationSchema#D");
        expResult.add("http://example.org/ApplicationSchema#DExactGeom");
        expResult.add("http://example.org/ApplicationSchema#E");
        expResult.add("http://example.org/ApplicationSchema#EExactGeom");
        expResult.add("http://example.org/ApplicationSchema#F");
        expResult.add("http://example.org/ApplicationSchema#FExactGeom");
        expResult.add("http://example.org/ApplicationSchema#G");
        expResult.add("http://example.org/ApplicationSchema#GExactGeom");

        ArrayList<String> result = resourceQuery(featureGeometryQuery("ex:C", "geo:sfIntersects"), infModel);
        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);

    }

    @Test
    public void geometryFeatureTest() {
        System.out.println("Geometry Geometry Test: ");
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#A");
        expResult.add("http://example.org/ApplicationSchema#B");
        expResult.add("http://example.org/ApplicationSchema#C");
        expResult.add("http://example.org/ApplicationSchema#D");
        expResult.add("http://example.org/ApplicationSchema#E");
        expResult.add("http://example.org/ApplicationSchema#F");
        expResult.add("http://example.org/ApplicationSchema#G");

        ArrayList<String> result = resourceQuery(geometryFeatureQuery("ex:C", "geo:sfIntersects"), infModel);
        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);

    }

}
