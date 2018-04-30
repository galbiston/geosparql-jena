/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import static conformance_test.ConformanceTestSuite.*;

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
 *
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

        expResult.add("http://example.org/ApplicationSchema#AExactGeom");
        expResult.add("http://example.org/ApplicationSchema#BExactGeom");
        expResult.add("http://example.org/ApplicationSchema#CExactGeom");
        expResult.add("http://example.org/ApplicationSchema#DExactGeom");
        expResult.add("http://example.org/ApplicationSchema#EExactGeom");
        expResult.add("http://example.org/ApplicationSchema#FExactGeom");
        expResult.add("http://example.org/ApplicationSchema#GExactGeom");

        String queryString = "SELECT ?geometry WHERE{"
                + " ?geometry rdf:type geo:Geometry ."
                + "}ORDER BY ?geometry";
        List<String> result = queryMany(queryString, infModel);

        assertEquals(expResult, result);
    }

}
