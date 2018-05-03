/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.wkt;

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
 * A.3.2.1 /conf/geometry-extension/wkt-literal
 *
 * Requirement: /req/geometry-extension/wkt-literal All RDFS Literals of type
 * geo:wktLiteral shall consist of an optional URI identifying the coordinate
 * reference system followed by Simple Features Well Known Text (WKT) describing
 * a geometric value, Valid geo:wktLiterals are formed by concatenating a valid,
 * absolute URI as defined in [RFC 2396], one or more spaces (Unicode U+0020
 * character) as a separator, and a WKT string as defined in Simple Features
 * [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:wktLiteral values return
 * the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.5.1 Req 10
 *
 * d.) Test Type: Capabilities
 */
public class WktLiteralTest {

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

    @Test
    public void wktLiteralTest() {

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> Point(60 60)^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?aWKT WHERE{"
                + " geom:PointA geo:asWKT ?aWKT ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
