/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension.simplefeatures_spatial_relations;

import static conformance_test.ConformanceTestSuite.*;
import implementation.functionregistry.RegistryLoader;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author haozhechen
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
public class SfIntersectsTest {

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
         * Intersects returns t (TRUE) if the intersection does not result in an
         * empty set, Intersects returns the exact opposite result of disjoint.
         */
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#G");
        expectedList.add("http://example.org/ApplicationSchema#F");
        expectedList.add("http://example.org/ApplicationSchema#E");
        expectedList.add("http://example.org/ApplicationSchema#D");
        expectedList.add("http://example.org/ApplicationSchema#C");
        expectedList.add("http://example.org/ApplicationSchema#B");
        expectedList.add("http://example.org/ApplicationSchema#A");

        ArrayList<String> actualList = resourceQuery(topologyVocabluaryQuery("ex:C", "geo:sfIntersects", ""), infModel);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void negativeTest() {

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + "ex:E ex:hasExactGeometry ?bGeom . "
                + "ex:C ex:hasExactGeometry ?cGeom ."
                + "?place geo:sfIntersects ?bGeom . "
                + "FILTER ( ?aGeom != ?cGeom ) . "
                + "FILTER ( ?aGeom != ?bGeom ) . "
                + "}";
        assertFalse(emptyQuery(Q1, infModel));
    }

}
