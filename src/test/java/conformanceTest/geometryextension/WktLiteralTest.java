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
 * A.3.2.1 /conf/geometry-extension/wkt-literal
 *
 * Requirement: /req/geometry-extension/wkt-literal
 * All RDFS Literals of type geo:wktLiteral shall consist of an optional
 * URI identifying the coordinate reference system followed by Simple
 * Features Well Known Text (WKT) describing a geometric value, Valid
 * geo:wktLiterals are formed by concatenating a valid, absolute URI as
 * defined in [RFC 2396], one or more spaces (Unicode U+0020 character)
 * as a separator, and a WKT string as defined in Simple Features [ISO
 * 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:wktLiteral values
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.5.1 Req 10
 *
 * d.) Test Type: Capabilities
 */
public class WktLiteralTest {

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

        this.expectedList.add("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)^^http://www.opengis.net/ont/geosparql#wktLiteral");

        String Q1 = "SELECT ?aWKT WHERE{"
                + " ex:A ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        this.actualList = literalQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
