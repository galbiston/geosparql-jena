/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.queryrewrite.egenhofer;

import static conformanceTest.ConformanceTestSuite.*;
import implementation.functionregistry.RegistryLoader;
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
 * @author haozhechen
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
public class EhQueryRewriteCoversTest {

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
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#C");
        expectedList.add("http://example.org/ApplicationSchema#CExactGeom");

        ArrayList<String> actualList = resourceQuery(featureFeatureQueryRewriteQuery("ex:D", "geo:ehCovers"), infModel);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void featureGeometryTest() {
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#C");
        expectedList.add("http://example.org/ApplicationSchema#CExactGeom");

        ArrayList<String> actualList = resourceQuery(featureGeometryQueryRewriteQuery("ex:D", "geo:ehCovers"), infModel);
        assertEquals(expectedList, actualList);

    }

    @Test
    public void geometryFeatureTest() {
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#C");

        ArrayList<String> actualList = resourceQuery(geometryFeatureQueryRewriteQuery("ex:D", "geo:ehCovers"), infModel);
        assertEquals(expectedList, actualList);
    }

}
