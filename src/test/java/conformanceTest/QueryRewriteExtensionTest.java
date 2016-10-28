/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import static conformanceTest.ConformanceTestSuite.initModels;
import static main.Main.init;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A.6Conformance Class: Query Rewrite Extension (relation_family,
 * serialization, version)
 * Conformance Class URI: /conf/query-rewrite-extension.
 */
public class QueryRewriteExtensionTest {

    public QueryRewriteExtensionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the GeoSPARQL functionalities
         */
        init();

        /**
         * Initialize all the models
         */
        initModels();
    }

    /**
     * A.6.1 relation_family = Simple Features
     * A.6.1.1 /conf/query-rewrite-extension/sf-query-rewrite
     *
     * Requirement: /req/query-rewrite-extension/sf-query-rewrite
     * Basic graph pattern matching shall use the semantics defined by the
     * RIF Core Entailment Regime [W3C SPARQL Entailment] for the RIF rules
     * [W3C RIF Core] geor:sfEquals, geor:sfDisjoint, geor:sfIntersects,
     * geor:sfTouches, geor:sfCrosses, geor:sfWithin, geor:sfContains,
     * geor:sfOverlaps.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that queries involving the following query
     * transformation rules return the correct result for a test dataset
     * when using the specified serialization and version: geor:sfEquals,
     * geor:sfDisjoint, geor:sfIntersects, geor:sfTouches, geor:sfCrosses,
     * geor:sfWithin, geor:sfContains and geor:sfOverlaps.
     *
     * c.) Reference: Clause 11.2 Req 28
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void sfQueryRewriteTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.sfQueryRewriteTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.6.2 relation_family = Egenhofer
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
    @Test
    public void ehQueryRewriteTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.ehQueryRewriteTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.6.3 relation_family = RCC8
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
     * c.) Reference: Clause 11.4 Req 30 d.) Test Type: Capabilities
     */
    @Test
    public void rcc8QueryRewriteTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.rcc8QueryRewriteTestQueries(), INF_WKT_MODEL);

    }
}
