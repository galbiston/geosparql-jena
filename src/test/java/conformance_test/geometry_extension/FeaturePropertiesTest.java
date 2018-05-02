/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import conformance_test.ConformanceTestSuite;
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

    private static final InfModel SPATIAL_RELATIONS_MODEL = ConformanceTestSuite.initSpatialRelationsModel();

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
        String result = ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);
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
        String result = ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void featureClassTest() {
        System.out.println("Feature Class");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");
        expResult.add("http://example.org/Feature#B");
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Feature#D");
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#Empty");
        expResult.add("http://example.org/Feature#F");
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Feature#K");
        expResult.add("http://example.org/Feature#L");
        expResult.add("http://example.org/Feature#X");

        String queryString = "SELECT ?feature WHERE{"
                + " ?feature rdf:type geo:Feature ."
                + "}ORDER BY ?feature";

        List<String> result = ConformanceTestSuite.queryMany(queryString, SPATIAL_RELATIONS_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
