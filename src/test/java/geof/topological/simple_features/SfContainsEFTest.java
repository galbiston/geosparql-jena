/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.simple_features;

import static conformance_test.ConformanceTestSuite.initWktModel;
import implementation.functionregistry.RegistryLoader;
import implementation.support.Prefixes;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.lang.sparql_11.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 */
public class SfContainsEFTest {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * Inference WKT model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel infModel;

    public SfContainsEFTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        RegistryLoader.load();
        infModel = initWktModel();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ParseException {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFunction() {

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:sfContains(?aWKT, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> ) ."
                + "}";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), infModel)) {
            ResultSet rs = qExec.execSelect();
            ResultSetFormatter.out(rs);
        }

    }
}
