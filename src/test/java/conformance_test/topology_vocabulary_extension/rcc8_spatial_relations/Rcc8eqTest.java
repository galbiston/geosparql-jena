/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.rcc8_spatial_relations;

import static conformance_test.ConformanceTestSuite.*;
import implementation.GeoSPARQLSupport;

import java.util.ArrayList;
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
 * A.2.3.1 /conf/topology-vocab-extension/rcc8-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/rcc8-spatial-relations
 * Implementations shall allow the properties geo:rcc8eq, geo:rcc8dc,
 * geo:rcc8ec, geo:rcc8po, geo:rcc8tppi, geo:rcc8tpp, geo:rcc8ntpp,
 * geo:rcc8ntppi to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset
 *
 * c.) Reference: Clause 7.4 Req 6
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8eqTest {

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

        /**
         * Equal returns t (TRUE) if two geometries of the same type have
         * identical X,Y coordinate values.
         */
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#F");

        ArrayList<String> result = resourceQuery(topologyVocabluaryQuery("ex:F", "geo:rcc8eq", ""), infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void negativeTest() {

        ArrayList<String> expResult = new ArrayList<>();

        assertEquals(expResult, resourceQuery(topologyVocabluaryQuery("ex:A", "geo:rcc8eq", "FILTER ( ?aGeom != ?bGeom )"), infModel));

    }

}
