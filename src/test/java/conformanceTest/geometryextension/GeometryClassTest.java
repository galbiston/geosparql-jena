/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

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
 * A.3.1.1 /conf/geometry-extension/geometry-class
 *
 * Requirement: /req/geometry-extension/geometry-class
 * Implementations shall allow the RDFS class geo:Geometry to be used in
 * SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:Geometry return
 * the correct result on a test dataset.
 *
 * c.) Reference: Clause 8.2.1 Req 7 d.) Test Type: Capabilities
 */
public class GeometryClassTest {

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

        this.expectedList.add("http://example.org/ApplicationSchema#BExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#CExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#AExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#FPointGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#EExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#CPointGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#DPointGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#DExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#FExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#EPointGeom");

        String Q1 = "SELECT ?geometry WHERE{"
                + " ?geometry rdf:type geo:Geometry ."
                + "}";
        this.actualList = resourceQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
