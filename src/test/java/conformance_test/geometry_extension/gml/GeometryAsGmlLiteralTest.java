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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 *
 * A.3.3.4 /conf/geometry-extension/geometry-as-gml-literal
 *
 * Requirement: /req/geometry-extension/geometry-as-gml-literal Implementations
 * shall allow the RDF property geo:asGML to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving the geo:asGML property return
 * the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.6.2 Req 18
 *
 * d.) Test Type: Capabilities
 */
public class GeometryAsGmlLiteralTest {

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

    @Test
    public void gmlLiteralTest() {
        System.out.println("GML Literal");

        String expResult = "<gml:Point srsName=\\\"http://www.opengis.net/def/crs/EPSG/0/27700\\\" xmlns:gml=\\\"http://www.opengis.net/ont/gml\\\"><gml:pos>-83.4 34.4</gml:pos></gml:Point>^^http://www.opengis.net/ont/geosparql#gmlLiteral";

        String queryString = "SELECT ?aGML WHERE{"
                + " geom:PointA geo:asGML ?aGML ."
                + "}";

        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
