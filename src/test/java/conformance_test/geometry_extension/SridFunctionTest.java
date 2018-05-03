/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import conformance_test.TestQuerySupport;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
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

    private static final InfModel SAMPLE_DATA_MODEL = TestQuerySupport.getSampleData_WKT();

    @BeforeClass
    public static void setUpClass() {
    }

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
    public void sridFunctionTest() {
        System.out.println("SRID Function");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://www.opengis.net/def/crs/EPSG/0/27700");
        expResult.add("http://www.opengis.net/def/crs/OGC/1.3/CRS84");

        String queryString = "SELECT DISTINCT ?srid WHERE{"
                + "?geometry geo:asWKT ?aWKT ."
                + "BIND(geof:getSRID(?aWKT) AS ?srid)"
                + "}ORDER BY ?srid";

        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
