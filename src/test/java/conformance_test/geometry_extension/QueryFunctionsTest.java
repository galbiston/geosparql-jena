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

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING(30 40, 30 70, 90 70, 90 40, 30 40)^^http://www.opengis.net/ont/geosparql#wktLiteral";

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

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((80 60, 79.61570560806462 56.09819355967743, 78.47759065022574 52.3463313526982, 76.62939224605091 48.88859533960796, 74.14213562373095 45.85786437626905, 71.11140466039204 43.370607753949095, 67.6536686473018 41.522409349774264, 63.90180644032257 40.38429439193539, 60 40, 56.09819355967743 40.38429439193539, 52.34633135269821 41.522409349774264, 48.88859533960796 43.370607753949095, 45.85786437626905 45.85786437626905, 43.370607753949095 48.88859533960796, 41.522409349774264 52.346331352698215, 40.384294391935384 56.09819355967745, 40 60.000000000000014, 40.3842943919354 63.90180644032259, 41.52240934977428 67.65366864730181, 43.37060775394911 71.11140466039207, 45.85786437626908 74.14213562373098, 48.88859533960799 76.62939224605093, 52.34633135269824 78.47759065022575, 56.098193559677476 79.61570560806462, 60.00000000000005 80, 63.901806440322616 79.6157056080646, 67.65366864730186 78.47759065022572, 71.1114046603921 76.62939224605087, 74.142135623731 74.14213562373091, 76.62939224605094 71.11140466039198, 78.47759065022576 67.65366864730173, 79.61570560806462 63.90180644032249, 80 60))^^http://www.opengis.net/ont/geosparql#wktLiteral";

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
        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((49.8998201414908 -7.499999814064664, 49.89982357683287 -7.50005416286678, 49.89983379761466 -7.500106428518145, 49.899850410735944 -7.500154600833086, 49.89987277724078 -7.500196827054515, 49.89990003689172 -7.5002314831114525, 49.89993114125447 -7.500257236082268, 49.899964894020975 -7.500273095461297, 49.89999999702002 -7.5002784512569605, 50.00000000230592 -7.500279028565077, 50.00003507527487 -7.500273671157825, 50.00006880150028 -7.5002578058177605, 50.0000998860218 -7.500232041693291, 50.0001271353083 -7.500197368014151, 50.000149503085794 -7.500155116110727, 50.00016613051125 -7.500106908296793, 50.000176379150155 -7.500054595578216, 50.00017985549062 -7.500000186579547, 50.000179851972355 -7.399999813406196, 50.000176375562425 -7.399945404434442, 50.000166126731166 -7.399893091824653, 50.00014949901335 -7.399844884274249, 50.000127130879626 -7.3998026328497755, 50.00009988121984 -7.399767959908488, 50.00006879636259 -7.3997421968031025, 50.00003506989767 -7.3997263327617935, 49.99999999684417 -7.399720976906207, 49.90000000248464 -7.399721554225687, 49.8999648994036 -7.399726908470129, 49.89993114639969 -7.399742766550021, 49.89990004170292 -7.399768518500614, 49.89987278168009 -7.39980317381786, 49.89985041482011 -7.39984539955785, 49.899833801407375 -7.39989357160617, 49.899823580433896 -7.399945837145121, 49.89982014502279 -7.400000185916223, 49.8998201414908 -7.499999814064664))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((49.9 -7.5, 50.0 -7.5, 50.0 -7.4, 49.9 -7.4, 49.9 -7.5))";
        GeoSPARQLSupport.loadFunctionsNoIndex();
        InfModel model = ModelFactory.createRDFSModel(ModelFactory.createDefaultModel());
        model.add(ResourceFactory.createResource("http://example.org/Geometry#PointA"), Geo.AS_WKT_PROP, lexicalForm, WKTDatatype.INSTANCE);
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

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((145 30, 145 40, 160 40, 160 30, 145 30))^^http://www.opengis.net/ont/geosparql#wktLiteral";

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

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((140 15, 140 45, 200 45, 200 15, 140 15), (145 30, 160 30, 160 40, 145 40, 145 30))^^http://www.opengis.net/ont/geosparql#wktLiteral";

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

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((30 40, 30 70, 90 70, 90 40, 30 40))^^http://www.opengis.net/ont/geosparql#wktLiteral";

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

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING(80 70, 80 40)^^http://www.opengis.net/ont/geosparql#wktLiteral";

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

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> MULTIPOLYGON(((30 40, 30 70, 90 70, 90 45, 80 45, 80 40, 30 40)), ((80 40, 90 40, 90 45, 140 45, 140 15, 80 15, 80 40)))^^http://www.opengis.net/ont/geosparql#wktLiteral";

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

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((30 40, 30 70, 90 70, 90 45, 140 45, 140 15, 80 15, 80 40, 30 40))^^http://www.opengis.net/ont/geosparql#wktLiteral";

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
