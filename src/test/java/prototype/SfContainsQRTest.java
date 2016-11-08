/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import conformanceTest.RDFDataLocation;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import org.apache.jena.util.FileManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import vocabulary.Prefixes;

/**
 *
 * @author haozhechen
 */
public class SfContainsQRTest {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * Inference WKT model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_WKT_MODEL;

    public SfContainsQRTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        DEFAULT_WKT_MODEL = ModelFactory.createDefaultModel();
        /**
         * Setup inference model.
         */
        Model schema = FileManager.get().loadModel("http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf");
        /**
         * The use of OWL reasoner can bind schema with existing test data.
         */
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        INF_WKT_MODEL = ModelFactory.createInfModel(reasoner, DEFAULT_WKT_MODEL);
        INF_WKT_MODEL.read(RDFDataLocation.SAMPLE_WKT);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        PropertyFunctionRegistry.get().put("http://www.opengis.net/ont/geosparql#sfContains", prototype.SfContainsQR.class);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of expressionFunction method, of class SfContainsQR.
     */
    @Test
    public void testExpressionFunction() {

        String queryString = "SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " ?aWKT geo:sfContains \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)^^http://www.opengis.net/ont/geosparql#wktLiteral\" ."
                + "}";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet rs = qExec.execSelect();
            ResultSetFormatter.out(rs);
        }

    }

}
