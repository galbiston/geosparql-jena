/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.property_functions.geometry_property;

import conformance_test.ConformanceTestSuite;
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
 */
public class GetDimensionTest {

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

    /**
     * Test of get method, of class GetDimension.
     */
    @Test
    public void testGetDimension() {
        System.out.println("Get Dimension");
        String queryString = "SELECT ?dimension ?empty ?simple ?coordinateDimension WHERE{"
                + " BIND(geom:PointB AS ?aGeom) ."
                + " ?aGeom geo:dimension ?dimension ."
                + " ?aGeom geo:isEmpty ?empty ."
                + " ?aGeom geo:isSimple ?simple ."
                + " ?aGeom geo:coordinateDimension ?coordinateDimension . "
                + "}";

        String expResult = "0^^http://www.w3.org/2001/XMLSchema#integer false^^http://www.w3.org/2001/XMLSchema#boolean true^^http://www.w3.org/2001/XMLSchema#boolean 2^^http://www.w3.org/2001/XMLSchema#integer";
        String result = ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
