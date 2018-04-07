/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension.egenhofer_query_functions;

import static conformance_test.ConformanceTestSuite.*;
import java.util.ArrayList;
import java.util.List;
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
 * A.4.3.1 /conf/geometry-topology-extension/eh-query-functions
 *
 * Requirement: /req/geometry-topology-extension/eh-query-functions
 * Implementations shall support geof:ehEquals, geof:ehDisjoint, geof:ehMeet,
 * geof:ehOverlap, geof:ehCovers, geof:ehCoveredBy, geof:ehInside,
 * geof:ehContains as SPARQL extension functions, consistent with their
 * corresponding DE-9IM intersection patterns, as defined by Simple Features
 * [ISO 19125- 1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each of the
 * following functions returns the correct result for a test dataset when using
 * the specified serialization and version: geof:ehEquals, geof:ehDisjoint,
 * geof:ehMeet, geof:ehOverlap, geof:ehCovers, geof:ehCoveredBy, geof:ehInside,
 * geof:ehContains.
 *
 * c.) Reference: Clause 9.4 Req 23
 *
 * d.) Test Type: Capabilities
 */
public class EhInsideTest {

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
         * ehInside is slightly different from the sfWithin, which will not
         * return the same instance while the sfWithin will return the same
         * instance.
         */
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#C");

        String queryString = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:ehInside(\"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, ?aWKT) ."
                + "}";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void negativeTest() {

        String queryString = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:ehInside(\"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-86.4 31.4)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, ?aWKT) ."
                + "}";
        List<String> expResult = new ArrayList<>();
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

}
