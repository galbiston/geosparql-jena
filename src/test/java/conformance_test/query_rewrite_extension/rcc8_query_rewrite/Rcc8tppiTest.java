/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.query_rewrite_extension.rcc8_query_rewrite;

import static conformance_test.ConformanceTestSuite.*;
import implementation.function_registry.RegistryLoader;
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
 * @author haozhechen
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
public class Rcc8tppiTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();
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
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#C");
        expectedList.add("http://example.org/ApplicationSchema#CExactGeom");

        ArrayList<String> actualList = resourceQuery(featureFeatureQuery("ex:D", "geo:rcc8tppi"), infModel);
        assertEquals(expectedList, actualList);

    }

    @Test
    public void featureGeometryTest() {
        System.out.println("Feature Geometry Test: ");
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#C");
        expectedList.add("http://example.org/ApplicationSchema#CExactGeom");

        ArrayList<String> actualList = resourceQuery(featureGeometryQuery("ex:D", "geo:rcc8tppi"), infModel);
        assertEquals(expectedList, actualList);

    }

    @Test
    public void geometryFeatureTest() {
        System.out.println("Geometry Geometry Test: ");
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#C");

        ArrayList<String> actualList = resourceQuery(geometryFeatureQuery("ex:D", "geo:rcc8tppi"), infModel);
        assertEquals(expectedList, actualList);

    }

}
