/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import vocabulary.RDFDataLocation;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Prefixes;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class DistanceFilterFuncTest {

    private static Model MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceFilterFuncTest.class);

    public DistanceFilterFuncTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        MODEL = ModelFactory.createDefaultModel();
        MODEL.read(RDFDataLocation.SAMPLE_WKT);
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

    /**
     * Test of exec method, of class Distance.
     */
    @Test
    public void testExec() {
        System.out.println("exec");
        long queryStartTime = System.nanoTime();
        String queryString = "SELECT ?dist WHERE{ "
                + "ntu:A ntu:hasExactGeometry ?aGeom . "
                + "?aGeom geo:asWKT ?aWKT . "
                + "ntu:B ntu:hasExactGeometry ?bGeom . "
                + "?bGeom geo:asWKT ?bWKT . "
                + "BIND(geof:distance(?aWKT,?bWKT,uom:metre) AS ?dist )"
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.DISTANCE_NAME), geof.nontopological.filterfunction.Distance.class);

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            ResultSet rs = qExec.execSelect();
            long endTime = System.nanoTime();
            long duration = endTime - queryStartTime;
            ResultSetFormatter.out(rs);
            LOGGER.info("Query Execution Time: {}", duration / 1000000);
        }

    }

}
