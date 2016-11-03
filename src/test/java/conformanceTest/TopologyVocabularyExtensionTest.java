/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import static conformanceTest.ConformanceTestSuite.initModels;
import implementation.functionregistry.RegistryLoader;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A.2Conformance Class: Topology Vocabulary Extension (relation_family)
 * Conformance Class URI: /conf/topology-vocab-extension.
 */
public class TopologyVocabularyExtensionTest {

    public TopologyVocabularyExtensionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the GeoSPARQL functionalities
         */
        RegistryLoader.load();

        /**
         * Initialize all the models
         */
        initModels();
    }

    /**
     * A.2.1 relation_family = Simple Features A.2.1.1
     * /conf/topology-vocab-extension/sf-spatial-relations
     *
     * Requirement: /req/topology-vocab-extension/sf-spatial-relations
     * Implementations shall allow the properties geo:sfEquals, geo:sfDisjoint,
     * geo:sfIntersects, geo:sfTouches, geo:sfCrosses, geo:sfWithin,
     * geo:sfContains, geo:sfOverlaps to be used in SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that queries involving these properties return
     * the correct result for a test dataset.
     *
     * c.) Reference: Clause 7.2 Req 4
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void sfSpatialRelationsTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.sfSpatialRelationsTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.2.2 relation_family = Egenhofer A.2.2.1
     * /conf/topology-vocab-extension/eh-spatial-relations
     *
     * Requirement: /req/topology-vocab-extension/eh-spatial-relations
     * Implementations shall allow the properties geo:ehEquals, geo:ehDisjoint,
     * geo:ehMeet, geo:ehOverlap, geo:ehCovers, geo:ehCoveredBy, geo:ehInside,
     * geo:ehContains to be used in SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that queries involving these properties return
     * the correct result for a test dataset.
     *
     * c.) Reference: Clause 7.3 Req 5
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void ehSpatialRelationsTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.ehSpatialRelationsTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.2.3 relation_family = RCC8 A.2.3.1
     * /conf/topology-vocab-extension/rcc8-spatial-relations
     *
     * Requirement: /req/topology-vocab-extension/rcc8-spatial-relations
     * Implementations shall allow the properties geo:rcc8eq, geo:rcc8dc,
     * geo:rcc8ec, geo:rcc8po, geo:rcc8tppi, geo:rcc8tpp, geo:rcc8ntpp,
     * geo:rcc8ntppi to be used in SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that queries involving these properties return
     * the correct result for a test dataset
     *
     * c.) Reference: Clause 7.4 Req 6
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void rcc8SpatialRelationsTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.rcc8SpatialRelationsTestQueries(), INF_WKT_MODEL);

    }
}
