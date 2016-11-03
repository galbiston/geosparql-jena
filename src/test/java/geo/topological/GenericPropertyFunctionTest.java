/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological;

import implementation.functionregistry.RegistryLoader;
import implementation.support.Prefixes;
import implementation.support.RDFDataLocation;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.ReasonerVocabulary;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Greg
 */
public class GenericPropertyFunctionTest {

    public GenericPropertyFunctionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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
     * Test of execEvaluated method, of class GenericPropertyFunction.
     */
    @Test
    public void testExecTemp() {
        System.out.println("execTemp");

        //TODO Fix assert or remove. Debug purposes only.
        RegistryLoader.load();

        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefixes(Prefixes.get());
        model.read(RDFDataLocation.GEOSPARQL_SAMPLE);

        Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
        reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_DEFAULT);
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        String queryString = "PREFIX geo: <http://www.opengis.net/ont/geosparql#> PREFIX my: <http://example.org/ApplicationSchema#> SELECT ?f WHERE { ?f geo:sfOverlaps my:A }";

        QueryExecution qe = QueryExecutionFactory.create(queryString, infModel);

        ResultSet rs = qe.execSelect();

        String result = ResultSetFormatter.asText(rs);

        System.out.println(result);

    }

}
