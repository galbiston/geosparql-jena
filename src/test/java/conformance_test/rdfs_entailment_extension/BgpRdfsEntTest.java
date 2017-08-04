/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.rdfs_entailment_extension;

import static conformance_test.ConformanceTestSuite.*;
import implementation.functionregistry.RegistryLoader;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 *
 * A.5.1.1 /conf/rdfsentailmentextension/bgp-rdfs-ent
 *
 * Requirement: /req/rdfs-entailment-extension/bgp-rdfs-ent Basic graph pattern
 * matching shall use the semantics defined by the RDFS Entailment Regime [W3C
 * SPARQL Entailment].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving entailed RDF
 * triples returns the correct result for a test dataset using the specified
 * serialization, version and relation_family.
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
        RegistryLoader.load();
        infModel = initWktModel();
    }
    private static InfModel infModel;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void positiveTest() {

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#G");
        expectedList.add("http://example.org/ApplicationSchema#F");
        expectedList.add("http://example.org/ApplicationSchema#E");
        expectedList.add("http://example.org/ApplicationSchema#D");
        expectedList.add("http://example.org/ApplicationSchema#C");
        expectedList.add("http://example.org/ApplicationSchema#B");
        expectedList.add("http://example.org/ApplicationSchema#A");

        String Q1 = "SELECT ?feature WHERE{"
                + " ?feature rdf:type geo:Feature ."
                + "}";
        ArrayList<String> actualList = resourceQuery(Q1, infModel);
        assertEquals(expectedList, actualList);
    }

}
