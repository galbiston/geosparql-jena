/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.egenhofer_spatial_relations;

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
 * A.2.2.1 /conf/topology-vocab-extension/eh-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/eh-spatial-relations
 * Implementations shall allow the properties geo:ehEquals, geo:ehDisjoint,
 * geo:ehMeet, geo:ehOverlap, geo:ehCovers, geo:ehCoveredBy, geo:ehInside,
 * geo:ehContains to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 7.3 Req 5
 *
 * d.) Test Type: Capabilities
 */
public class EhOverlapTest {

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
         * ehOverlap is slightly different from sfOverlaps, sfOverlaps compares
         * two geometries of the same dimension and returns TRUE if their
         * intersection set results in a geometry different from both but of the
         * same dimension. However, ehOverlap does not necessarily require the
         * intersection of the two geometries to be exact same dimension,
         * therefore ehOverlap's functionality can be seen as a combination of
         * sfCrosses and sfOverlaps.
         */
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#F");
        expResult.add("http://example.org/ApplicationSchema#B");

        ArrayList<String> result = resourceQuery(topologyVocabluaryQuery("ex:C", "geo:ehOverlap", ""), infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void negativeTest() {

        ArrayList<String> expResult = new ArrayList<>();

        assertEquals(expResult, resourceQuery(topologyVocabluaryQuery("ex:A", "geo:ehOverlap", "FILTER ( ?aGeom != ?bGeom )"), infModel));

    }

}
