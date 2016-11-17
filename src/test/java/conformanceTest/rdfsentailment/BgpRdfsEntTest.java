/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.rdfsentailment;

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
 * A.5.1.1 /conf/rdfsentailmentextension/bgp-rdfs-ent
 *
 * Requirement: /req/rdfs-entailment-extension/bgp-rdfs-ent
 * Basic graph pattern matching shall use the semantics defined by the
 * RDFS Entailment Regime [W3C SPARQL Entailment].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving
 * entailed RDF triples returns the correct result for a test dataset
 * using the specified serialization, version and relation_family.
 *
 * c.) Reference: Clause 10.2 Req 25
 *
 * d.) Test Type: Capabilities
 */
public class BgpRdfsEntTest {

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

        this.expectedList.add("http://example.org/ApplicationSchema#F");
        this.expectedList.add("http://example.org/ApplicationSchema#E");
        this.expectedList.add("http://example.org/ApplicationSchema#D");
        this.expectedList.add("http://example.org/ApplicationSchema#C");
        this.expectedList.add("http://example.org/ApplicationSchema#B");
        this.expectedList.add("http://example.org/ApplicationSchema#A");

        String Q1 = "SELECT ?feature WHERE{"
                + " ?feature rdf:type geo:Feature ."
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
