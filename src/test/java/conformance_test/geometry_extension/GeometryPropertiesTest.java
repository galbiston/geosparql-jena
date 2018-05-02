/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import conformance_test.ConformanceTestSuite;
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
public class GeometryPropertiesTest {

    private static final InfModel SPATIAL_RELATIONS_MODEL = ConformanceTestSuite.initSpatialRelationsModel();

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
    public void dimensionTest() {
        System.out.println("Dimension");
        String expResult = "0^^http://www.w3.org/2001/XMLSchema#integer";

        String queryString = "SELECT ?dimension WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:dimension ?dimension ."
                + "}";
        String result = ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void coordinateDimensionTest() {
        System.out.println("Coordinate Dimension");
        String expResult = "2^^http://www.w3.org/2001/XMLSchema#integer";

        String queryString = "SELECT ?coordinateDimension WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:coordinateDimension ?coordinateDimension ."
                + "}";
        String result = ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void spatialDimensionTest() {
        System.out.println("Spatial Dimension");
        String expResult = "2^^http://www.w3.org/2001/XMLSchema#integer";

        String queryString = "SELECT ?spatialDimension WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:spatialDimension ?spatialDimension ."
                + "}";
        String result = ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isEmptyTest() {
        System.out.println("Is Empty");
        String expResult = "false^^http://www.w3.org/2001/XMLSchema#boolean";

        String queryString = "SELECT ?isEmpty WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:isEmpty ?isEmpty ."
                + "}";
        String result = ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void isSimpleTest() {
        System.out.println("Is Simple");
        String expResult = "true^^http://www.w3.org/2001/XMLSchema#boolean";

        String queryString = "SELECT ?isSimple WHERE{"
                + " feat:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:isSimple ?isSimple ."
                + "}";
        String result = ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
