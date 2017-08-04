/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import static conformance_test.ConformanceTestSuite.*;
import implementation.function_registry.RegistryLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 *
 * A.3.1.1 /conf/geometry-extension/geometry-class
 *
 * Requirement: /req/geometry-extension/geometry-class Implementations shall
 * allow the RDFS class geo:Geometry to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:Geometry return the
 * correct result on a test dataset.
 *
 * c.) Reference: Clause 8.2.1 Req 7 d.) Test Type: Capabilities
 */
public class GeometryClassTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();
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

        List<String> expectedList = new ArrayList<>();

        expectedList.add("http://example.org/ApplicationSchema#AExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#BExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#CExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#DExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#EExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#FExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#GExactGeom");

        String Q1 = "SELECT ?geometry WHERE{"
                + " ?geometry rdf:type geo:Geometry ."
                + "}ORDER BY ?geometry";
        List<String> actualList = resourceQuery(Q1, infModel);

        assertEquals(expectedList, actualList);
    }

}
