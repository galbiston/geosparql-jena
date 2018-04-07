/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.wkt;

import static conformance_test.ConformanceTestSuite.*;
import implementation.datatype.WKTDatatype;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 *
 * A.3.2.1 /conf/geometry-extension/wkt-literal
 *
 * Requirement: /req/geometry-extension/wkt-literal All RDFS Literals of type
 * geo:wktLiteral shall consist of an optional URI identifying the coordinate
 * reference system followed by Simple Features Well Known Text (WKT) describing
 * a geometric value, Valid geo:wktLiterals are formed by concatenating a valid,
 * absolute URI as defined in [RFC 2396], one or more spaces (Unicode U+0020
 * character) as a separator, and a WKT string as defined in Simple Features
 * [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:wktLiteral values return
 * the correct result for a test dataset.
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

        ArrayList<Literal> expResult = new ArrayList<>();
        expResult.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)", WKTDatatype.THE_WKT_DATATYPE));

        String queryString = "SELECT ?aWKT WHERE{"
                + " ex:A ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        List<Literal> result = literalQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

}
