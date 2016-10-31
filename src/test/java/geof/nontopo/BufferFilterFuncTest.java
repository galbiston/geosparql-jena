/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import support.RDFDataLocation;
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
import support.Prefixes;
import support.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class BufferFilterFuncTest {

    private static Model MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(BufferFilterFuncTest.class);

    public BufferFilterFuncTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        long dataReadStartTime = System.nanoTime();
        MODEL = ModelFactory.createDefaultModel();
        MODEL.read(RDFDataLocation.SAMPLE_WKT);
        long endTime = System.nanoTime();
        long duration = endTime - dataReadStartTime;
        LOGGER.info("Data Read Time: {} ", duration / 1000000);
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
     * Test of exec method, of class Buffer.
     */
    @Test
    public void testExec() {
        System.out.println("exec");

        long queryStartTime = System.nanoTime();
        String queryString = "SELECT (geof:buffer(?wkt, 10, uom:metre) AS ?buf) WHERE{ "
                + "ntu:B ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.BUFFER_NAME), geof.nontopological.filterfunction.Buffer.class);
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            ResultSet rs = qExec.execSelect();
            long endTime = System.nanoTime();
            long duration = endTime - queryStartTime;
            ResultSetFormatter.out(rs);
            LOGGER.info("Query Execution Time: {}", duration / 1000000);
        }

    }
}
