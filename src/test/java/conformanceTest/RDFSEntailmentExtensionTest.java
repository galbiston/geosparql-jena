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
 * A.5Conformance Class: RDFS Entailment Extension (relation_family,
 * serialization, version) Conformance Class URI:
 * /conf/rdfs-entailment-extension.
 */
public class RDFSEntailmentExtensionTest {

    public RDFSEntailmentExtensionTest() {
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
     * A.5.1 Tests for all implementations A.5.1.1
     * /conf/rdfsentailmentextension/bgp-rdfs-ent
     *
     * Requirement: /req/rdfs-entailment-extension/bgp-rdfs-ent Basic graph
     * pattern matching shall use the semantics defined by the RDFS Entailment
     * Regime [W3C SPARQL Entailment].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that a set of SPARQL queries involving entailed
     * RDF triples returns the correct result for a test dataset using the
     * specified serialization, version and relation_family.
     *
     * c.) Reference: Clause 10.2 Req 25
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void bgpRdfsEntTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.bgpRdfsEntTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.5.2 serialization=WKT A.5.2.1
     * /conf/rdfs-entailment-extension/wkt-geometry-types
     *
     * Requirement: /req/rdfs-entailment-extension/wkt-geometry-types
     * Implementations shall support graph patterns involving terms from an
     * RDFS/OWL class hierarchy of geometry types consistent with the one in the
     * specified version of Simple Features [ISO 19125-1].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that a set of SPARQL queries involving WKT
     * Geometry types returns the correct result for a test dataset using the
     * specified version of Simple Features.
     *
     * c.) Reference: Clause 10.3.1 Req 26
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void wktGeometryTypesTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.wktGeometryTypesTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.5.3 serialization=GML A.5.3.1
     * /conf/rdfs-entailment-extension/gml-geometry-types
     *
     * Requirement: /req/rdfs-entailment-extension/gml-geometry-types
     * Implementations shall support graph patterns involving terms from an
     * RDFS/OWL class hierarchy of geometry types consistent with the GML schema
     * that implements GM_Object using the specified version of GML [OGC
     * 07-036].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that a set of SPARQL queries involving GML
     * Geometry types returns the correct result for a test dataset using the
     * specified version of GML.
     *
     * c.) Reference: Clause 10.4.1 Req 27
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void gmlGeometryTypesTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.gmlGeometryTypesTestQueries(), INF_WKT_MODEL);

    }

}
