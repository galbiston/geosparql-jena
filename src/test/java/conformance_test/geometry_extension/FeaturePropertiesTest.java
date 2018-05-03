/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

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
 * A.3.1.2 /conf/geometry-extension/feature-properties
 *
 * Requirement: /req/geometry-extension/feature-properties Implementations shall
 * allow the properties geo:hasGeometry and geo:hasDefaultGeometry to be used in
 * SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 8.3 Req 8
 *
 * d.) Test Type: Capabilities
 */
public class FeaturePropertiesTest {

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
    public void testHasGeometry() {
        System.out.println("Has Geometry");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> Point(60 60)^^http://www.opengis.net/ont/geosparql#wktLiteral";
        String queryString = "SELECT ?aWKT WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void testHasDefaultGeometry() {
        System.out.println("Has Default Geometry");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> Point(60 60)^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?aWKT WHERE{"
                + " feat:A geo:hasDefaultGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
