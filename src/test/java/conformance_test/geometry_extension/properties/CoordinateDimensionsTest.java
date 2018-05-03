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
public class CoordinateDimensionsTest {

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
    public void coordinateDimensionBothBoundTest() {
        System.out.println("Coordinate Dimension Both Bound");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");

        String queryString = "SELECT ?feature WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " BIND(feat:A AS ?feature) ."
                + " ?aGeom geo:coordinateDimension \"2\"^^<http://www.w3.org/2001/XMLSchema#integer> ."
                + "}";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void coordinateDimensionObjectUnboundTest() {
        System.out.println("Coordinate Dimension Object Unbound");

        String expResult = "2^^http://www.w3.org/2001/XMLSchema#integer";

        String queryString = "SELECT ?coordinateDimension WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:coordinateDimension ?coordinateDimension ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void coordinateDimensionSubjectUnboundTest() {
        System.out.println("Coordinate Dimension Subject Unbound");

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
                + " ?aGeom geo:coordinateDimension \"2\"^^<http://www.w3.org/2001/XMLSchema#integer> ."
                + "}ORDER BY ?feature";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void coordinateDimensionBothUnboundTest() {
        System.out.println("Coordinate Dimension Both Unbound");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#B 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#C 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#C2 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#D 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#E 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#Empty 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#F 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#G 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#H 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#I 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#J 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#K 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#L 2^^http://www.w3.org/2001/XMLSchema#integer");

        String queryString = "SELECT ?feature ?coordinateDimension WHERE{"
                + " ?feature geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:coordinateDimension ?coordinateDimension ."
                + "}ORDER BY ?feature";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
