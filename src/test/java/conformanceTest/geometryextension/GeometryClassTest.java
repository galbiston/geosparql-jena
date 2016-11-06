/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import java.util.ArrayList;
import static main.Main.init;
import main.TopologyRegistryLevel;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import vocabulary.Prefixes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 *
 * A.3.1.1 /conf/geometry-extension/geometry-class
 *
 * Requirement: /req/geometry-extension/geometry-class
 * Implementations shall allow the RDFS class geo:Geometry to be used in
 * SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:Geometry return
 * the correct result on a test dataset.
 *
 * c.) Reference: Clause 8.2.1 Req 7 d.) Test Type: Capabilities
 */
public class GeometryClassTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        init(TopologyRegistryLevel.DEFAULT);
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

        this.expectedList.add("http://ntu.ac.uk/ont/geo#BExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#CExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#AExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#FPointGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#EExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#CPointGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#DPointGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#DExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#FExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#EPointGeom");

        String Q1 = "SELECT ?geometry WHERE{"
                + " ?geometry rdf:type geo:Geometry ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("?geometry");
                this.actualList.add(resource.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
