/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.rdfsentailment;

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
 * A.5.2.1 /conf/rdfs-entailment-extension/wkt-geometry-types
 *
 * Requirement: /req/rdfs-entailment-extension/wkt-geometry-types
 * Implementations shall support graph patterns involving terms from an
 * RDFS/OWL class hierarchy of geometry types consistent with the one in
 * the specified version of Simple Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving WKT
 * Geometry types returns the correct result for a test dataset using
 * the specified version of Simple Features.
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
    public void positiveTest() {

        this.expectedList.add("http://example.org/ApplicationSchema#FExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#EExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#DExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#CExactGeom");

        String Q1 = "SELECT ?geometry WHERE{"
                + " ?geometry rdf:type sf:Polygon ."
                + "}";
        this.actualList = resourceQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
