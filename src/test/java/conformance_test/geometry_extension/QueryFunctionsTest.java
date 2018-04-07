/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import static conformance_test.ConformanceTestSuite.*;

import java.util.Arrays;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 *
 *
 * A.3.1.4 /conf/geometry-extension/query-functions
 *
 * Requirement: /req/geometry-extension/query-functions Implementations shall
 * support geof:distance, geof:buffer, geof:convexHull, geof:intersection,
 * geof:union, geof:difference, geof:symDifference, geof:envelope and
 * geof:boundary as SPARQL extension functions, consistent with the definitions
 * of the corresponding functions (distance, buffer, convexHull, intersection,
 * difference, symDifference, envelope and boundary respectively) in Simple
 * Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each of the
 * following functions returns the correct result for a test dataset when using
 * the specified serialization and version: geof:distance, geof:buffer,
 * geof:convexHull, geof:intersection, geof:union, geof:difference,
 * geof:symDifference, geof:envelope and geof:boundary.
 *
 * c.) Reference: Clause 8.7 Req 19
 *
 * d.) Test Type: Capabilities
 */
public class QueryFunctionsTest {

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
    public void boundaryTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#C");

        String queryString = "SELECT ?place WHERE{ "
                + "?place ex:hasExactGeometry ?aGeom . "
                + "?aGeom geo:asWKT ?aWkt . "
                + "BIND ((geof:boundary( ?aWkt)) AS ?aBoundary). "
                + "BIND ((geof:boundary( \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.1, -83.2 34.1, -83.2 34.5, -83.6 34.5, -83.6 34.1))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>)) AS ?bBoundary). "
                + "FILTER ( geof:sfEquals(?aBoundary, ?bBoundary)) "
                + " }";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void bufferTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#B", "http://example.org/ApplicationSchema#C");

        String queryString = "SELECT ?place WHERE{ "
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " ex:B ex:hasExactGeometry ?bGeom . "
                + " ?bGeom geo:asWKT ?bWkt . "
                + " BIND( geof:buffer(?bWkt, 1000, uom:metre) AS ?buffer) . "
                + " FILTER ( geof:sfIntersects(?aWkt, ?buffer) )"
                + " }ORDER BY ?place";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    /**
     * The increase of the radius should lead the buffer intersects with ex:E as
     * well, compare to the result of positiveTest.
     */
    @Test
    public void anotherBufferTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#B", "http://example.org/ApplicationSchema#C", "http://example.org/ApplicationSchema#D", "http://example.org/ApplicationSchema#F");

        String queryString = "SELECT ?place WHERE{ "
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " ex:B ex:hasExactGeometry ?bGeom . "
                + " ?bGeom geo:asWKT ?bWkt . "
                + " BIND( geof:buffer(?bWkt, 10000, uom:metre) AS ?buffer) . "
                + " FILTER ( geof:sfIntersects(?aWkt, ?buffer) )"
                + " }ORDER BY ?place";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void convexHullTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#C");

        String queryString = "SELECT ?place WHERE{"
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " BIND ( geof:convexHull(\"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.1, -83.2 34.1, -83.2 34.5, -83.6 34.5, -83.6 34.3, -83.4 34.3, -83.6 34.2, -83.6 34.1))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>) AS ?convexHull ) . "
                + " FILTER ( geof:sfEquals(?aWkt, ?convexHull ))"
                + "}";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void differenceTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#E");

        String queryString = "SELECT ?place WHERE{"
                + " ex:C ex:hasExactGeometry ?cGeom . "
                + " ?cGeom geo:asWKT ?cWkt . "
                + " ex:E ex:hasExactGeometry ?eGeom . "
                + " ?eGeom geo:asWKT ?eWkt . "
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " BIND ( geof:difference(?eWkt, ?cWkt) AS ?difference ) . "
                + " FILTER ( geof:sfEquals(?aWkt, ?difference ))"
                + "}";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void distanceTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#C", "http://example.org/ApplicationSchema#B", "http://example.org/ApplicationSchema#F");

        String queryString = "SELECT ?place WHERE{ "
                + "?place ex:hasExactGeometry ?aGeom . "
                + "?aGeom geo:asWKT ?aWkt . "
                + "ex:E ex:hasExactGeometry ?eGeom . "
                + "?eGeom geo:asWKT ?eWkt . "
                + "FILTER (?eGeom != ?aGeom)"
                + " }"
                + "ORDER BY ASC (geof:distance(?eWkt, ?aWkt, uom:metre))"
                + "LIMIT 3";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void envelopeTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#C");

        String queryString = "SELECT ?place WHERE{"
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " BIND ( geof:envelope(\"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.3, -83.4 34.1, -83.2 34.3, -83.4 34.5, -83.6 34.3))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>) AS ?envelope ) . "
                + " FILTER ( geof:sfEquals(?aWkt, ?envelope ))"
                + "}";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void intersectionTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#D");

        String queryString = "SELECT ?place WHERE{"
                + " ex:C ex:hasExactGeometry ?cGeom . "
                + " ?cGeom geo:asWKT ?cWkt . "
                + " ex:D ex:hasExactGeometry ?eGeom . "
                + " ?eGeom geo:asWKT ?eWkt . "
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " BIND ( geof:intersection(?eWkt, ?cWkt) AS ?intersection ) . "
                + " FILTER ( geof:sfEquals(?aWkt, ?intersection ))"
                + "}";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void symmetricDifferenceTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#C");

        String queryString = "SELECT ?place WHERE{"
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " BIND ( geof:symDifference(\"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.1, -83.4 34.1, -83.4 34.3, -83.6 34.3, -83.6 34.1))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.3, -83.4 34.3, -83.4 34.1, -83.2 34.1, -83.2 34.5, -83.6 34.5, -83.6 34.3))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>) AS ?symDifference ) . "
                + " FILTER ( geof:sfEquals(?aWkt, ?symDifference ))"
                + "}";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void positiveTest() {

        List<String> expResult = Arrays.asList("http://example.org/ApplicationSchema#C");

        String queryString = "SELECT ?place WHERE{ "
                + "ex:C ex:hasExactGeometry ?cGeom . "
                + "?cGeom geo:asWKT ?cWkt . "
                + "ex:D ex:hasExactGeometry ?eGeom . "
                + "?eGeom geo:asWKT ?eWkt . "
                + "?place ex:hasExactGeometry ?aGeom . "
                + "?aGeom geo:asWKT ?aWkt . "
                + "BIND ((geof:union( ?cWkt, ?eWkt )) AS ?union)"
                + "FILTER ( geof:sfEquals(?aWkt, ?union))"
                + " }";
        List<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

}
