/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.test;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
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
import vocabulary.Prefixes;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class georEqualsTest {

    private static Model MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(georEqualsTest.class);

    public georEqualsTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        MODEL = ModelFactory.createDefaultModel();
        MODEL.read(TestDataLocation.DATA);

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
     * Test of exec method, of class georEquals.
     */
    @Test
    public void testExec() {
        System.out.println("exec");

        String queryString = "SELECT ?b WHERE{ "
                + "ntu:A ntu:hasExactGeometry ?aGeom ."
                + "?b ntu:hasExactGeometry ?bGeom ."
                + "ntu:A geo:sfEquals ?bGeom"
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_EQUALS_NAME), prototype.georEquals.class);
        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            ResultSet rs = qExec.execSelect();

            ResultSetFormatter.out(rs);
        }

    }

}
