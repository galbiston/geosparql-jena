/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.wkt;

import static conformance_test.ConformanceTestSuite.initWktEmptyModel;
import static conformance_test.ConformanceTestSuite.literalQuery;
import implementation.function_registry.RegistryLoader;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 *
 * A.3.2.4 /conf/geometry-extension/wkt-literal-empty
 *
 * Requirement: /req/geometry-extension/wkt-literal-empty An empty RDFS Literal
 * of type geo:wktLiteral shall be interpreted as an empty geometry.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving empty geo:wktLiteral values
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.5.1 Req 13
 *
 * d.) Test Type: Capabilities
 */
public class WktLiteralEmptyTest {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * This negative model facilitates the test for empty geometries and the WKT
     * literal without a specified SRID.
     */
    public static InfModel TEST_WKT_MODEL;

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();
        infModel = initWktEmptyModel();
    }

    private static InfModel infModel;

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
    public void geometryPositiveTest() {

        List<String> expResult = Arrays.asList("true^^http://www.w3.org/2001/XMLSchema#boolean");

        String queryString = "SELECT ?aWKT WHERE{"
                + " ex:A ex:hasExactGeometry ?geom ."
                + " ?geom geo:isEmpty ?aWKT ."
                + "}";
        List<String> result = literalQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void geometryNegativeTest() {

        List<String> expResult = Arrays.asList("false^^http://www.w3.org/2001/XMLSchema#boolean");

        String queryString = "SELECT ?aWKT WHERE{"
                + " ex:B ex:hasExactGeometry ?geom ."
                + " ?geom geo:isEmpty ?aWKT ."
                + "}";
        List<String> result = literalQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

}
