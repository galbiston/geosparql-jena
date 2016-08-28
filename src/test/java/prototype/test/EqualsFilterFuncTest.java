/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.test;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.ReasonerVocabulary;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Prefixes;

/**
 *
 * @author haozhechen
 */
public class EqualsFilterFuncTest {

    private static Model MODEL;
    private static InfModel INF_MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(EqualsFilterFuncTest.class);

    public EqualsFilterFuncTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        MODEL = ModelFactory.createDefaultModel();
        //Model schema = FileManager.get().loadModel("http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf");
        //Model schema = FileManager.get().loadModel(TestDataLocation.SCHEMA);
        //MODEL.read(TestDataLocation.SAMPLE);
        //MODEL = FileManager.get().loadModel(TestDataLocation.SAMPLE);
        //==========================================================
        //==========================================================
        //==========================================================
        //RDFS Reasoner
        Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
        reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_DEFAULT);
        //==========================================================
        //==========================================================
        //==========================================================
        //OWL Reasoner
        //Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        //reasoner = reasoner.bindSchema(schema);
        //==========================================================
        //==========================================================
        //==========================================================
        //Rule Reasoner
        //String rule1 = "[r1: (<http://ntu.ac.uk/ont/geo#A> <http://www.opengis.net/ont/geosparql#hasGeometry> ?b) -> (<http://ntu.ac.uk/ont/geo#A> <http://ntu.ac.uk/ont/geo#hasExactGeometry> ?b) ]";
        //GenericRuleReasoner rdfsReasoner = (GenericRuleReasoner) ReasonerRegistry.getRDFSReasoner();
        //List<Rule> customRules = new ArrayList<>(rdfsReasoner.getRules());
        //customRules.add(Rule.parseRule(rule1));
        //Reasoner reasoner = new GenericRuleReasoner(customRules);

        //==========================================================
        //==========================================================
        //==========================================================
        INF_MODEL = ModelFactory.createInfModel(reasoner, MODEL);
        INF_MODEL.read(TestDataLocation.SAMPLE);
        INF_MODEL.prepare();
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
     * Test of exec method, of class EqualsFilterFunc.
     */
    @Test
    public void testExec() {

        System.out.println("exec");
        String queryString = "SELECT ?b WHERE{ "
                + "?b geo:hasGeometry ?bGeom."
                //                + "ntu:A ntu:hasPointGeometry ?aGeom . ?aGeom gml:asGML ?aGML . "
                //                + "?b ntu:hasPointGeometry ?bGeom . ?bGeom gml:asGML ?bGML . "
                //                + "FILTER ( ext:sfEquals(?aGML, ?bGML) )"
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), INF_MODEL)) {
            ResultSet rs = qExec.execSelect();
            //LOGGER.info("Test Query: \n{}", qExec.getQuery().toString());
            ResultSetFormatter.out(rs);
        }
    }

    @Ignore
    @Test
    public void InfTest() {
        System.out.println("Inference Model Test");

        //Resource res = INF_MODEL.getResource("http://ntu.ac.uk/ont/geo#RoadNode");
        Resource feature = INF_MODEL.getResource("http://www.opengis.net/ont/geosparql#Feature");
        Property ntu = INF_MODEL.getProperty("http://ntu.ac.uk/ont/geo#hasExactGeometry");
        Property geo = INF_MODEL.getProperty("http://www.opengis.net/ont/geosparql#hasGeometry");
        LOGGER.info("geo property: {}", geo.toString());
        System.out.println("nForce *:");
        printStatements(INF_MODEL, feature, null, null);
    }

    public void printStatements(Model m, Resource s, Property p, Resource o) {
        for (StmtIterator i = m.listStatements(s, p, o); i.hasNext();) {
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }

}
