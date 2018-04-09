/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.simplefeatures_spatial_relations;

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
 * A.2.1.1 /conf/topology-vocab-extension/sf-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/sf-spatial-relations
 * Implementations shall allow the properties geo:sfEquals, geo:sfDisjoint,
 * geo:sfIntersects, geo:sfTouches, geo:sfCrosses, geo:sfWithin, geo:sfContains,
 * geo:sfOverlaps to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 7.2 Req 4
 *
 * d.) Test Type: Capabilities
 */
public class SfTouchesTest {

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
         * Touch returns t (TRUE) if none of the points common to both
         * geometries intersect the interiors of both geometries, At least one
         * geometry must be a line string, polygon, multi line string, or multi
         * polygon.
         */
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#E");

        ArrayList<String> result = resourceQuery(topologyVocabluaryQuery("ex:C", "geo:sfTouches", ""), infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void negativeTest() {

        ArrayList<String> expResult = new ArrayList<>();

        assertEquals(expResult, resourceQuery(topologyVocabluaryQuery("ex:A", "geo:sfTouches", "FILTER ( ?aGeom != ?bGeom )"), infModel));

    }

}
