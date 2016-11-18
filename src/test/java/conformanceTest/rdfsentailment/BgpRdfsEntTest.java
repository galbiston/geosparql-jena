/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.rdfsentailment;

import static conformanceTest.ConformanceTestSuite.*;
import static implementation.functionregistry.RegistryLoader.load;
import java.util.ArrayList;
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

        this.expectedList.add("http://example.org/ApplicationSchema#F");
        this.expectedList.add("http://example.org/ApplicationSchema#E");
        this.expectedList.add("http://example.org/ApplicationSchema#D");
        this.expectedList.add("http://example.org/ApplicationSchema#C");
        this.expectedList.add("http://example.org/ApplicationSchema#B");
        this.expectedList.add("http://example.org/ApplicationSchema#A");

        String Q1 = "SELECT ?feature WHERE{"
                + " ?feature rdf:type geo:Feature ."
                + "}";
        this.actualList = resourceQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
