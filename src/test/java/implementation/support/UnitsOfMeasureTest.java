/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.support;

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
public class UnitsOfMeasureTest {

    private static final double RADS_TO_DEGREES = 180 / Math.PI;

    public UnitsOfMeasureTest() {
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
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testConversionRadianToMetre() throws FactoryException {
        System.out.println("conversionRadianToMetre");
        double targetDistance = 0.5;
        String targetDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
        CoordinateReferenceSystem crs;

        crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB - metres projected

        UnitsOfMeasure sourceUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = targetDistance * 6371000;
        double result = UnitsOfMeasure.conversion(targetDistance, targetDistanceUnitURI, sourceUnitsOfMeasure);
        //System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testConversionMetreToDegree() throws FactoryException {
        System.out.println("conversionMetreToDegree");
        double targetDistance = 100.0;
        String targetDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
        CoordinateReferenceSystem crs;

        crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");  //OSGB - metres projected

        UnitsOfMeasure sourceUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = (targetDistance / 6371000) * RADS_TO_DEGREES;
        double result = UnitsOfMeasure.conversion(targetDistance, targetDistanceUnitURI, sourceUnitsOfMeasure);
        //System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testConversionMetreToMetre() throws FactoryException {
        System.out.println("conversionMetreToMetre");
        double targetDistance = 100.0;
        String targetDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
        CoordinateReferenceSystem crs;

        crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB - metres projected

        UnitsOfMeasure sourceUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = 100.0;
        double result = UnitsOfMeasure.conversion(targetDistance, targetDistanceUnitURI, sourceUnitsOfMeasure);
        //System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testConversionDegreeToDegree() throws FactoryException {
        System.out.println("conversionDegreeToDegree");
        double targetDistance = 100.0;
        String targetDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
        CoordinateReferenceSystem crs;

        crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");  //WGS84 degrees non-projected

        UnitsOfMeasure sourceUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = 100.0;
        double result = UnitsOfMeasure.conversion(targetDistance, targetDistanceUnitURI, sourceUnitsOfMeasure);
        //System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testConversionRadianToDegree() throws FactoryException {
        System.out.println("conversionRadianToDegree");
        double targetDistance = 0.5;
        String targetDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
        CoordinateReferenceSystem crs;

        crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");  //WGS84 degrees non-projected

        UnitsOfMeasure sourceUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = targetDistance * RADS_TO_DEGREES;
        double result = UnitsOfMeasure.conversion(targetDistance, targetDistanceUnitURI, sourceUnitsOfMeasure);
        //System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testConversionDegreeToMetre() throws FactoryException {
        System.out.println("conversionDegreeToMetre");
        double targetDistance = 10.0;
        String targetDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
        CoordinateReferenceSystem crs;

        crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB metres projected

        UnitsOfMeasure sourceUnitsOfMeasure = new UnitsOfMeasure(crs);

        double expResult = (targetDistance / RADS_TO_DEGREES) * 6371000;
        double result = UnitsOfMeasure.conversion(targetDistance, targetDistanceUnitURI, sourceUnitsOfMeasure);
        //System.out.println("Expected: " + expResult + " Result: " + result);

        assertEquals(expResult, result, 0.0);
    }

}
