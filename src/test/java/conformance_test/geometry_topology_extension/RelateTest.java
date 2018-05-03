/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension;

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
 * A.4.1.1 /conf/geometry-topology-extension/relate-query-function
 *
 * Requirement: /req/geometry-topology-extension/relate-query-function
 * Implementations shall support geof:relate as a SPARQL extension function,
 * consistent with the relate operator defined in Simple Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving the
 * geof:relate function returns the correct result for a test dataset when using
 * the specified serialization and version.
 *
 * c.) Reference: Clause 9.2 Req 21
 *
 * d.) Test Type: Capabilities
 */
public class RelateTest {

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

    private static final String RELATE_QUERY_STRING = "SELECT ?geom\n"
            + "WHERE{\n"
            + "    ?geom geo:asWKT ?geomLit .\n"
            + "    FILTER(geof:relate(?geomLit, #target#, #relation#)) .\n"
            + "}ORDER by ?geom";

    private static final String RELATION_REPLACEMENT = "#relation#";
    private static final String TARGET_REPLACEMENT = "#target#";

    public static final List<String> runRelateQuery(String target, String relation) {
        String queryString = RELATE_QUERY_STRING.replace(RELATION_REPLACEMENT, relation).replace(TARGET_REPLACEMENT, target);
        //System.out.println(queryString);
        return TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);
    }

    @Test
    public void pointTest() {

        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PointA");

        List<String> result = runRelateQuery("\"<http://www.opengis.net/def/crs/EPSG/0/27700> Point(60 60)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>", "\"T*F**FFF*\"");
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void lineTest() {

        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#LineStringD");

        List<String> result = runRelateQuery("\"<http://www.opengis.net/def/crs/EPSG/0/27700> LineString(40 50, 80 50)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>", "\"TFFFTFFFT\"");
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void polygonTest() {

        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonH");

        List<String> result = runRelateQuery("\"<http://www.opengis.net/def/crs/EPSG/0/27700> Polygon((30 40, 30 70, 90 70, 90 40, 30 40))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>", "\"TFFFTFFFT\"");
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
