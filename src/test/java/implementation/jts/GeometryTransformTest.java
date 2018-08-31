/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.jts;

import implementation.parsers.wkt.WKTReader;
import implementation.registry.CRSRegistry;
import implementation.registry.MathTransformRegistry;
import implementation.vocabulary.SRS_URI;
import org.apache.sis.referencing.CRS;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 * @author Gerg
 */
public class GeometryTransformTest {

    public GeometryTransformTest() {
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
     * Test of transform method, of class GeometryTransformation.
     *
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    @Test
    public void testTransformPoint() throws FactoryException, MismatchedDimensionException, TransformException {
        System.out.println("transform_Point");
        Geometry sourceGeometry = WKTReader.extract("POINT ZM(5 10 8 3)").getGeometry();

        CoordinateReferenceSystem sourceCRS = CRS.forCode(SRS_URI.WGS84_CRS);
        CoordinateReferenceSystem targetCRS = CRS.forCode(CRSRegistry.DEFAULT_WKT_CRS84_CODE);
        MathTransform transform = MathTransformRegistry.getMathTransform(sourceCRS, targetCRS);

        Geometry expResult = WKTReader.extract("POINT ZM(10 5 8 3)").getGeometry();
        Geometry result = GeometryTransformation.transform(sourceGeometry, transform);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of transform method, of class GeometryTransformation.
     *
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    @Test
    public void testTransform_Polygon() throws FactoryException, MismatchedDimensionException, TransformException {
        System.out.println("transform_Polygon");
        Geometry sourceGeometry = WKTReader.extract("POLYGON(5.0 5.0, 5.0 15.0, 15.0 15.0, 15.0 5.0, 5.0 5.0)").getGeometry();

        CoordinateReferenceSystem sourceCRS = CRS.forCode(SRS_URI.WGS84_CRS);
        CoordinateReferenceSystem targetCRS = CRS.forCode(CRSRegistry.DEFAULT_WKT_CRS84_CODE);
        MathTransform transform = MathTransformRegistry.getMathTransform(sourceCRS, targetCRS);

        Geometry expResult = WKTReader.extract("POLYGON(5.0 5.0, 15.0 5.0, 15.0 15.0, 5.0 15.0, 5.0 5.0)").getGeometry();
        Geometry result = GeometryTransformation.transform(sourceGeometry, transform);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    //TODO - additional tests
}
