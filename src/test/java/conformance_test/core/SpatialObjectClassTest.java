/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.core;

import static conformance_test.ConformanceTestSuite.*;
import implementation.function_registry.RegistryLoader;
import implementation.support.Prefixes;
import java.util.ArrayList;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 *
 * A.1.2 /conf/core/spatial-object-class Requirement:
 * /req/core/spatial-object-class
 *
 * Implementations shall allow the RDFS class geo:SpatialObject to be used in
 * SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:SpatialObject return the
 * correct result on a test dataset.
 *
 * c.) Reference: Clause 6.2.1 Req 2
 *
 * d.) Test Type: Capabilities
 */
public class SpatialObjectClassTest {

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

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#G");
        expectedList.add("http://example.org/ApplicationSchema#F");
        expectedList.add("http://example.org/ApplicationSchema#E");
        expectedList.add("http://example.org/ApplicationSchema#D");
        expectedList.add("http://example.org/ApplicationSchema#C");
        expectedList.add("http://example.org/ApplicationSchema#B");
        expectedList.add("http://example.org/ApplicationSchema#A");
        expectedList.add("http://example.org/ApplicationSchema#GExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#FExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#EExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#DExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#CExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#BExactGeom");
        expectedList.add("http://example.org/ApplicationSchema#AExactGeom");

        String Q1 = "SELECT ?feature WHERE{"
                + " ?feature rdf:type geo:SpatialObject ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        ArrayList<String> actualList = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), infModel)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("?feature");
                actualList.add(resource.toString());
            }
        }
        assertEquals(expectedList, actualList);
    }
}
