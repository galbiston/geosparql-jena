/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.query_rewrite_extension.egenhofer_query_rewrite;

import static conformance_test.ConformanceTestSuite.*;
import java.util.ArrayList;
import java.util.List;
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
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#CExactGeom");
        expResult.add("http://example.org/ApplicationSchema#C");

        List<String> result = queryMany(featureFeatureQuery("ex:A", "geo:ehContains"), infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void featureGeometryTest() {
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#CExactGeom");
        expResult.add("http://example.org/ApplicationSchema#C");

        List<String> result = queryMany(featureGeometryQuery("ex:A", "geo:ehContains"), infModel);
        assertEquals(expResult, result);

    }

    @Test
    public void geometryFeatureTest() {
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#C");

        List<String> result = queryMany(geometryFeatureQuery("ex:A", "geo:ehContains"), infModel);
        assertEquals(expResult, result);
    }

}
