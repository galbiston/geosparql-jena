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
 * * A.3.3.2 /conf/geometry-extension/gml-literal-empty
 *
 * Requirement: /req/geometry-extension/gml-literal-empty An empty
 * geo:gmlLiteral shall be interpreted as an empty geometry.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving empty geo:gmlLiteral values
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.6.1 Req 16
 *
 * d.) Test Type: Capabilities
 */
public class GmlLiteralEmptyTest {

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
    public void emptyCoordinatesTest() {

        String expResult = "true^^http://www.w3.org/2001/XMLSchema#boolean";

        String queryString = "SELECT ?isEmpty WHERE{"
                + "geom:PointEmptyA geo:isEmpty ?isEmpty ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void emptyLiteralTest() {

        String expResult = "true^^http://www.w3.org/2001/XMLSchema#boolean";

        String queryString = "SELECT ?isEmpty WHERE{"
                + "geom:PointEmptyB geo:isEmpty ?isEmpty ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
