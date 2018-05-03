/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.gml;

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
 * * A.3.3.1 /conf/geometry-extension/gml-literal
 *
 * Requirement: /req/geometry-extension/gml-literal All geo:gmlLiterals shall
 * consist of a valid element from the GML schema that implements a subtype of
 * GM_Object as defined in [OGC 07-036].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:gmlLiteral values return
 * the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.6.1 Req 15
 *
 * d.) Test Type: Capabilities
 */
public class GmlLiteralTest {

    private static final InfModel SAMPLE_DATA_MODEL = TestQuerySupport.getSampleData_GML();

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

    //TODO - Test for valid elements of subtype schema.
}
