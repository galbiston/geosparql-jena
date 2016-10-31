/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.test;

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

/**
 *
 * @author haozhechen
 */
public class DistanceFilterFuncTest {

    private static Model MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceTest.class);

    public DistanceFilterFuncTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        MODEL = ModelFactory.createDefaultModel();
        LOGGER.info("Before Reading Data");
        MODEL.read(RDFDataLocation.SAMPLE);
        LOGGER.info("After Reading Data");
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
        String queryString = "SELECT ?b WHERE{ "
                + "ntu:A ntu:hasPointGeometry ?aGeom . ?aGeom gml:asGML ?aGML . "
                + "?b ntu:hasPointGeometry ?bGeom . ?bGeom gml:asGML ?bGML . "
                + "FILTER ( ?bGeom != ?aGeom )"
                + " }"
                + "ORDER BY DESC ( ext:distance(?aGML, ?bGML, uom:degree) )";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        FunctionRegistry.get().put("http://example/f#distance", geof.nontopological.filterfunction.Distance.class);

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            ResultSet rs = qExec.execSelect();
            LOGGER.info("success in executing result!");
            ResultSetFormatter.out(rs);
        }
    }

}
