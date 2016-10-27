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
 * A.4Conformance Class: Geometry Topology Extension (relation_family,
 * serialization, version)
 * Conformance Class URI: /conf/geometry-topology-extension.
 */
public class GeometryTopologyTest {

    public GeometryTopologyTest() {
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
     * A.4.1 Tests for all relation families
     * A.4.1.1 /conf/geometry-topology-extension/relate-query-function
     *
     * Requirement: /req/geometry-topology-extension/relate-query-function
     * Implementations shall support geof:relate as a SPARQL extension
     * function, consistent with the relate operator defined in Simple
     * Features [ISO 19125-1].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that a set of SPARQL queries involving the
     * geof:relate function returns the correct result for a test dataset
     * when using the specified serialization and version.
     *
     * c.) Reference: Clause 9.2 Req 21
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void relateQueryFunctionTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.relateQueryFunctionTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.4.2 relation_family = Simple Features
     * A.4.2.1 /conf/geometry-topology-extension/sf-query-functions
     *
     * Requirement: /req/geometry-topology-extension/sf-query-functions
     * Implementations shall support geof:sfEquals, geof:sfDisjoint,
     * geof:sfIntersects, geof:sfTouches, geof:sfCrosses, geof:sfWithin,
     * geof:sfContains, geof:sfOverlaps as SPARQL extension functions,
     * consistent with their corresponding DE-9IM intersection patterns, as
     * defined by Simple Features [ISO 19125-1].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that a set of SPARQL queries involving each
     * of the following functions returns the correct result for a test
     * dataset when using the specified serialization and version:
     * geof:sfEquals, geof:sfDisjoint, geof:sfIntersects, geof:sfTouches,
     * geof:sfCrosses, geof:sfWithin, geof:sfContains, geof:sfOverlaps.
     *
     * c.) Reference: Clause 9.3 Req 22
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void sfQueryFunctionsTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.sfQueryFunctionsTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.4.3 relation_family = Egenhofer
     * A.4.3.1 /conf/geometry-topology-extension/eh-query-functions
     *
     * Requirement: /req/geometry-topology-extension/eh-query-functions
     * Implementations shall support geof:ehEquals, geof:ehDisjoint,
     * geof:ehMeet, geof:ehOverlap, geof:ehCovers, geof:ehCoveredBy,
     * geof:ehInside, geof:ehContains as SPARQL extension functions,
     * consistent with their corresponding DE-9IM intersection patterns, as
     * defined by Simple Features [ISO 19125- 1].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that a set of SPARQL queries involving each
     * of the following functions returns the correct result for a test
     * dataset when using the specified serialization and version:
     * geof:ehEquals, geof:ehDisjoint, geof:ehMeet, geof:ehOverlap,
     * geof:ehCovers, geof:ehCoveredBy, geof:ehInside, geof:ehContains.
     *
     * c.) Reference: Clause 9.4 Req 23
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void ehQueryFunctionsTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.ehQueryFunctionsTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.4.4 relation_family = RCC8
     * A.4.4.1 /conf/geometry-topology-extension/rcc8-query-functions
     *
     * Requirement: /req/geometry-topology-extension/rcc8-query-functions
     * Implementations shall support geof:rcc8eq, geof:rcc8dc, geof:rcc8ec,
     * geof:rcc8po, geof:rcc8tppi, geof:rcc8tpp, geof:rcc8ntpp,
     * geof:rcc8ntppi as SPARQL extension functions, consistent with their
     * corresponding DE-9IM intersection patterns, as defined by Simple
     * Features [ISO 19125-1].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that a set of SPARQL queries involving each
     * of the following functions returns the correct result for a test
     * dataset when using the specified serialization and version:
     * geof:rcc8eq, geof:rcc8dc, geof:rcc8ec, geof:rcc8po, geof:rcc8tppi,
     * geof:rcc8tpp, geof:rcc8ntpp, geof:rcc8ntppi.
     *
     * c.) Reference: Clause 9.5 Req 24
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void rcc8QueryFunctionsTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.rcc8QueryFunctionsTestQueries(), INF_WKT_MODEL);

    }

}
