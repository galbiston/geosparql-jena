/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.core;

import static conformanceTest.ConformanceTestSuite.*;
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
 * A.1.2 /conf/core/spatial-object-class
 * Requirement: /req/core/spatial-object-class
 *
 * Implementations shall allow the RDFS class geo:SpatialObject to be
 * used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:SpatialObject
 * return the correct result on a test dataset.
 *
 * c.) Reference: Clause 6.2.1 Req 2
 *
 * d.) Test Type: Capabilities
 */
public class SpatialObjectClassTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
        initWktModel();
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

        this.expectedList.add("http://example.org/ApplicationSchema#G");
        this.expectedList.add("http://example.org/ApplicationSchema#F");
        this.expectedList.add("http://example.org/ApplicationSchema#E");
        this.expectedList.add("http://example.org/ApplicationSchema#D");
        this.expectedList.add("http://example.org/ApplicationSchema#C");
        this.expectedList.add("http://example.org/ApplicationSchema#B");
        this.expectedList.add("http://example.org/ApplicationSchema#A");
        this.expectedList.add("http://example.org/ApplicationSchema#GExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#FExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#EExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#DExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#CExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#BExactGeom");
        this.expectedList.add("http://example.org/ApplicationSchema#AExactGeom");
        String Q1 = "SELECT ?feature WHERE{"
                + " ?feature rdf:type geo:SpatialObject ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("?feature");
                this.actualList.add(resource.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }
}
