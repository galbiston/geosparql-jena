/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.registry;

import implementation.vocabulary.SRS_URI;
import java.lang.reflect.Type;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import org.apache.sis.measure.Quantities;
import org.apache.sis.measure.Units;
import org.apache.sis.referencing.CRS;
import org.apache.sis.referencing.CommonCRS;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class CRSRegistryTest {

    public CRSRegistryTest() {
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
     * Test of getCRS method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testGetCRS() throws FactoryException {
        System.out.println("getCRS");
        String srsURI = "http://www.opengis.net/def/crs/EPSG/0/4326";
        CoordinateReferenceSystem expResult = CRS.forCode(srsURI);
        CoordinateReferenceSystem result = CRSRegistry.getCRS(srsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCRS method, of class CRSRegistry.
     */
    @Test
    public void testGetDefaultWKTCRS() {
        try {
            System.out.println("getDefaultWKTCRS");
            String srsURI = SRS_URI.DEFAULT_WKT_CRS84;

            String default_CRS_WKT = "GEOGCS[\"CRS 84\", \n"
                    + "  DATUM[\"WGS_1984\", \n"
                    + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
                    + "    AUTHORITY[\"EPSG\",\"6326\"]], \n"
                    + "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                    + "  UNIT[\"degree\", 0.017453292519943295], \n"
                    + "  AXIS[\"Geodetic longitude\", EAST], \n"
                    + "  AXIS[\"Geodetic latitude\", NORTH], \n"
                    + "  AUTHORITY[\"OGC\", 4326]]";

            CoordinateReferenceSystem expResult = CRS.fromWKT(default_CRS_WKT);
            CoordinateReferenceSystem result = CRSRegistry.getCRS(srsURI);

            //System.out.println("Exp: " + expResult);
            //System.out.println("Res: " + result);
            assertEquals(expResult, result);
        } catch (FactoryException ex) {
            System.out.println("Default WKT CRS error: " + ex.getMessage());
        }
    }

    @Test
    public void testLookupWKTCRS() {
        try {
            System.out.println("lookupWKTCRS");
            String srsURI = SRS_URI.DEFAULT_WKT_CRS84;

            String default_CRS_WKT = "GEOGCS[\"CRS 84\", \n"
                    + "  DATUM[\"WGS_1984\", \n"
                    + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
                    + "    AUTHORITY[\"EPSG\",\"6326\"]], \n"
                    + "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                    + "  UNIT[\"degree\", 0.017453292519943295], \n"
                    + "  AXIS[\"Geodetic longitude\", EAST], \n"
                    + "  AXIS[\"Geodetic latitude\", NORTH], \n"
                    + "  AUTHORITY[\"OGC\", 4326]]";

            CoordinateReferenceSystem expResult = CRS.fromWKT(default_CRS_WKT);
            CoordinateReferenceSystem result = CRSRegistry.getCRS(srsURI);

            //System.out.println("Exp: " + expResult);
            //System.out.println("Res: " + result);
            assertEquals(expResult, result);
        } catch (FactoryException ex) {
            System.out.println("Default WKT CRS error: " + ex.getMessage());
        }
    }

    @Ignore
    @Test
    @SuppressWarnings("unchecked")
    public void testCRS() {
        System.out.println("extractCRSDistanceUnits");

        try {
            System.out.println("------------EPSG:4326 None");
            CoordinateReferenceSystem wgs84CRS = CRS.forCode("http://www.opengis.net/def/crs/EPSG/0/4326");
            System.out.println(wgs84CRS);

            System.out.println("------------EPSG:27700 None - ");
            CoordinateReferenceSystem osgbCRS = CRS.forCode("http://www.opengis.net/def/crs/EPSG/0/27700");
            System.out.println(osgbCRS);

            System.out.println("------------ Units - ");

            Unit osgbUnit = CRS.getHorizontalComponent(osgbCRS).getCoordinateSystem().getAxis(0).getUnit();
            System.out.println(osgbUnit);
            Unit wgs84Unit = CRS.getHorizontalComponent(wgs84CRS).getCoordinateSystem().getAxis(0).getUnit();
            System.out.println(wgs84Unit);

            System.out.println(osgbUnit);

            double distOSGB = 100;
            Quantity<Length> dist = Quantities.create(distOSGB, osgbUnit);

            Class class1 = wgs84Unit.getClass();
            String name = class1.getName();
            String typeName = class1.getTypeName();
            Class[] interfaces = class1.getInterfaces();
            Type[] types = class1.getGenericInterfaces();
            //UnitConverter converter = wgs84Unit.getConverterTo(osgbUnit);
            //UnitConverter converter2 = wgs84Unit.getConverterTo(SI.RADIAN);
            //UnitConverter converter3 = wgs84Unit.getConverterTo(SI.METER);
            Quantity<Angle> angleDist = Quantities.create(distOSGB, wgs84Unit);
            Quantity< Angle> radDist = angleDist.to(Units.RADIAN);

            System.out.println("Proj WGS: " + wgs84CRS.getRemarks() + " Proj OSGB: " + osgbCRS.getRemarks());
            System.out.println("WGS: " + wgs84Unit.getSystemUnit() + " OSGB: " + osgbUnit.getSystemUnit());
            System.out.println("Before: " + distOSGB + " After: " + radDist);

        } catch (FactoryException ex) {
            System.out.println("CRS Not Decoded");
        }

    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testCheckAxisXY_WGS84() throws FactoryException {
        System.out.println("checkAxisXY");
        CoordinateReferenceSystem crs = CRS.forCode(SRS_URI.WGS84_CRS);
        Boolean expResult = false;
        Boolean result = CRSRegistry.checkAxisXY(crs);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testCheckAxisXY_Geocentric() throws FactoryException {
        System.out.println("checkAxisXY_Geocentric");
        CoordinateReferenceSystem crs = CommonCRS.WGS84.geocentric();
        Boolean expResult = false;
        Boolean result = CRSRegistry.checkAxisXY(crs);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testCheckAxisXY_CRS84() throws FactoryException {
        System.out.println("checkAxisXY_Geocentric");
        CoordinateReferenceSystem crs = CRS.forCode("CRS:84");
        Boolean expResult = true;
        Boolean result = CRSRegistry.checkAxisXY(crs);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testCheckAxisXY_OSGB() throws FactoryException {
        System.out.println("checkAxisXY_Geocentric");
        CoordinateReferenceSystem crs = CRS.forCode(SRS_URI.OSGB36_CRS);
        Boolean expResult = true;
        Boolean result = CRSRegistry.checkAxisXY(crs);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
