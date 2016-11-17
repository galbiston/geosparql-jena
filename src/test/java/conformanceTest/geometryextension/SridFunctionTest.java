/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.*;
import static implementation.functionregistry.RegistryLoader.load;
import java.util.ArrayList;
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
 * A.3.1.5 /conf/geometry-extension/srid-function
 *
 * Requirement: /req/geometry-extension/srid-function
 * Implementations shall support geof:getSRID as a SPARQL extension
 * function.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a SPARQL query involving the
 * geof:getSRID function returns the correct result for a test dataset
 * when using the specified serialization and version.
 *
 * c.) Reference: Clause 8.7 Req 20
 *
 * d.) Test Type: Capabilities
 */
public class SridFunctionTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
        initWktModel();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private ArrayList expectedList;
    private ArrayList actualList;

    @Before
    public void setUp() {
        this.expectedList = new ArrayList<>();
        this.actualList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        this.actualList.clear();
        this.expectedList.clear();
    }

    @Test
    public void positiveTest() {

        this.expectedList.add("http://www.opengis.net/def/crs/OGC/1.3/CRS84");

        String Q1 = "SELECT ((geof:getsrid ( ?aWKT )) AS ?srid) WHERE{"
                + " ex:C ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        this.actualList = literalQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
