/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import conformance_test.TestQuerySupport;
import implementation.GeoSPARQLSupport;
import implementation.datatype.WKTDatatype;
import implementation.vocabulary.Geo;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
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
 * A.3.1.4 /conf/geometry-extension/query-functions
 *
 * Requirement: /req/geometry-extension/query-functions Implementations shall
 * support geof:distance, geof:buffer, geof:convexHull, geof:intersection,
 * geof:union, geof:difference, geof:symDifference, geof:envelope and
 * geof:boundary as SPARQL extension functions, consistent with the definitions
 * of the corresponding functions (distance, buffer, convexHull, intersection,
 * difference, symDifference, envelope and boundary respectively) in Simple
 * Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each of the
 * following functions returns the correct result for a test dataset when using
 * the specified serialization and version: geof:distance, geof:buffer,
 * geof:convexHull, geof:intersection, geof:union, geof:difference,
 * geof:symDifference, geof:envelope and geof:boundary.
 *
 * c.) Reference: Clause 8.7 Req 19
 *
 * d.) Test Type: Capabilities
 */
public class QueryFunctionsTest {

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
    public void boundaryTest() {
        System.out.println("Boundary Function");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING (30 40, 30 70, 90 70, 90 40, 30 40)^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?boundary WHERE{ "
                + "geom:PolygonH geo:asWKT ?hWkt . "
                + "BIND(geof:boundary(?hWkt) AS ?boundary) . "
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void bufferTest_Projection() {
        System.out.println("Buffer Function Projection");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON ((80 60, 79.61570560806462 56.09819355967743, 78.47759065022574 52.3463313526982, 76.62939224605091 48.88859533960796, 74.14213562373095 45.85786437626905, 71.11140466039204 43.370607753949095, 67.6536686473018 41.522409349774264, 63.90180644032257 40.38429439193539, 60 40, 56.09819355967743 40.38429439193539, 52.34633135269821 41.522409349774264, 48.88859533960796 43.370607753949095, 45.85786437626905 45.85786437626905, 43.370607753949095 48.88859533960796, 41.522409349774264 52.346331352698215, 40.384294391935384 56.09819355967745, 40 60.000000000000014, 40.3842943919354 63.90180644032259, 41.52240934977428 67.65366864730181, 43.37060775394911 71.11140466039207, 45.85786437626908 74.14213562373098, 48.88859533960799 76.62939224605093, 52.34633135269824 78.47759065022575, 56.098193559677476 79.61570560806462, 60.00000000000005 80, 63.901806440322616 79.6157056080646, 67.65366864730186 78.47759065022572, 71.1114046603921 76.62939224605087, 74.142135623731 74.14213562373091, 76.62939224605094 71.11140466039198, 78.47759065022576 67.65366864730173, 79.61570560806462 63.90180644032249, 80 60))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?buffer WHERE{ "
                + "geom:PointA geo:asWKT ?aWkt . "
                + "BIND( geof:buffer(?aWkt, 20, uom:metre) AS ?buffer) . "
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    //TODO improve the checking of buffer distance.
    @Test
    public void bufferTest_Geographic() {
        System.out.println("Buffer Function Geographic");
        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON ((49.76699653893713 -7.555722360076407, 49.76696152213475 -7.5557287374109965, 49.766927984025536 -7.555745539189558, 49.766897213455735 -7.555772119710877, 49.76687039291471 -7.555807457488758, 49.766848553093574 -7.5558501945089365, 49.76683253327731 -7.555898688417312, 49.766822949092656 -7.555951075633834, 49.76682016885081 -7.556005342966762, 49.76682429939393 -7.556059404975513, 49.76683518198949 -7.556111184109526, 49.766852398429904 -7.556158690544004, 49.76687528710336 -7.556200098644986, 49.76690296841828 -7.556233817125648, 49.7669343786043 -7.556258550198071, 49.76696831059141 -7.5562733473705, 49.76700346039605 -7.556277639976286, 49.767038477231885 -7.556271263030407, 49.767072015419636 -7.5562544615732, 49.767102786101084 -7.556227881256956, 49.76712960676988 -7.5561925435366515, 49.76715144671545 -7.556149806417781, 49.76716746663387 -7.556101312269474, 49.76717705088286 -7.55604892470831, 49.76717983114141 -7.555994656978492, 49.76717570056483 -7.55594059458103, 49.76716481789074 -7.555888815125665, 49.76714760133867 -7.55584130848611, 49.76712471253745 -7.5557999003275516, 49.7670970310981 -7.55576618194558, 49.76706562080992 -7.555741449113088, 49.76703168875847 -7.555726652285303, 49.76699653893713 -7.555722360076407))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON ((49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556, 49.767 -7.556))";
        GeoSPARQLSupport.loadFunctionsNoIndex();
        InfModel model = ModelFactory.createRDFSModel(ModelFactory.createDefaultModel());
        model.add(ResourceFactory.createResource("http://example.org/Geometry#PointA"), Geo.AS_WKT_PROP, lexicalForm, WKTDatatype.THE_WKT_DATATYPE);
        String queryString = "SELECT ?buffer WHERE{ "
                + "geom:PointA geo:asWKT ?aWkt . "
                + "BIND( geof:buffer(?aWkt, 20, uom:metre) AS ?buffer) . "
                + "}";

        String result = TestQuerySupport.querySingle(queryString, model);
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void convexHullTest() {
        System.out.println("Convex Hull Function");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON ((145 30, 145 40, 160 40, 160 30, 145 30))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?convexHull WHERE{ "
                + "geom:PolygonL geo:asWKT ?lWkt . "
                + "BIND( geof:convexHull(?lWkt) AS ?convexHull) . "
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void differenceTest() {
        System.out.println("Difference Function");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON ((140 15, 140 45, 200 45, 200 15, 140 15), (145 30, 160 30, 160 40, 145 40, 145 30))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?difference WHERE{ "
                + "geom:PolygonJ geo:asWKT ?jWkt . "
                + "geom:PolygonL geo:asWKT ?lWkt . "
                + "BIND( geof:difference(?jWkt, ?lWkt) AS ?difference) . "
                + "}";

        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void distanceMetresTest() {
        System.out.println("Distance Metres Function");

        String expResult = "30.0e0^^http://www.w3.org/2001/XMLSchema#double";

        String queryString = "SELECT ?distance WHERE{ "
                + "geom:PointA geo:asWKT ?aWkt . "
                + "geom:PointB geo:asWKT ?bWkt . "
                + "BIND( geof:distance(?aWkt, ?bWkt, uom:metre) AS ?distance) . "
                + "}";

        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void distanceRadiansTest() {
        System.out.println("Distance Radians Function");

        Double expResult = 7.2449E-6;

        String queryString = "SELECT ?distance WHERE{ "
                + "geom:PointA geo:asWKT ?aWkt . "
                + "geom:PointB geo:asWKT ?bWkt . "
                + "BIND( geof:distance(?aWkt, ?bWkt, uom:radian) AS ?distance) . "
                + "}";

        String resultString = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);
        Double result = Double.parseDouble(resultString.substring(0, resultString.indexOf("^")));

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0001);
    }

    @Test
    public void envelopeTest() {
        System.out.println("Envelope Function");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON ((30 40, 30 70, 90 70, 90 40, 30 40))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?envelope WHERE{ "
                + "geom:PolygonH geo:asWKT ?hWkt . "
                + "BIND( geof:envelope(?hWkt) AS ?envelope) . "
                + "}";

        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void intersectionTest() {
        System.out.println("Intersection Function");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING (80 70, 80 40)^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?intersection WHERE{ "
                + "geom:LineStringG geo:asWKT ?gWkt . "
                + "geom:PolygonH geo:asWKT ?hWkt . "
                + "BIND( geof:intersection(?gWkt, ?hWkt) AS ?intersection) . "
                + "}";

        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void symmetricDifferenceTest() {
        System.out.println("Symmetric Difference Function");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> MULTIPOLYGON (((30 40, 30 70, 90 70, 90 45, 80 45, 80 40, 30 40)), ((80 40, 90 40, 90 45, 140 45, 140 15, 80 15, 80 40)))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?symDifference WHERE{ "
                + "geom:PolygonH geo:asWKT ?hWkt . "
                + "geom:PolygonI geo:asWKT ?IWkt . "
                + "BIND( geof:symDifference(?hWkt, ?IWkt) AS ?symDifference) . "
                + "}";

        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void unionTest() {
        System.out.println("Union Function");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON ((30 40, 30 70, 90 70, 90 45, 140 45, 140 15, 80 15, 80 40, 30 40))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?union WHERE{ "
                + "geom:PolygonH geo:asWKT ?hWkt . "
                + "geom:PolygonI geo:asWKT ?IWkt . "
                + "BIND( geof:union(?hWkt, ?IWkt) AS ?union) . "
                + "}";

        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
