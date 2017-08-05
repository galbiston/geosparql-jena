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
 * A.1.3 /conf/core/feature-class Requirement: /req/core/feature-class
 * Implementations shall allow the RDFS class geo:Feature to be used in SPARQL
 * graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:Feature return the correct
 * result on a test dataset.
 *
 * c.) Reference: Clause 6.2.2 Req 3
 *
 * d.) Test Type: Capabilities
 */
public class FeatureClassTest {

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

        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#G");
        expResult.add("http://example.org/ApplicationSchema#F");
        expResult.add("http://example.org/ApplicationSchema#E");
        expResult.add("http://example.org/ApplicationSchema#D");
        expResult.add("http://example.org/ApplicationSchema#C");
        expResult.add("http://example.org/ApplicationSchema#B");
        expResult.add("http://example.org/ApplicationSchema#A");

        String queryString = "SELECT ?feature WHERE{"
                + " ?feature rdf:type geo:Feature ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        ArrayList<String> result = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), infModel)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("?feature");
                result.add(resource.toString());
            }
        }
        assertEquals(expResult, result);
    }
}
