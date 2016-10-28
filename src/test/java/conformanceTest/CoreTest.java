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
 * A.1Conformance Class: Core
 * Conformance Class URI: /conf/core.
 */
public class CoreTest {

    public CoreTest() {
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
     * A.1.1 /conf/core/sparql-protocol
     * Requirement: /req/core/sparql-protocol
     *
     * Implementations shall support the SPARQL Query Language for RDF
     * [W3C SPARQL], the SPARQL Protocol for RDF [W3CSPARQL Protocol] and
     * the SPARQL Query Results XML Format [W3C SPARQL Result Format].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that the implementation accepts SPARQL
     * queries and returns the correct results in the correct format,
     * according to the SPARQL Query Language for RDF, the SPARQL Protocol
     * for RDF and SPARQL Query Results XML Format W3C specifications.
     *
     * c.) Reference: Clause 6.1 Req 1 d.) Test Type: Capabilities.
     */
    @Test
    public void sparqlProtocolTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.sparqlProtocolTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.1.2 /conf/core/spatial-object-class
     * Requirement: /req/core/spatial-object-class
     *
     * Implementations shall allow the RDFS class geo:SpatialObject to be
     * used in SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving geo:SpatialObject
     * return the correct result on a test dataset.
     *
     * c.) Reference: Clause 6.2.1 Req 2
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void spatialObjectClassTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.spatialObjectClassTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.1.3 /conf/core/feature-class
     * Requirement: /req/core/feature-class
     * Implementations shall allow the RDFS class geo:Feature to be used in
     * SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving geo:Feature return the
     * correct result on a test dataset.
     *
     * c.) Reference: Clause 6.2.2 Req 3
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void featureClassTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.featureClassTestQueries(), INF_WKT_MODEL);

    }

}
