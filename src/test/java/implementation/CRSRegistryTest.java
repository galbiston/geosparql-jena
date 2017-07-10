/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import java.lang.reflect.Type;
import javax.measure.Measure;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
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
 * @author Gregory Albiston
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
     * Test of addCRS method, of class CRSRegistry.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testAddCRS() throws FactoryException {
        System.out.println("addCRS");
        String srsURI = "http://www.opengis.net/def/crs/EPSG/0/4326";
        CoordinateReferenceSystem expResult = CRS.decode(srsURI);
        CoordinateReferenceSystem result = CRSRegistry.addCRS(srsURI);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCRS method, of class CRSRegistry.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testGetCRS() throws FactoryException {
        System.out.println("getCRS");
        String srsURI = "http://www.opengis.net/def/crs/EPSG/0/4326";
        CoordinateReferenceSystem expResult = CRS.decode(srsURI);

        CoordinateReferenceSystem result = CRSRegistry.getCRS(srsURI);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCRS method, of class CRSRegistry.
     */
    @Test
    public void testGetDefaultWKTCRS() {
        try {
            System.out.println("getDefaultWKTCRS");
            String srsURI = CRSRegistry.DEFAULT_WKT_CRS;

            String default_CRS_WKT = "GEOGCS[\"CRS 84\", \n"
                    + "  DATUM[\"WGS_1984\", \n"
                    + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
                    + "    AUTHORITY[\"EPSG\",\"6326\"]], \n"
                    + "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                    + "  UNIT[\"degree\", 0.017453292519943295], \n"
                    + "  AXIS[\"Geodetic longitude\", EAST], \n"
                    + "  AXIS[\"Geodetic latitude\", NORTH], \n"
                    + "  AUTHORITY[\"OGC\", 4326]]";

            CoordinateReferenceSystem expResult = CRS.parseWKT(default_CRS_WKT);
            CoordinateReferenceSystem result = CRSRegistry.getCRS(srsURI);

            assertEquals(expResult, result);
        } catch (FactoryException ex) {
            System.out.println("Default WKT CRS error: " + ex.getMessage());
        }
    }

    @Test
    public void testCRS() {
        System.out.println("extractCRSDistanceUnits");

        try {
            System.out.println("------------EPSG:4326 None");
            CoordinateReferenceSystem wgs84CRS = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");
            System.out.println(wgs84CRS);

            System.out.println("------------EPSG:27700 None - ");
            CoordinateReferenceSystem osgbCRS = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/27700");
            System.out.println(osgbCRS);

            System.out.println("------------ Units - ");

            Unit osgbUnit = CRS.getHorizontalCRS(osgbCRS).getCoordinateSystem().getAxis(0).getUnit();
            System.out.println(osgbUnit);
            Unit wgs84Unit = CRS.getHorizontalCRS(wgs84CRS).getCoordinateSystem().getAxis(0).getUnit();
            System.out.println(wgs84Unit);

            System.out.println(osgbUnit);

            double distOSGB = 100;
            Measure<Double, Length> dist = Measure.valueOf(distOSGB, osgbUnit);

            Class class1 = wgs84Unit.getClass();
            String name = class1.getName();
            String typeName = class1.getTypeName();
            Class[] interfaces = class1.getInterfaces();
            Type[] types = class1.getGenericInterfaces();
            //UnitConverter converter = wgs84Unit.getConverterTo(osgbUnit);
            //UnitConverter converter2 = wgs84Unit.getConverterTo(SI.RADIAN);
            //UnitConverter converter3 = wgs84Unit.getConverterTo(SI.METER);
            Measure<Double, Angle> angleDist = Measure.valueOf(distOSGB, wgs84Unit);
            Measure<Double, Angle> radDist = angleDist.to(SI.RADIAN);

            System.out.println("Proj WGS: " + CRS.getProjectedCRS(wgs84CRS) + " Proj OSGB: " + CRS.getProjectedCRS(osgbCRS));
            System.out.println("WGS: " + wgs84Unit.isStandardUnit() + " OSGB: " + osgbUnit.isStandardUnit());
            System.out.println("Before: " + distOSGB + " After: " + radDist);

        } catch (FactoryException ex) {
            System.out.println("CRS Not Decoded");
        }

    }

}
