/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.topologyvocablulary;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import static implementation.functionregistry.RegistryLoader.load;
import implementation.support.Prefixes;
import java.util.ArrayList;
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
public class EhSpatialRelationsInsideTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
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
         * ehInside is slightly different from the sfWithin, which will not
         * return the same instance while the sfWithin will return the same
         * instance.
         */
        this.expectedList.add("http://ntu.ac.uk/ont/geo#C");

        String Q1 = "SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> geo:ehInside ?aWKT ."
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
                + " \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-86.4 31.4)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> geo:ehInside ?aWKT ."
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
