/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.queryrewrite;

import static conformanceTest.ConformanceTestSuite.*;
import static implementation.functionregistry.RegistryLoader.load;
import java.util.ArrayList;
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
 * Requirement: /req/query-rewrite-extension/eh-query-rewrite
 * Basic graph pattern matching shall use the semantics defined by the
 * RIF Core Entailment Regime [W3C SPARQL Entailment] for the RIF rules
 * [W3C RIF Core] geor:ehEquals, geor:ehDisjoint, geor:ehMeet,
 * geor:ehOverlap, geor:ehCovers, geor:ehCoveredBy, geor:ehInside,
 * geor:ehContains.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving the following query
 * transformation rules return the correct result for a test dataset
 * when using the specified serialization and version: geor:ehEquals,
 * geor:ehDisjoint, geor:ehMeet, geor:ehOverlap, geor:ehCovers,
 * geor:ehCoveredBy, geor:ehInside, geor:ehContains.
 *
 * c.) Reference: Clause 11.3 Req 29
 *
 * d.) Test Type: Capabilities
 */
public class EhQueryRewriteCoveredByTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
        initWktModel();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private ArrayList expectedList;
    private ArrayList actualList;

    @Before
    public void setUp() {
        this.expectedList = new ArrayList<>();
        this.actualList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        this.actualList.clear();
        this.expectedList.clear();
    }

    @Test
    public void featureFeatureTest() {
        this.expectedList.add("http://example.org/ApplicationSchema#D");
        this.expectedList.add("http://example.org/ApplicationSchema#DExactGeom");

        this.actualList = resourceQuery(featureFeatureQueryRewriteQuery("ex:C", "geo:ehCoveredBy"), INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

    @Test
    public void featureGeometryTest() {
        this.expectedList.add("http://example.org/ApplicationSchema#D");
        this.expectedList.add("http://example.org/ApplicationSchema#DExactGeom");

        this.actualList = resourceQuery(featureGeometryQueryRewriteQuery("ex:C", "geo:ehCoveredBy"), INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);

    }

    @Test
    public void geometryFeatureTest() {
        this.expectedList.add("http://example.org/ApplicationSchema#D");

        this.actualList = resourceQuery(geometryFeatureQueryRewriteQuery("ex:C", "geo:ehCoveredBy"), INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
