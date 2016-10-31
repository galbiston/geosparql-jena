/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.test;

import support.RDFDataLocation;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.Prefixes;

/**
 *
 * @author haozhechen
 */
public class EqualsQueryRewritePFTest {

    private static Model MODEL;
    private static InfModel INF_MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(EqualsQueryRewritePFTest.class);

    public EqualsQueryRewritePFTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        MODEL = ModelFactory.createDefaultModel();
        LOGGER.info("Before Reading Data");
        MODEL.read(RDFDataLocation.SAMPLE);
        LOGGER.info("After Reading Data");

        INF_MODEL = ModelFactory.createRDFSModel(MODEL);

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
     * Test of build method, of class EqualsQueryRewritePF.
     */
    @Test
    public void testBuild() {
        System.out.println("build");

    }

    /**
     * Test of exec method, of class EqualsQueryRewritePF.
     */
    @Test
    public void testExec() {
        System.out.println("exec");

        String queryString = "SELECT ?b WHERE{ "
                + "ntu:A ntu:hasExactGeometry ?aGeom. "
                + "?b ntu:hasExactGeometry ?bGeom. "
                + "ntu:A geo:sfEquals ?bGeom. "
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        PropertyFunctionRegistry.get().put("http://www.opengis.net/ont/geosparql#sfEquals", prototype.EqualsQueryRewritePF.class);

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), INF_MODEL)) {
            ResultSet rs = qExec.execSelect();
            LOGGER.info("success in executing result!");
            ResultSetFormatter.out(rs);
        }
    }

}
