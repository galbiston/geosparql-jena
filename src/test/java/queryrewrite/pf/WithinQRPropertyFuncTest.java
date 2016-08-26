/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.pf;

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
import prototype.test.TestDataLocation;
import vocabulary.Prefixes;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class WithinQRPropertyFuncTest {

    private static Model MODEL;
    private static InfModel INF_MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(WithinQRPropertyFuncTest.class);

    public WithinQRPropertyFuncTest() {
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
     * Test of exec method, of class SFWithinQRPropertyFunc.
     */
    @Test
    public void testExec() {
        System.out.println("exec");

        String queryString = "SELECT ?place WHERE{ "
                + "ntu:A ntu:hasExactGeometry ?dGeom . ?dGeom gml:asGML ?dGML . "
                + "?place ntu:hasExactGeometry ?Geom . ?Geom gml:asGML ?GML . "
                + "?dGML geo:sfWithin ?GML ."
                + "FILTER ( ?Geom != ?dGeom )"
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.SFWithinQRPropertyFunc.class);

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            ResultSet rs = qExec.execSelect();
            LOGGER.info("Test Query: \n{}", qExec.getQuery().toString());
            ResultSetFormatter.out(rs);
        }

    }

}
