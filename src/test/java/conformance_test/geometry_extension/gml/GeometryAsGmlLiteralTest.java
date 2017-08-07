/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.gml;

import static conformance_test.ConformanceTestSuite.*;
import implementation.GeoSPARQLModel;
import implementation.datatype.GMLDatatype;

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
 * @author haozhechen
 *
 * A.3.3.4 /conf/geometry-extension/geometry-as-gml-literal
 *
 * Requirement: /req/geometry-extension/geometry-as-gml-literal Implementations
 * shall allow the RDF property geo:asGML to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving the geo:asGML property return
 * the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.6.2 Req 18
 *
 * d.) Test Type: Capabilities
 */
public class GeometryAsGmlLiteralTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        
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

        List<Literal> expResult = new ArrayList<>();
        expResult.add(ResourceFactory.createTypedLiteral("<gml:Point srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:coordinates>-83.4,34.4</gml:coordinates></gml:Point>", GMLDatatype.THE_GML_DATATYPE));

        String queryString = "SELECT ?aGML WHERE{"
                + " ex:A ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asGML ?aGML ."
                + "}";
        List<Literal> result = literalQuery(queryString, infModel);

        System.out.println("Exp: " + expResult);
        System.out.println("Res: " + result);

        assertEquals(expResult, result);
    }

}
