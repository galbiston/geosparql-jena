/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.gml;

import static conformance_test.ConformanceTestSuite.*;
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

        ArrayList<Literal> expResult = new ArrayList<>();
        expResult.add(ResourceFactory.createTypedLiteral("http://www.opengis.net/def/crs/OGC/1.3/CRS84"));

        String queryString = "SELECT ?srid WHERE{"
                + " ex:B ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asGML ?aGML ."
                + "BIND(geof:getSRID( ?aGML ) AS ?srid)"
                + "}";
        List<Literal> result = literalQuery(queryString, infModel);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
