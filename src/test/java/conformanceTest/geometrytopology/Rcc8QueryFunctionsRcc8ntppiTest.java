/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometrytopology;

import java.util.ArrayList;
import static main.Main.init;
import main.TopologyRegistryLevel;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import prototype.test.TestDataLocation;
import vocabulary.Prefixes;

/**
 *
 * @author haozhechen
 *
 * A.4.4.1 /conf/geometry-topology-extension/rcc8-query-functions
 *
 * Requirement: /req/geometry-topology-extension/rcc8-query-functions
 * Implementations shall support geof:rcc8eq, geof:rcc8dc, geof:rcc8ec,
 * geof:rcc8po, geof:rcc8tppi, geof:rcc8tpp, geof:rcc8ntpp,
 * geof:rcc8ntppi as SPARQL extension functions, consistent with their
 * corresponding DE-9IM intersection patterns, as defined by Simple
 * Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each
 * of the following functions returns the correct result for a test
 * dataset when using the specified serialization and version:
 * geof:rcc8eq, geof:rcc8dc, geof:rcc8ec, geof:rcc8po, geof:rcc8tppi,
 * geof:rcc8tpp, geof:rcc8ntpp, geof:rcc8ntppi.
 *
 * c.) Reference: Clause 9.5 Req 24
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8QueryFunctionsRcc8ntppiTest {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * Inference WKT model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_WKT_MODEL;

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        init(TopologyRegistryLevel.DEFAULT);
        /**
         * Setup inference model.
         */
        DEFAULT_WKT_MODEL = ModelFactory.createDefaultModel();
        Model schema = FileManager.get().loadModel("http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf");
        /**
         * The use of OWL reasoner can bind schema with existing test data.
         */
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        INF_WKT_MODEL = ModelFactory.createInfModel(reasoner, DEFAULT_WKT_MODEL);
        INF_WKT_MODEL.read(TestDataLocation.SAMPLE_WKT);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private ArrayList expectedList;
    private ArrayList actualList;

    @Before
    public void setUp() {
        this.expectedList = new ArrayList<>();
        this.actualList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        this.actualList.clear();
        this.expectedList.clear();
    }

    @Test
    public void positiveTest() {

        this.expectedList.add("http://ntu.ac.uk/ont/geo#C");

        String Q1 = "SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " ?aWKT geo:rcc8ntppi \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.4 34.3, -83.3 34.3, -83.3 34.4, -83.4 34.4, -83.4 34.3))^^http://www.opengis.net/ont/geosparql#wktLiteral\" ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("?place");
                this.actualList.add(resource.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

    @Test
    public void negativeTest() {

        String Q1 = "SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " ?aWKT geo:rcc8ntppi \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.1, -83.4 34.1, -83.4 34.3, -83.6 34.3, -83.6 34.1))^^http://www.opengis.net/ont/geosparql#wktLiteral\" ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("?place");
                assertNull("should be null", resource.toString());
            }
        }
    }

}
