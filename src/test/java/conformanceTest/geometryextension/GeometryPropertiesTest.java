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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 *
 * A.3.1.3 /conf/geometry-extension/geometry-properties
 *
 * Requirement: /req/geometry-extension/geometry-properties
 * Implementations shall allow the properties geo:dimension,
 * geo:coordinateDimension, geo:spatialDimension, geo:isEmpty,
 * geo:isSimple, geo:hasSerialization to be used in SPARQL graph
 * patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.4 Req 9
 *
 * d.) Test Type: Capabilities
 */
public class GeometryPropertiesTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
        initWktModel();
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

        this.expectedList.add("0^^http://www.w3.org/2001/XMLSchema#integer");
        this.expectedList.add("0^^http://www.w3.org/2001/XMLSchema#integer");
        this.expectedList.add("0^^http://www.w3.org/2001/XMLSchema#integer");
        this.expectedList.add("false^^http://www.w3.org/2001/XMLSchema#boolean");
        this.expectedList.add("true^^http://www.w3.org/2001/XMLSchema#boolean");

        String Q1 = "SELECT ?dimension ?coordinateDimension ?spatialDimension ?isEmpty ?isSimple WHERE{"
                + " ex:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:dimension ?dimension ."
                + " ?aGeom geo:coordinateDimension ?coordinateDimension ."
                + " ?aGeom geo:spatialDimension ?spatialDimension ."
                + " ?aGeom geo:isEmpty ?isEmpty ."
                + " ?aGeom geo:isSimple ?isSimple ."
                + "}";
        this.actualList = literalQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
