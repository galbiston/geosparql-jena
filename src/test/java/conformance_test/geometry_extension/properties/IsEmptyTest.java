/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.properties;

import conformance_test.TestQuerySupport;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 *
 * A.3.1.3 /conf/geometry-extension/geometry-properties
 *
 * Requirement: /req/geometry-extension/geometry-properties Implementations
 * shall allow the properties geo:dimension, geo:coordinateDimension,
 * geo:spatialDimension, geo:isEmpty, geo:isSimple, geo:hasSerialization to be
 * used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
 *
 * c.) Reference: Clause 8.4 Req 9
 *
 * d.) Test Type: Capabilities
 */
public class IsEmptyTest {

    private static final InfModel SAMPLE_DATA_MODEL = TestQuerySupport.getSampleData_WKT();

    @BeforeClass
    public static void setUpClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void isEmptyBothBoundTest() {
        System.out.println("Is Empty Both Bound");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#Empty");

        String queryString = "SELECT ?feature WHERE{"
                + " feat:Empty geo:hasGeometry ?aGeom ."
                + " BIND(feat:Empty AS ?feature) ."
                + " ?aGeom geo:isEmpty \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> ."
                + "}";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isEmptyBothBoundTest2() {
        System.out.println("Is Empty Both Bound2");

        List<String> expResult = new ArrayList<>();

        String queryString = "SELECT ?feature WHERE{"
                + " feat:Empty geo:hasGeometry ?aGeom ."
                + " BIND(feat:Empty AS ?feature) ."
                + " ?aGeom geo:isEmpty \"false\"^^<http://www.w3.org/2001/XMLSchema#boolean> ."
                + "}";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isEmptyObjectUnboundTest() {
        System.out.println("Is Empty Object Unbound");

        String expResult = "false^^http://www.w3.org/2001/XMLSchema#boolean";

        String queryString = "SELECT ?isEmpty WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:isEmpty ?isEmpty ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isEmptySubjectUnboundTest() {
        System.out.println("Is Empty Subject Unbound");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#Empty");

        String queryString = "SELECT ?feature WHERE{"
                + " ?feature geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:isEmpty \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> ."
                + "}ORDER BY ?feature";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isEmptyBothUnboundTest() {
        System.out.println("Is Empty Both Unbound");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#B false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#C false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#C2 false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#D false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#E false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#Empty true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#F false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#G false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#H false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#I false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#J false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#K false^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#L false^^http://www.w3.org/2001/XMLSchema#boolean");

        String queryString = "SELECT ?feature ?isEmpty WHERE{"
                + " ?feature geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:isEmpty ?isEmpty ."
                + "}ORDER BY ?feature";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
