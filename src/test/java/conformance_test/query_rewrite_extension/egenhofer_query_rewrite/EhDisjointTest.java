/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.query_rewrite_extension.egenhofer_query_rewrite;

import static conformance_test.ConformanceTestSuite.*;
import implementation.GeoSPARQLSupport;

import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
public class EhDisjointTest {

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
        ArrayList<String> result = resourceQuery(featureFeatureQuery("ex:C", "geo:ehDisjoint"), infModel);
        assertEquals(expResult, result);

    }

    @Test
    public void featureGeometryTest() {
        System.out.println("Feature Geometry Test: ");

        ArrayList<String> expResult = new ArrayList<>();
        ArrayList<String> result = resourceQuery(featureGeometryQuery("ex:C", "geo:ehDisjoint"), infModel);
        assertEquals(expResult, result);

    }

    @Test
    public void geometryFeatureTest() {
        System.out.println("Geometry Geometry Test: ");

        ArrayList<String> expResult = new ArrayList<>();
        ArrayList<String> result = resourceQuery(geometryFeatureQuery("ex:C", "geo:ehDisjoint"), infModel);
        assertEquals(expResult, result);

    }

}
