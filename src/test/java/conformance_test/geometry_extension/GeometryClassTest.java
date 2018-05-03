/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import conformance_test.TestQuerySupport;
import java.util.ArrayList;
import java.util.List;
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
 * A.3.1.1 /conf/geometry-extension/geometry-class
 *
 * Requirement: /req/geometry-extension/geometry-class Implementations shall
 * allow the RDFS class geo:Geometry to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:Geometry return the
 * correct result on a test dataset.
 *
 * c.) Reference: Clause 8.2.1 Req 7 d.) Test Type: Capabilities
 */
public class GeometryClassTest {

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
    public void geometryClassTest() {
        System.out.println("Geometry Class");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringF");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PointA");
        expResult.add("http://example.org/Geometry#PointB");
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");
        expResult.add("http://example.org/Geometry#PointEmpty");
        expResult.add("http://example.org/Geometry#PolygonH");
        expResult.add("http://example.org/Geometry#PolygonI");
        expResult.add("http://example.org/Geometry#PolygonJ");
        expResult.add("http://example.org/Geometry#PolygonK");
        expResult.add("http://example.org/Geometry#PolygonL");

        String queryString = "SELECT ?geometry WHERE{"
                + " ?geometry rdf:type geo:Geometry ."
                + "}ORDER BY ?geometry";

        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
