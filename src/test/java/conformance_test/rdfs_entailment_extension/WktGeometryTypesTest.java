/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.rdfs_entailment_extension;

import static conformance_test.ConformanceTestSuite.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 *
 * A.5.2.1 /conf/rdfs-entailment-extension/wkt-geometry-types
 *
 * Requirement: /req/rdfs-entailment-extension/wkt-geometry-types
 * Implementations shall support graph patterns involving terms from an RDFS/OWL
 * class hierarchy of geometry types consistent with the one in the specified
 * version of Simple Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving WKT Geometry
 * types returns the correct result for a test dataset using the specified
 * version of Simple Features.
 *
 * c.) Reference: Clause 10.3.1 Req 26
 *
 * d.) Test Type: Capabilities
 */
public class WktGeometryTypesTest {

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
    public void positiveTest() {

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#GExactGeom");
        expResult.add("http://example.org/ApplicationSchema#FExactGeom");
        expResult.add("http://example.org/ApplicationSchema#EExactGeom");
        expResult.add("http://example.org/ApplicationSchema#DExactGeom");
        expResult.add("http://example.org/ApplicationSchema#CExactGeom");

        String queryString = "SELECT ?geometry WHERE{"
                + " ?geometry rdf:type sf:Polygon ."
                + "}";
        List<String> result = queryMany(queryString, infModel);

        assertEquals(expResult, result);
    }

}
