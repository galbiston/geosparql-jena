/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.topologyvocablulary;

import static conformanceTest.ConformanceTestSuite.*;
import static implementation.functionregistry.RegistryLoader.load;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
 * Implementations shall allow the properties geo:ehEquals,
 * geo:ehDisjoint, geo:ehMeet, geo:ehOverlap, geo:ehCovers,
 * geo:ehCoveredBy, geo:ehInside, geo:ehContains to be used in SPARQL
 * graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 7.3 Req 5
 *
 * d.) Test Type: Capabilities
 */
public class EhSpatialRelationsDisjointTest {

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

        /**
         * Disjoint returns t (TRUE) if the intersection of the two geometries
         * is an empty set.
         */
        this.expectedList.add("http://example.org/ApplicationSchema#G");
        this.expectedList.add("http://example.org/ApplicationSchema#F");
        this.expectedList.add("http://example.org/ApplicationSchema#E");
        this.expectedList.add("http://example.org/ApplicationSchema#D");
        this.expectedList.add("http://example.org/ApplicationSchema#B");

        this.actualList = resourceQuery(topologyVocabluaryQuery("ex:A", "geo:ehDisjoint", ""), INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

    @Test
    public void negativeTest() {

        assertFalse("failure - should be false", emptyQuery(topologyVocabluaryQuery("ex:C", "geo:ehDisjoint", "FILTER ( ?aGeom != ?bGeom )"), INF_WKT_MODEL));
    }

}
