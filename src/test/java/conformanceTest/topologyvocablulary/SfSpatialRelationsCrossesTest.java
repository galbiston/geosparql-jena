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
 * A.2.1.1 /conf/topology-vocab-extension/sf-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/sf-spatial-relations
 * Implementations shall allow the properties geo:sfEquals,
 * geo:sfDisjoint, geo:sfIntersects, geo:sfTouches, geo:sfCrosses,
 * geo:sfWithin, geo:sfContains, geo:sfOverlaps to be used in SPARQL
 * graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 7.2 Req 4
 *
 * d.) Test Type: Capabilities
 */
public class SfSpatialRelationsCrossesTest {

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
         * Cross returns t (TRUE) if the intersection results in a geometry
         * whose dimension is one less than the maximum dimension of the two
         * source geometries and the intersection set is interior to both source
         * geometries, Cross returns t (TRUE) for only multipoint/polygon,
         * multipoint/linestring, linestring/linestring, linestring/polygon, and
         * linestring/multipolygon comparisons.
         */
        this.expectedList.add("http://example.org/ApplicationSchema#B");

        this.actualList = resourceQuery(topologyVocabluaryQuery("ex:C", "geo:sfCrosses", ""), INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

    @Test
    public void negativeTest() {

        assertFalse("failure - should be false", emptyQuery(topologyVocabluaryQuery("ex:F", "geo:sfCrosses", "FILTER ( ?aGeom != ?bGeom )"), INF_WKT_MODEL));
    }
}
