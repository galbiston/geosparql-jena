/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;

/**
 *
 * @author haozhechen
 */
public class ConformanceTestSuite {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * Inference WKT model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_WKT_MODEL;

    /**
     * Default GML model - with no inference support.
     */
    public static Model DEFAULT_GML_MODEL;

    /**
     * Inference GML model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_GML_MODEL;

    /**
     * This negative model facilitates the test for empty geometries and the GML
     * literal without a specified SRID.
     */
    public static Model TEST_GML_MODEL;

    static {

        initWktModel();
        initGmlModel();

    }

    /**
     * This method initialize all the WKT test models, need to be called before
     * query execution.
     */
    public static void initWktModel() {
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
        INF_WKT_MODEL.read(RDFDataLocation.SAMPLE_WKT);

    }

    /**
     * This method initialize all the GML test models, need to be called before
     * query execution.
     */
    public static void initGmlModel() {
        /**
         * Setup inference model.
         */
        DEFAULT_GML_MODEL = ModelFactory.createDefaultModel();
        Model schema = FileManager.get().loadModel("http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf");
        /**
         * The use of OWL reasoner can bind schema with existing test data.
         */
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        INF_GML_MODEL = ModelFactory.createInfModel(reasoner, DEFAULT_GML_MODEL);
        INF_GML_MODEL.read(RDFDataLocation.SAMPLE_GML);

    }

}
