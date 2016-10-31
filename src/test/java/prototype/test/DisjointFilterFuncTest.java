/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.test;

import vocabulary.RDFDataLocation;
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
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Prefixes;

/**
 *
 * @author haozhechen
 */
public class DisjointFilterFuncTest {

    private static Model MODEL;
    private static InfModel INF_MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisjointFilterFuncTest.class);

    public DisjointFilterFuncTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        MODEL = ModelFactory.createDefaultModel();
        LOGGER.info("Before Reading Data");
        MODEL.read(RDFDataLocation.SAMPLE);
        LOGGER.info("After Reading Data");

        INF_MODEL = ModelFactory.createRDFSModel(MODEL);

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
     * Test of exec method, of class SFDisjointFilterFunc.
     */
    @Test
    public void testExec() {
        System.out.println("exec");
        String queryString = "SELECT ?b WHERE{ "
                + "ntu:A ntu:hasPointGeometry ?aGeom . ?aGeom gml:asGML ?aGML . "
                + "{?b ntu:hasPointGeometry ?bGeom . ?bGeom gml:asGML ?bGML .} "
                + "UNION { ?b ntu:hasExactGeometry ?bGeom . ?bGeom gml:asGML ?bGML .  }"
                + "FILTER ( geof:sfDisjoint(?aGML, ?bGML) )"
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());
        FunctionRegistry.get().put("http://www.opengis.net/def/function/geosparql/sfDisjoint", geof.topo.sf.SFDisjointFilterFunc.class);

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), INF_MODEL)) {
            ResultSet rs = qExec.execSelect();
            LOGGER.info("success in executing result!");
            ResultSetFormatter.out(rs);
        }
    }

    @Test
    public void quickTest() {
        System.out.println("Qucik Test");
        Resource s = ResourceFactory.createResource("http://ntu.ac.uk/ont/geo#A");
        Property p = ResourceFactory.createProperty("http://www.opengis.net/ont/geosparql#hasGeometry");
        boolean isPresent = INF_MODEL.contains(s, p);

        Resource a = INF_MODEL.createResource("http://ntu.ac.uk/ont/geo#A");
        System.out.println(a.listProperties().toList());

        assertEquals(true, isPresent);

    }

}
