/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import conformanceTest.RDFDataLocation;
import static implementation.functionregistry.RegistryLoader.load;
import implementation.support.Prefixes;
import java.util.ArrayList;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 *
 * A.3.2.2 /conf/geometry-extension/wkt-literal-default-srs
 *
 * Requirement: /req/geometry-extension/wkt-literal-default-srs
 * The URI <http://www.opengis.net/def/crs/OGC/1.3/CRS84> shall be
 * assumed as the spatial reference system for geo:wktLiterals that do
 * not specify an explicit spatial reference system URI.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:wktLiteral values
 * without an explicit encoded spatial reference system URI return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 8.5.1 Req 11
 *
 * d.) Test Type: Capabilities
 */
public class WktLiteralDefaultSrsTest {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * This negative model facilitates the test for empty geometries and the WKT
     * literal without a specified SRID.
     */
    public static Model TEST_WKT_MODEL;

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
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
        TEST_WKT_MODEL = ModelFactory.createInfModel(reasoner, DEFAULT_WKT_MODEL);
        TEST_WKT_MODEL.read(RDFDataLocation.SAMPLE_WKT_EMPTY);
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

        this.expectedList.add("http://www.opengis.net/def/crs/OGC/1.3/CRS84");

        String Q1 = "SELECT ((geof:getsrid ( ?aWKT )) AS ?srid) WHERE{"
                + " ex:B ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), TEST_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Literal literal = solution.getLiteral("?srid");
                this.actualList.add(literal.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
