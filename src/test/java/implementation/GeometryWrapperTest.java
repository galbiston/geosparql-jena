/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import implementation.jts.CustomCoordinateSequence;
import implementation.support.GeoSerialisationEnum;
import implementation.vocabulary.UnitsOfMeasureLookUp;
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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of checkCRS method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCheckCRS() throws Exception {
        System.out.println("checkCRS");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = CRSRegistry.WGS84_CRS;
        GeometryWrapper sourceCRSGeometry = new GeometryWrapper(geometry, sourceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        //Only the CRS is important in the instance.
        String targetSRSURI = CRSRegistry.DEFAULT_WKT_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, targetSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        //Expecting the coordinates to be reveresed.
        Geometry geometryTarget = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        GeometryWrapper expResult = new GeometryWrapper(geometryTarget, targetSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());
        GeometryWrapper result = instance.checkCRS(sourceCRSGeometry);

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
        String sourceSRSURI = CRSRegistry.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        CoordinateReferenceSystem expResult = CRS.decode(sourceSRSURI);
        CoordinateReferenceSystem result = instance.getCRS();
        assertEquals(expResult, result);
    }

    /**
     * Test of getXYGeometry method, of class GeometryWrapper.
     */
    @Test
    public void testGetXYGeometry() {
        System.out.println("getXYGeometry");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = CRSRegistry.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        //Expect the coordinates to be reversed as JTS is x,y and WGS84 is y,x
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        Geometry result = instance.getXYGeometry();
        assertEquals(expResult, result);
    }

    /**
     * Test of getParsingGeometry method, of class GeometryWrapper.
     */
    @Test
    public void testGetParsingGeometry() {
        System.out.println("getParsingGeometry");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = CRSRegistry.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        //Expect coordinates to be same as supplied.
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        Geometry result = instance.getParsingGeometry();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSrsURI method, of class GeometryWrapper.
     */
    @Test
    public void testGetSrsURI() {
        System.out.println("getSrsURI");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = CRSRegistry.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        String expResult = CRSRegistry.WGS84_CRS;
        String result = instance.getSrsURI();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSRID method, of class GeometryWrapper.
     */
    @Test
    public void testGetSRID() {
        System.out.println("getSRID");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = CRSRegistry.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        String expResult = CRSRegistry.WGS84_CRS;
        String result = instance.getSRID();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeoSerialisation method, of class GeometryWrapper.
     */
    @Test
    public void testGetSerialisation() {
        System.out.println("getSerialisation");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = CRSRegistry.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        GeoSerialisationEnum expResult = GeoSerialisationEnum.WKT;
        GeoSerialisationEnum result = instance.getGeoSerialisation();
        assertEquals(expResult, result);
    }

    /**
     * Test of distance same CRS method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceSameCRS() throws Exception {
        System.out.println("distance");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        String targetSRSURI = CRSRegistry.OSGB_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(12.0, 1.0));
        String instanceSRSURI = CRSRegistry.OSGB_CRS;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        //CRS is in metres.
        String distanceUnitsURI = UnitsOfMeasureLookUp.METRE_URI;

        double expResult = 10.0;
        double result = instance.distance(targetGeometry, distanceUnitsURI);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of distance different CRS method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceDifferentCRS() throws Exception {
        System.out.println("distance");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        String targetSRSURI = CRSRegistry.WGS84_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 12.0));
        String instanceSRSURI = CRSRegistry.DEFAULT_WKT_CRS;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        //CRS is in degrees.
        String distanceUnitsURI = UnitsOfMeasureLookUp.DEGREE_URI;

        double expResult = 10.0;
        double result = instance.distance(targetGeometry, distanceUnitsURI);
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of empty WKT GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testEmptyWKT() throws Exception {
        System.out.println("emptyWKT");
        CustomCoordinateSequence sequence = new CustomCoordinateSequence(DimensionInfo.xyPoint().getDimensions());
        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(sequence);
        String instanceSRSURI = CRSRegistry.DEFAULT_WKT_CRS;
        GeometryWrapper result = new GeometryWrapper(instanceGeo, instanceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        GeometryWrapper expResult = GeometryWrapper.emptyWKT();
        assertEquals(expResult, result);
    }

    /**
     * Test of empty WKT GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testEmptyGeometryWrapper() throws Exception {
        System.out.println("emptyGeometryWrapper");

        String instanceSRSURI = CRSRegistry.DEFAULT_WKT_CRS;
        GeometryWrapper result = new GeometryWrapper(instanceSRSURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());

        GeometryWrapper expResult = GeometryWrapper.emptyWKT();
        assertEquals(expResult, result);
    }

}
