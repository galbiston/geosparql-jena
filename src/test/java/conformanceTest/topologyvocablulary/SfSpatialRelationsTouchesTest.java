/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.topologyvocablulary;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import java.util.ArrayList;
import static main.Main.init;
import main.TopologyRegistryLevel;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import vocabulary.Prefixes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
public class SfSpatialRelationsTouchesTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        init(TopologyRegistryLevel.DEFAULT);
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
         * Touch returns t (TRUE) if none of the points common to both
         * geometries intersect the interiors of both geometries, At least one
         * geometry must be a line string, polygon, multi line string, or
         * multi polygon.
         */
        this.expectedList.add("http://ntu.ac.uk/ont/geo#E");

        String Q1 = "SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " ?aWKT geo:sfTouches \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.1, -83.2 34.1, -83.2 34.5, -83.6 34.5, -83.6 34.1))^^http://www.opengis.net/ont/geosparql#wktLiteral\" ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("?place");
                this.actualList.add(resource.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

    @Test
    public void negativeTest() {

        String Q1 = "SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " ?aWKT geo:sfTouches \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-86.4 31.4)^^http://www.opengis.net/ont/geosparql#wktLiteral\" ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("?place");
                assertNull("should be null", resource.toString());
            }
        }
    }

}
