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
public class DimensionsTest {

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
    public void dimensionBothBoundTest() {
        System.out.println("Dimension Both Bound");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");

        String queryString = "SELECT ?feature WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " BIND(feat:A AS ?feature) ."
                + " ?aGeom geo:dimension \"0\"^^<http://www.w3.org/2001/XMLSchema#integer> ."
                + "}";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void dimensionObjectUnboundTest() {
        System.out.println("Dimension Object Unbound");
        String expResult = "0^^http://www.w3.org/2001/XMLSchema#integer";

        String queryString = "SELECT ?dimension WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:dimension ?dimension ."
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void dimensionSubjectUnboundTest() {
        System.out.println("Dimension Subject Unbound");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");
        expResult.add("http://example.org/Feature#B");
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Feature#Empty");

        String queryString = "SELECT ?feature WHERE{"
                + " ?feature geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:dimension \"0\"^^<http://www.w3.org/2001/XMLSchema#integer> ."
                + "}ORDER BY ?feature";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void dimensionBothUnboundTest() {
        System.out.println("Dimension Both Unbound");
        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A 0^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#B 0^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#C 0^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#C2 0^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#D 1^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#E 1^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#Empty 0^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#F 1^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#G 1^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#H 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#I 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#J 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#K 2^^http://www.w3.org/2001/XMLSchema#integer");
        expResult.add("http://example.org/Feature#L 2^^http://www.w3.org/2001/XMLSchema#integer");

        String queryString = "SELECT ?feature ?dimension WHERE{"
                + " ?feature geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:dimension ?dimension ."
                + "}ORDER BY ?feature";
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
