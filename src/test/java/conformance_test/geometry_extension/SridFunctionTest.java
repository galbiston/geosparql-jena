/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import static conformance_test.ConformanceTestSuite.*;
import implementation.function_registry.RegistryLoader;
import java.util.Arrays;
import java.util.List;
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
 * A.3.1.5 /conf/geometry-extension/srid-function
 *
 * Requirement: /req/geometry-extension/srid-function Implementations shall
 * support geof:getSRID as a SPARQL extension function.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a SPARQL query involving the geof:getSRID
 * function returns the correct result for a test dataset when using the
 * specified serialization and version.
 *
 * c.) Reference: Clause 8.7 Req 20
 *
 * d.) Test Type: Capabilities
 *
 * Additional Information, page 22 of OGC GeoSPARQL Standard 11-052r4:
 *
 * Clause 8.7.10 Function: geof:getsrid geof:getSRID (geom:ogc:geomLiteral):
 * xsd:anyURI Returns the spatial reference system URI for geom.
 *
 */
public class SridFunctionTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();
        infModel = initWktModel();
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

        List<String> expectedList = Arrays.asList("http://www.opengis.net/def/crs/OGC/1.3/CRS84");

        String Q1 = "SELECT ((geof:getSRID ( ?aWKT )) AS ?srid) WHERE{"
                + " ex:C ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        List<String> actualList = literalQuery(Q1, infModel);

        assertEquals(expectedList, actualList);
    }

}
