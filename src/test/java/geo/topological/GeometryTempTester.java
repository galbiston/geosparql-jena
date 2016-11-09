/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Greg
 */
public class GeometryTempTester {

    public GeometryTempTester() {
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
     * GeometryTempTester of execEvaluated method, of class GenericPropertyFunction.
     */
    @Test
    public void testExec() {
        System.out.println("exec");

        WKTReader wktReader = new WKTReader();
        try {
            Geometry geom1 = wktReader.read("Polygon((-83.6 34.1, -83.2 34.1, -83.2 34.5, -83.6 34.5, -83.6 34.1))");
            Geometry geom2 = wktReader.read("LineString(-83.8 34.0, -83.4 34.0)");

            boolean result = geom1.overlaps(geom2);
            boolean expResult = true;
            assertEquals(result, expResult);

        } catch (ParseException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

    }

}
