/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.rdfs_entailment_extension;

import conformance_test.TestQuerySupport;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 *
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

    private static final InfModel SAMPLE_DATA_MODEL = TestQuerySupport.getSampleData_WKT();

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

    //TODO - RDFS Inferencing
}
