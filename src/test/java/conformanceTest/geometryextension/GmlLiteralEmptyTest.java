/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.*;
import implementation.functionregistry.RegistryLoader;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 *
 * * A.3.3.2 /conf/geometry-extension/gml-literal-empty
 *
 * Requirement: /req/geometry-extension/gml-literal-empty An empty
 * geo:gmlLiteral shall be interpreted as an empty geometry.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving empty geo:gmlLiteral values
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.6.1 Req 16
 *
 * d.) Test Type: Capabilities
 */
public class GmlLiteralEmptyTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();

        infModel = initGmlEmptyModel();
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
    public void positiveTest() {

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("urn:ogc:def:crs:OGC::CRS84");

        String Q1 = "SELECT ((geof:getsrid ( ?aGML )) AS ?srid) WHERE{"
                + " ex:B ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asGML ?aGML ."
                + "}";
        ArrayList<String> actualList = literalQuery(Q1, infModel);
        assertEquals(expectedList, actualList);
    }

}
