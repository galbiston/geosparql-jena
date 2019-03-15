/*
 * Copyright 2019 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.implementation;

import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.CoordinateXYZM;

/**
 *
 *
 */
public class DimensionInfoTest {

    public DimensionInfoTest() {
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
     * Test of findForPoint method, of class DimensionInfo.
     */
    @Test
    public void testFindForPoint_xy() {
        System.out.println("findForPoint_xy");
        Coordinate coordinate = new Coordinate(1.0, 2.0);
        DimensionInfo expResult = DimensionInfo.XY_POINT;
        DimensionInfo result = DimensionInfo.findForPoint(coordinate);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForPoint method, of class DimensionInfo.
     */
    @Test
    public void testFindForPoint_xyz() {
        System.out.println("findForPoint_xyz");
        Coordinate coordinate = new Coordinate(1.0, 2.0, 3.0);
        DimensionInfo expResult = DimensionInfo.XYZ_POINT;
        DimensionInfo result = DimensionInfo.findForPoint(coordinate);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForPoint method, of class DimensionInfo.
     */
    @Test
    public void testFindForPoint_xym() {
        System.out.println("findForPoint_xym");
        Coordinate coordinate = new CoordinateXYM(1.0, 2.0, 3.0);
        DimensionInfo expResult = DimensionInfo.XYM_POINT;
        DimensionInfo result = DimensionInfo.findForPoint(coordinate);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForPoint method, of class DimensionInfo.
     */
    @Test
    public void testFindForPoint_xyzm() {
        System.out.println("findForPoint_xyzm");
        Coordinate coordinate = new CoordinateXYZM(1.0, 2.0, 3.0, 4.0);
        DimensionInfo expResult = DimensionInfo.XYZM_POINT;
        DimensionInfo result = DimensionInfo.findForPoint(coordinate);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForLineString method, of class DimensionInfo.
     */
    @Test
    public void testFindForLineString_xy() {
        System.out.println("findForLineString_xy");
        Coordinate coordinate = new Coordinate(1.0, 2.0);
        List<Coordinate> coordinates = Arrays.asList(coordinate);
        DimensionInfo expResult = DimensionInfo.XY_LINESTRING;
        DimensionInfo result = DimensionInfo.findForLineString(coordinates);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForLineString method, of class DimensionInfo.
     */
    @Test
    public void testFindForLineString_xyz() {
        System.out.println("findForLineString_xyz");
        Coordinate coordinate = new Coordinate(1.0, 2.0, 3.0);
        List<Coordinate> coordinates = Arrays.asList(coordinate);
        DimensionInfo expResult = DimensionInfo.XYZ_LINESTRING;
        DimensionInfo result = DimensionInfo.findForLineString(coordinates);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForLineString method, of class DimensionInfo.
     */
    @Test
    public void testFindForLineString_xym() {
        System.out.println("findForLineString_xym");
        Coordinate coordinate = new CoordinateXYM(1.0, 2.0, 3.0);
        List<Coordinate> coordinates = Arrays.asList(coordinate);
        DimensionInfo expResult = DimensionInfo.XYM_LINESTRING;
        DimensionInfo result = DimensionInfo.findForLineString(coordinates);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForLineString method, of class DimensionInfo.
     */
    @Test
    public void testFindForLineString_xyzm() {
        System.out.println("findForLineString_xyzm");
        Coordinate coordinate = new CoordinateXYZM(1.0, 2.0, 3.0, 4.0);
        List<Coordinate> coordinates = Arrays.asList(coordinate);
        DimensionInfo expResult = DimensionInfo.XYZM_LINESTRING;
        DimensionInfo result = DimensionInfo.findForLineString(coordinates);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForPolygon method, of class DimensionInfo.
     */
    @Test
    public void testFindForPolygon_xy() {
        System.out.println("findForPolygon_xy");
        Coordinate coordinate = new Coordinate(1.0, 2.0);
        List<Coordinate> coordinates = Arrays.asList(coordinate);
        DimensionInfo expResult = DimensionInfo.XY_POLYGON;
        DimensionInfo result = DimensionInfo.findForPolygon(coordinates);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForPolygon method, of class DimensionInfo.
     */
    @Test
    public void testFindForPolygon_xyz() {
        System.out.println("findForPolygon_xyz");
        Coordinate coordinate = new Coordinate(1.0, 2.0, 3.0);
        List<Coordinate> coordinates = Arrays.asList(coordinate);
        DimensionInfo expResult = DimensionInfo.XYZ_POLYGON;
        DimensionInfo result = DimensionInfo.findForPolygon(coordinates);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForPolygon method, of class DimensionInfo.
     */
    @Test
    public void testFindForPolygon_xym() {
        System.out.println("findForPolygon_xym");
        Coordinate coordinate = new CoordinateXYM(1.0, 2.0, 3.0);
        List<Coordinate> coordinates = Arrays.asList(coordinate);
        DimensionInfo expResult = DimensionInfo.XYM_POLYGON;
        DimensionInfo result = DimensionInfo.findForPolygon(coordinates);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of findForPolygon method, of class DimensionInfo.
     */
    @Test
    public void testFindForPolygon_xyzm() {
        System.out.println("findForPolygon_xyzm");
        Coordinate coordinate = new CoordinateXYZM(1.0, 2.0, 3.0, 4.0);
        List<Coordinate> coordinates = Arrays.asList(coordinate);
        DimensionInfo expResult = DimensionInfo.XYZM_POLYGON;
        DimensionInfo result = DimensionInfo.findForPolygon(coordinates);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
