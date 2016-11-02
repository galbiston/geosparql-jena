/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.test;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Prefixes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 */
public class DistanceTest {

    private static Model MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceTest.class);

    public DistanceTest() {

    }

    @BeforeClass
    public static void setUpClass() {
        MODEL = ModelFactory.createDefaultModel();
        LOGGER.info("Before Reading Data");
        //InputStream in = FileManager.get().open( "/Users/haozhechen/NetBeansProjects/GeoSPARQL/src/main/java/propertyfunction/spatialdata.rdf" );
        MODEL.read(TestDataLocation.SAMPLE);
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
        System.out.println("distance");

        String queryString = "SELECT ?aGML ?bGML ?distance WHERE{ "
                + "ntu:A ntu:hasPointGeometry ?aGeom . ?aGeom gml:asGML ?aGML . "
                + "ntu:B ntu:hasPointGeometry ?bGeom . ?bGeom gml:asGML ?bGML . "
                + "?distance ext:distance(?aGML ?bGML) ."
                + " }"
                + "LIMIT 2";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        PropertyFunctionRegistry.get().put("http://example/f#distance", prototype.Distance.class);
        //FunctionRegistry.get().put("http://example/f#distance", CustomPF.DistanceFilterFunc.class) ;

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            ResultSet rs = qExec.execSelect();
            LOGGER.info("success in executing result!");
            ResultSetFormatter.out(rs);
        }

    }

    @Ignore
    @Test
    public void testDistance() {
        String queryString = "SELECT ?distance WHERE{ ?distance ext:distance(?aGML ?bGML)}";

        QuerySolutionMap bindings = new QuerySolutionMap();
        //bindings.add("aGML", ResourceFactory.);

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        PropertyFunctionRegistry.get().put("http://example/f#distance", prototype.Distance.class);

        QueryExecution qe = QueryExecutionFactory.create(query.asQuery(), MODEL);
        ResultSet rs = qe.execSelect();
        double result = -1;
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            result = qs.getLiteral("distance").getDouble();
        }
        double expResult = 5.545;
        assertEquals(result, expResult, 0);
    }

}
