/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static main.Main.init;
import main.TopologyRegistryLevel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import prototype.test.TestDataLocation;

/**
 *
 * @author haozhechen
 *
 * A.3.3.4 /conf/geometry-extension/geometry-as-gml-literal
 *
 * Requirement: /req/geometry-extension/geometry-as-gml-literal
 * Implementations shall allow the RDF property geo:asGML to be used in
 * SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving the geo:asGML property
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.6.2 Req 18
 *
 * d.) Test Type: Capabilities
 */
public class GeometryAsGmlLiteralTest {

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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

}
