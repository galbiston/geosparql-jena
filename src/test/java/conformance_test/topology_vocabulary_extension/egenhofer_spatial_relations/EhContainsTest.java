/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.egenhofer_spatial_relations;

import static conformance_test.ConformanceTestSuite.*;
import implementation.function_registry.RegistryLoader;
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
 * @author haozhechen
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
public class EhContainsTest {

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

        /**
         * ehContains is slightly different from the sfContains, which will not
         * return the same instance while the sfContains will return the same
         * instance.
         */
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#C");

        ArrayList<String> actualList = resourceQuery(topologyVocabluaryQuery("ex:A", "geo:ehContains", ""), infModel);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void negativeTest() {

        ArrayList<String> expectedList = new ArrayList<>();

        assertEquals(expectedList, resourceQuery(topologyVocabluaryQuery("ex:C", "geo:ehContains", "FILTER ( ?aGeom != ?bGeom )"), infModel));

    }

}
