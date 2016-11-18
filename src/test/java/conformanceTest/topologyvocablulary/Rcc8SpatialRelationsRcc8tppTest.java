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
 * A.2.3.1 /conf/topology-vocab-extension/rcc8-spatial-relations
 *
 * Requirement: /req/topology-vocab-extension/rcc8-spatial-relations
 * Implementations shall allow the properties geo:rcc8eq, geo:rcc8dc,
 * geo:rcc8ec, geo:rcc8po, geo:rcc8tppi, geo:rcc8tpp, geo:rcc8ntpp,
 * geo:rcc8ntppi to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties
 * return the correct result for a test dataset
 *
 * c.) Reference: Clause 7.4 Req 6
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8SpatialRelationsRcc8tppTest {

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

        this.expectedList.add("http://example.org/ApplicationSchema#C");

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.1, -83.4 34.1, -83.4 34.3, -83.6 34.3, -83.6 34.1))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> geo:rcc8tpp ?aWKT ."
                + "}";
        this.actualList = resourceQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

    @Test
    public void negativeTest() {

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.3 34.0, -83.1 34.0, -83.1 34.2, -83.3 34.2, -83.3 34.0))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> geo:rcc8tpp ?aWKT ."
                + "}";
        assertFalse("failure - should be false", emptyQuery(Q1, INF_WKT_MODEL));
    }

}
