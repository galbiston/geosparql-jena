/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import java.util.List;
import org.apache.jena.rdf.model.Literal;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Greg Albiston
 */
public class GeometryUtilTest {

    public GeometryUtilTest() {
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
     * Test of pointsToLineString method, of class GeometryUtil.
     */
    @Test
    public void testPointsToLineString() {
        System.out.println("pointsToLineString");
        List<Literal> points = null;
        Literal expResult = null;
        Literal result = GeometryUtil.pointsToLineString(points);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
