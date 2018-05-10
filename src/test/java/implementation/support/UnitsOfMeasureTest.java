/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.support;

import implementation.UnitsOfMeasure;
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
    @Test(expected = NullPointerException.class)
    public void testConversionRadianToMetre() throws FactoryException {
        System.out.println("conversionRadianToMetre");
        double distance = 0.5;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
        CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB - metres projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        Double expResult = null;
        Double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test(expected = NullPointerException.class)
    public void testConversionMetreToDegree() throws FactoryException {
        System.out.println("conversionMetreToDegree");
        double distance = 100.0;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
        CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");  //OSGB - metres projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        Double expResult = null;
        Double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
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
        double distance = 100.0;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
        CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB - metres projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = 100.0;
        double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
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
        double distance = 100.0;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
        CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");  //WGS84 degrees non-projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = 100.0;
        double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
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
        double distance = 0.5;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
        CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/4326");  //WGS84 degrees non-projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        double radsToDegrees = 180 / Math.PI;
        double expResult = distance * radsToDegrees;
        double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test(expected = NullPointerException.class)
    public void testConversionDegreeToMetre() throws FactoryException {
        System.out.println("conversionDegreeToMetre");
        double distance = 10.0;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
        CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB metres projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);

        Double expResult = null;
        Double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

}
