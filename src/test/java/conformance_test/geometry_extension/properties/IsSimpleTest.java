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
public class IsSimpleTest {

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
    public void isSimpleBothBoundTest() {
        System.out.println("Is Simple Both Bound");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");

        String queryString = "SELECT ?feature WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " BIND(feat:A AS ?feature) ."
                + " ?aGeom geo:isSimple \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> ."
                + "}";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isSimpleBothBoundTest2() {
        System.out.println("Is Simple Both Bound2");

        List<String> expResult = new ArrayList<>();

        String queryString = "SELECT ?feature WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " BIND(feat:A AS ?feature) ."
                + " ?aGeom geo:isSimple \"false\"^^<http://www.w3.org/2001/XMLSchema#boolean> ."
                + "}";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isSimpleObjectUnboundTest() {
        System.out.println("Is Simple Object Unbound");

        String expResult = "true^^http://www.w3.org/2001/XMLSchema#boolean";

        String queryString = "SELECT ?isSimple WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:isSimple ?isSimple ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isSimpleSubjectUnboundTest() {
        System.out.println("Is Simple Subject Unbound");

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

        String queryString = "SELECT ?feature WHERE{"
                + " ?feature geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:isSimple \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> ."
                + "}ORDER BY ?feature";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isSimpleBothUnboundTest() {
        System.out.println("Is Simple Both Unbound");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#B true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#C true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#C2 true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#D true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#E true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#Empty true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#F true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#G true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#H true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#I true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#J true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#K true^^http://www.w3.org/2001/XMLSchema#boolean");
        expResult.add("http://example.org/Feature#L true^^http://www.w3.org/2001/XMLSchema#boolean");

        String queryString = "SELECT ?feature ?isSimple WHERE{"
                + " ?feature geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:isSimple ?isSimple ."
                + "}ORDER BY ?feature";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
