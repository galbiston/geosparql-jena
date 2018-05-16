/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import implementation.datatype.GMLDatatype;
import implementation.datatype.GeoDatatypeEnum;
import implementation.datatype.WKTDatatype;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomGeometryFactory;
import implementation.vocabulary.SRS_URI;
import implementation.vocabulary.Unit_URI;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 *
 */
public class GeometryWrapperTest {

    GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    public GeometryWrapperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        GeoSPARQLSupport.loadFunctionsNoIndex();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of checkTransformCRS method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCheckCRS() throws Exception {
        System.out.println("checkCRS");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper sourceCRSGeometry = new GeometryWrapper(geometry, sourceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        //Only the SRS_URI is important in the instance.
        String targetSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper instance = new GeometryWrapper(geometry, targetSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        //Expecting the coordinates to be reveresed.
        Geometry geometryTarget = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        GeometryWrapper expResult = new GeometryWrapper(geometryTarget, targetSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());
        GeometryWrapper result = instance.checkTransformCRS(sourceCRSGeometry);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCRS method, of class GeometryWrapper.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testGetCRS() throws FactoryException {
        System.out.println("getCRS");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        CoordinateReferenceSystem expResult = CRS.decode(sourceSRSURI);
        CoordinateReferenceSystem result = instance.getCRS();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getXYGeometry method, of class GeometryWrapper.
     */
    @Test
    public void testGetXYGeometry() {
        System.out.println("getXYGeometry");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        //Expect the coordinates to be reversed as JTS is x,y and WGS84 is y,x
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        Geometry result = instance.getXYGeometry();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getParsingGeometry method, of class GeometryWrapper.
     */
    @Test
    public void testGetParsingGeometry() {
        System.out.println("getParsingGeometry");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        //Expect coordinates to be same as supplied.
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        Geometry result = instance.getParsingGeometry();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSrsURI method, of class GeometryWrapper.
     */
    @Test
    public void testGetSrsURI() {
        System.out.println("getSrsURI");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        String expResult = SRS_URI.WGS84_CRS;
        String result = instance.getSrsURI();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSRID method, of class GeometryWrapper.
     */
    @Test
    public void testGetSRID() {
        System.out.println("getSRID");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        String expResult = SRS_URI.WGS84_CRS;
        String result = instance.getSRID();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeoDatatypeEnum method, of class GeometryWrapper.
     */
    @Test
    public void testGetDatatypeEnum() {
        System.out.println("getDatatypeEnum");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        GeoDatatypeEnum expResult = GeoDatatypeEnum.WKT;
        GeoDatatypeEnum result = instance.getGeoDatatypeEnum();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of distance same SRS_URI method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceSameCRSSameUnit() throws Exception {
        System.out.println("distance, same CRS, same Unit");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        String targetSRSURI = SRS_URI.OSGB_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(12.0, 1.0));
        String instanceSRSURI = SRS_URI.OSGB_CRS;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        //CRS is in metres.
        String distanceUnitsURI = Unit_URI.METRE_URI;

        double expResult = 10.0;
        double result = instance.distance(targetGeometry, distanceUnitsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of distance same SRS_URI method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceSameCRSDifferentUnit() throws Exception {
        System.out.println("distance, same CRS, different Unit");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(385458, 156785)); //LatLon - 51.31, -2.21
        String targetSRSURI = SRS_URI.OSGB_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(487920, 157518)); //LatLon: 51.31, -0.74
        String instanceSRSURI = SRS_URI.OSGB_CRS;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        //CRS is in metres.
        String distanceUnitsURI = Unit_URI.RADIAN_URI;

        double expResult = 0.025656; //Degree: 1.47
        double result = instance.distance(targetGeometry, distanceUnitsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.000001);
    }

    /**
     * Test of distance different SRS_URI method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceDifferentCRSSameUnit() throws Exception {
        System.out.println("distance, different CRS, same Unit");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        String targetSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 12.0));
        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        //CRS is in degrees.
        String distanceUnitsURI = Unit_URI.DEGREE_ANGLE_URI;

        double expResult = 10.0;
        double result = instance.distance(targetGeometry, distanceUnitsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of distance different SRS_URI method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceDifferentCRSDifferentUnit() throws Exception {
        System.out.println("distance, different CRS, different Unit");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(0.0, 1.0));
        String targetSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 0.0));
        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        //CRS is in degrees.
        String distanceUnitsURI = Unit_URI.METRE_URI;

        double expResult = 111318; //1.0 degree of longigtude at the equator is approx 111.32km.
        double result = instance.distance(targetGeometry, distanceUnitsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of empty WKT GeometryWrapper.
     *
     */
    @Test
    public void testEmptyWKT() {
        System.out.println("emptyWKT");
        CustomCoordinateSequence sequence = new CustomCoordinateSequence(DimensionInfo.xyPoint().getDimensions());
        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(sequence);
        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper result = new GeometryWrapper(instanceGeo, instanceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        GeometryWrapper expResult = GeometryWrapper.EMPTY_WKT;

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of empty WKT GeometryWrapper.
     *
     */
    @Test
    public void testEmptyGeometryWrapper() {
        System.out.println("emptyGeometryWrapper");

        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper result = new GeometryWrapper(instanceSRSURI, GeoDatatypeEnum.WKT, DimensionInfo.xyPoint());

        GeometryWrapper expResult = GeometryWrapper.EMPTY_WKT;

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of asLiteral.
     *
     */
    @Test
    public void testAsLiteral() {
        System.out.println("asLiteral");

        String lexicalForm = "POINT(-83.38 33.95)";
        GeometryWrapper instance = WKTDatatype.INSTANCE.parse(lexicalForm);

        Literal result = instance.asLiteral();
        Literal expResult = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> " + lexicalForm, WKTDatatype.INSTANCE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of asLiteral conversion enum.
     *
     */
    @Test
    public void testAsLiteralConversionEnum() {
        System.out.println("asLiteralConversionEnum");

        String lexicalForm = "POINT(-83.38 33.95)";
        GeometryWrapper instance = WKTDatatype.INSTANCE.parse(lexicalForm);

        Literal result = instance.asLiteral(GeoDatatypeEnum.GML);
        String gmlGeometryLiteral = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";
        Literal expResult = ResourceFactory.createTypedLiteral(gmlGeometryLiteral, GMLDatatype.INSTANCE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of asLiteral conversion datatype.
     *
     */
    @Test
    public void testAsLiteralConversionDatatype() {
        System.out.println("asLiteralConversionDatatype");

        String lexicalForm = "POINT(-83.38 33.95)";
        GeometryWrapper instance = WKTDatatype.INSTANCE.parse(lexicalForm);

        Literal result = instance.asLiteral(GMLDatatype.INSTANCE);
        String gmlGeometryLiteral = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";
        Literal expResult = ResourceFactory.createTypedLiteral(gmlGeometryLiteral, GMLDatatype.INSTANCE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
