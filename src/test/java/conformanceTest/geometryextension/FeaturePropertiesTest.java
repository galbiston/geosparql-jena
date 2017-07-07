/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.*;
import static implementation.functionregistry.RegistryLoader.load;
import java.util.Arrays;
import java.util.List;
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
 * A.3.1.2 /conf/geometry-extension/feature-properties
 *
 * Requirement: /req/geometry-extension/feature-properties Implementations shall
 * allow the properties geo:hasGeometry and geo:hasDefaultGeometry to be used in
 * SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 8.3 Req 8
 *
 * d.) Test Type: Capabilities
 */
public class FeaturePropertiesTest {

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

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testHasGeometry() {

        List<String> expectedList = Arrays.asList("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)^^http://www.opengis.net/ont/geosparql#wktLiteral");

        String Q1 = "SELECT ?aWKT WHERE{"
                + " ex:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        List<String> actualList = literalQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", expectedList, actualList);
    }

    @Test
    public void testHasDefaultGeometry() {

        List<String> expectedList = Arrays.asList("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)^^http://www.opengis.net/ont/geosparql#wktLiteral");

        String Q1 = "SELECT ?aWKT WHERE{"
                + " ex:A geo:hasDefaultGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        List<String> actualList = literalQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", expectedList, actualList);
    }

}
