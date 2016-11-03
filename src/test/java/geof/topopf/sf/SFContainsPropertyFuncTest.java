/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topopf.sf;

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
import implementation.support.RDFDataLocation;
import implementation.support.Prefixes;
import implementation.vocabulary.Geo;

/**
 *
 * @author haozhechen
 */
public class SFContainsPropertyFuncTest {

    private static Model MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(SFContainsPropertyFuncTest.class);

    public SFContainsPropertyFuncTest() {
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
     * Test of exec method, of class ContainsQRPropertyFunc.
     */
    @Test
    public void testExec() {
        System.out.println("exec");

        long queryStartTime = System.nanoTime();
        String queryString = "SELECT ?place WHERE{ "
                //+ "ntu:D ntu:hasExactGeometry ?dGeom . "
                //+ "?dGeom gml:asGML ?dGML . "
                + "?place ntu:hasExactGeometry ?Geom . "
                + "?Geom geo:asWKT ?WKT . "
                //+ "?dGML geo:sfContains ?GML"
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        PropertyFunctionRegistry.get().put(Geo.SF_CONTAINS_NAME, geof.topological.simplefeatures.propertyfunction.sfContainsPF.class);

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            ResultSet rs = qExec.execSelect();
            long endTime = System.nanoTime();
            long duration = endTime - queryStartTime;
            ResultSetFormatter.out(rs);
            LOGGER.info("Query Execution Time: {}", duration / 1000000);
        }

    }

}
