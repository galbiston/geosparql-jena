/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.gml;

import static conformance_test.ConformanceTestSuite.*;
import implementation.functionregistry.RegistryLoader;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
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
 * * A.3.3.1 /conf/geometry-extension/gml-literal
 *
 * Requirement: /req/geometry-extension/gml-literal All geo:gmlLiterals shall
 * consist of a valid element from the GML schema that implements a subtype of
 * GM_Object as defined in [OGC 07-036].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:gmlLiteral values return
 * the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.6.1 Req 15
 *
 * d.) Test Type: Capabilities
 */
public class GmlLiteralTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();
        infModel = initGmlModel();
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
        expectedList.add("<gml:Point srsName='urn:ogc:def:crs:EPSG::27700' xmlns:gml='http://www.opengis.net/ont/gml'><gml:coordinates>-83.4,34.4</gml:coordinates></gml:Point>^^http://www.opengis.net/ont/geosparql#gmlLiteral");

        String Q1 = "SELECT ?aGML WHERE{"
                + " ex:A ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asGML ?aGML ."
                + "}";
        ArrayList<String> actualList = literalQuery(Q1, infModel);
        assertEquals(expectedList, actualList);
    }

}
