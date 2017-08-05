/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import static conformance_test.ConformanceTestSuite.*;
import implementation.function_registry.RegistryLoader;
import java.util.ArrayList;
import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
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
 * Requirement: /req/geometry-extension/geometry-properties Implementations
 * shall allow the properties geo:dimension, geo:coordinateDimension,
 * geo:spatialDimension, geo:isEmpty, geo:isSimple, geo:hasSerialization to be
 * used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties return the
 * correct result for a test dataset.
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
        RegistryLoader.load();
        infModel = initWktModel();
    }
    private static InfModel infModel;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void positiveTest() {

        ArrayList<Literal> expResult = new ArrayList<>();

        expResult.add(ResourceFactory.createTypedLiteral("0", XSDBaseNumericType.XSDinteger));
        expResult.add(ResourceFactory.createTypedLiteral("2", XSDBaseNumericType.XSDinteger));
        expResult.add(ResourceFactory.createTypedLiteral("2", XSDBaseNumericType.XSDinteger));
        expResult.add(ResourceFactory.createTypedLiteral("false", XSDBaseNumericType.XSDboolean));
        expResult.add(ResourceFactory.createTypedLiteral("true", XSDBaseNumericType.XSDboolean));

        String queryString = "SELECT ?dimension ?coordinateDimension ?spatialDimension ?isEmpty ?isSimple WHERE{"
                + " ex:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:dimension ?dimension ."
                + " ?aGeom geo:coordinateDimension ?coordinateDimension ."
                + " ?aGeom geo:spatialDimension ?spatialDimension ."
                + " ?aGeom geo:isEmpty ?isEmpty ."
                + " ?aGeom geo:isSimple ?isSimple ."
                + "}";
        ArrayList<Literal> result = literalQuery(queryString, infModel);

        System.out.println("Exp: " + expResult);
        System.out.println("Res: " + result);

        assertEquals(expResult, result);
    }

}
