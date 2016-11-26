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
 * A.6.3.1 /conf/query-rewrite-extension/rcc8-query-rewrite
 *
 * Requirement: /req/query-rewrite-extension/rcc8-query-rewrite
 * Basic graph pattern matching shall use the semantics defined by the
 * RIF Core Entailment Regime [W3C SPARQL Entailment] for the RIF rules
 * [W3C RIF Core] geor:rcc8eq, geor:rcc8dc, geor:rcc8ec, geor:rcc8po,
 * geor:rcc8tppi, geor:rcc8tpp, geor:rcc8ntpp, geor:rcc8ntppi.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving the following query
 * transformation rules return the correct result for a test dataset
 * when using the specified serialization and version: geor:rcc8eq,
 * geor:rcc8dc, geor:rcc8ec, geor:rcc8po, geor:rcc8tppi, geor:rcc8tpp,
 * geor:rcc8ntpp, geor:rcc8ntppi.
 *
 * c.) Reference: Clause 11.4 Req 30
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8QueryRewriteRcc8poTest {

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
        System.out.println("Feature Feature Test: ");
        this.expectedList.add("http://example.org/ApplicationSchema#C");
        this.expectedList.add("http://example.org/ApplicationSchema#CExactGeom");

        this.actualList = resourceQuery(featureFeatureQueryRewriteQuery("ex:F", "geo:rcc8po"), INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);

    }

    @Test
    public void featureGeometryTest() {
        System.out.println("Feature Geometry Test: ");
        this.expectedList.add("http://example.org/ApplicationSchema#C");
        this.expectedList.add("http://example.org/ApplicationSchema#CExactGeom");

        this.actualList = resourceQuery(featureGeometryQueryRewriteQuery("ex:F", "geo:rcc8po"), INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);

    }

    @Test
    public void geometryFeatureTest() {
        System.out.println("Geometry Geometry Test: ");
        this.expectedList.add("http://example.org/ApplicationSchema#C");

        this.actualList = resourceQuery(geometryFeatureQueryRewriteQuery("ex:F", "geo:rcc8po"), INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);

    }

}
