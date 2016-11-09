/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.rdfsentailment;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import static implementation.functionregistry.RegistryLoader.load;
import implementation.support.Prefixes;
import java.util.ArrayList;
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

/**
 *
 * @author haozhechen
 *
 * A.5.2.1 /conf/rdfs-entailment-extension/wkt-geometry-types
 *
 * Requirement: /req/rdfs-entailment-extension/wkt-geometry-types
 * Implementations shall support graph patterns involving terms from an
 * RDFS/OWL class hierarchy of geometry types consistent with the one in
 * the specified version of Simple Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving WKT
 * Geometry types returns the correct result for a test dataset using
 * the specified version of Simple Features.
 *
 * c.) Reference: Clause 10.3.1 Req 26
 *
 * d.) Test Type: Capabilities
 */
public class WktGeometryTypesTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
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

        this.expectedList.add("http://ntu.ac.uk/ont/geo#FExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#EExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#DExactGeom");
        this.expectedList.add("http://ntu.ac.uk/ont/geo#CExactGeom");

        String Q1 = "SELECT ?geometry WHERE{"
                + " ?geometry rdf:type sf:Polygon ."
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
