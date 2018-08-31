/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension;

import conformance_test.TestQuerySupport;
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
    public void bufferTest_Projection_Linear() {
        System.out.println("Buffer Function Projection Linear");

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

    @Test
    public void bufferTest_Projection_NonLinear() {
        System.out.println("Buffer Function Projection Non-Linear");

        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((74.39204761973815 58.93046388030052, 73.7935792641947 54.61624912638217, 72.66504878236447 50.508902633562684, 71.04982384038158 46.76626767590642, 69.00997585331788 43.53217199537903, 66.62389473285293 40.930900496430695, 63.98327645310201 39.062419002875686, 61.18959919176996 37.9985325448215, 58.35022347670747 37.78012590389699, 55.57426621468039 38.41559237986803, 52.96840719494503 39.880511234514415, 50.63278924539918 42.11858620401472, 48.65716962714214 45.04380894638598, 47.117470596858766 48.54376436397433, 46.07286171038868 52.48395072482526, 45.56348600285128 56.71294852718711, 45.60891742008971 61.06823955103755, 46.20740876765922 65.3824523454532, 47.335959049582016 69.48979424312711, 48.951197585556656 73.23242266196758, 50.99105089070508 76.4665108602494, 53.37712824344635 79.06777506601065, 56.01773424394196 80.93625058047473, 58.811392582952976 82.00013326201588, 61.650745614490006 82.2185389213264, 64.42667988454923 81.58307440485805, 67.03251910372637 80.11816014815122, 69.36812345974613 77.88009171932936, 71.34373776020948 74.95487646199763, 72.88344055786729 71.45492832921445, 73.92806172382552 67.51474795676768, 74.4374563532765 63.28575392253697, 74.39204761973815 58.93046388030052))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String queryString = "SELECT ?buffer WHERE{ "
                + "geom:PointA geo:asWKT ?aWkt . "
                + "BIND( geof:buffer(?aWkt, 0.0002, uom:degree) AS ?buffer) . "
                + "}";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    //TODO improve the checking of buffer distance.
    @Test
    public void bufferTest_Geographic_Linear() {
        System.out.println("Buffer Function Geographic Linear");
        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((49.899820141481335 -7.499999814065095, 49.89982357682339 -7.50005416286721, 49.89983379760518 -7.500106428518575, 49.899850410726465 -7.5001546008335165, 49.8998727772313 -7.500196827054945, 49.899900036882244 -7.500231483111882, 49.89993114124498 -7.500257236082699, 49.89996489401149 -7.5002730954617265, 49.89999999701054 -7.500278451257391, 50.00000000229664 -7.500279028565501, 50.00003507526559 -7.500273671158249, 50.000068801491004 -7.500257805818185, 50.00009988601251 -7.500232041693716, 50.000127135299024 -7.500197368014575, 50.000149503076514 -7.500155116111152, 50.000166130501974 -7.500106908297218, 50.000176379140875 -7.50005459557864, 50.00017985548134 -7.500000186579973, 50.000179851963594 -7.399999813406841, 50.00017637555365 -7.3999454044350905, 50.00016612672241 -7.399893091825297, 50.00014949900459 -7.399844884274894, 50.000127130870865 -7.399802632850422, 50.000099881211085 -7.399767959909134, 50.00006879635382 -7.399742196803749, 50.00003506988892 -7.399726332762439, 49.999999996835406 -7.399720976906853, 49.90000000247568 -7.399721554226337, 49.89996489939464 -7.39972690847078, 49.89993114639072 -7.399742766550671, 49.899900041693975 -7.399768518501266, 49.899872781671114 -7.399803173818511, 49.89985041481116 -7.399845399558502, 49.8998338013984 -7.399893571606822, 49.899823580424936 -7.399945837145772, 49.89982014501383 -7.400000185916873, 49.899820141481335 -7.499999814065095))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((49.9 -7.5, 50.0 -7.5, 50.0 -7.4, 49.9 -7.4, 49.9 -7.5))";
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
    public void bufferTest_Geographic_NonLinear() {
        System.out.println("Buffer Function Geographic Non-Linear");
        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((49.8998 -7.5, 49.89980384294392 -7.500039018064403, 49.899815224093494 -7.500076536686473, 49.89983370607754 -7.500111114046604, 49.89985857864376 -7.500141421356237, 49.899888885953395 -7.500166293922461, 49.89992346331353 -7.500184775906503, 49.89996098193559 -7.500196157056081, 49.9 -7.5002, 50 -7.5002, 50.000039018064406 -7.500196157056081, 50.00007653668647 -7.500184775906503, 50.000111114046604 -7.500166293922461, 50.00014142135624 -7.500141421356237, 50.00016629392246 -7.500111114046604, 50.000184775906504 -7.500076536686473, 50.00019615705608 -7.500039018064403, 50.0002 -7.5, 50.0002 -7.4, 50.00019615705608 -7.399960981935597, 50.000184775906504 -7.399923463313527, 50.00016629392246 -7.399888885953397, 50.00014142135624 -7.399858578643763, 50.000111114046604 -7.39983370607754, 50.00007653668647 -7.399815224093498, 50.000039018064406 -7.3998038429439195, 50 -7.3998, 49.9 -7.3998, 49.89996098193559 -7.3998038429439195, 49.89992346331353 -7.399815224093498, 49.899888885953395 -7.39983370607754, 49.89985857864376 -7.399858578643763, 49.89983370607754 -7.399888885953397, 49.899815224093494 -7.399923463313527, 49.89980384294392 -7.399960981935597, 49.8998 -7.4, 49.8998 -7.5))^^http://www.opengis.net/ont/geosparql#wktLiteral";

        String lexicalForm = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((49.9 -7.5, 50.0 -7.5, 50.0 -7.4, 49.9 -7.4, 49.9 -7.5))";
        InfModel model = ModelFactory.createRDFSModel(ModelFactory.createDefaultModel());
        model.add(ResourceFactory.createResource("http://example.org/Geometry#PointA"), Geo.AS_WKT_PROP, lexicalForm, WKTDatatype.INSTANCE);
        String queryString = "SELECT ?buffer WHERE{ "
                + "geom:PointA geo:asWKT ?aWkt . "
                + "BIND( geof:buffer(?aWkt, 0.0002, uom:degree) AS ?buffer) . "
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
