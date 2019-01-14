/*
 * Copyright 2019 .
 *
 * Licensed under the Apache License, Version 2.3488 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.3488
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.implementation;

import io.github.galbiston.geosparql_jena.implementation.jts.CustomGeometryFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 *
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GreatCirclePointDistanceTest {

    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    public GreatCirclePointDistanceTest() {
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
     * Test of latitude method, of class GreatCirclePointDistance.
     */
    @Test
    public void testLatitude() {
        System.out.println("latitude");
        double startLat = 48.85341;
        double startLon = -2.3488;
        double distance = 100000.0;
        double bearingRad = 0.0;
        GreatCirclePointDistance instance = new GreatCirclePointDistance(startLat, startLon, distance);
        double expResult = 0.86833;
        double result = instance.latitude(bearingRad);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of longitude method, of class GreatCirclePointDistance.
     */
    @Test
    public void testLongitude() {
        System.out.println("longitude");
        double endLatRad = 0.86833;
        double startLat = 48.85341;
        double startLon = -2.3488;
        double distance = 100000.0;
        double bearingRad = 0.0;
        GreatCirclePointDistance instance = new GreatCirclePointDistance(startLat, startLon, distance);
        double expResult = -0.04099;
        double result = instance.longitude(endLatRad, bearingRad);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of radToPoint method, of class GreatCirclePointDistance.
     */
    @Test
    public void testRadToPoint_double_double() {
        System.out.println("radToPoint");
        double latRad = 0.5;
        double lonRad = 0.1;
        Point expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(Math.toDegrees(latRad), Math.toDegrees(lonRad)));
        Point result = GreatCirclePointDistance.radToPoint(latRad, lonRad);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of radToPoint method, of class GreatCirclePointDistance.
     */
    @Test
    public void testRadToPoint_3args_false() {
        System.out.println("radToPoint_false");
        double latRad = 0.5;
        double lonRad = Math.PI + 0.1;
        boolean isNormaliseLon = false;
        Point expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(Math.toDegrees(latRad), Math.toDegrees(lonRad)));
        Point result = GreatCirclePointDistance.radToPoint(latRad, lonRad, isNormaliseLon);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of radToPoint method, of class GreatCirclePointDistance.
     */
    @Test
    public void testRadToPoint_3args_true() {
        System.out.println("radToPoint_true");
        double latRad = 0.5;
        double lonRad = Math.PI + 0.2;
        boolean isNormaliseLon = true;
        Point expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(Math.toDegrees(latRad), Math.toDegrees(-Math.PI + 0.2)));
        Point result = GreatCirclePointDistance.radToPoint(latRad, lonRad, isNormaliseLon);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of normaliseLongitude method, of class GreatCirclePointDistance.
     */
    @Test
    public void testNormaliseLongitude() {
        System.out.println("normaliseLongitude");
        double lonDegrees = 185;
        double expResult = -175;
        double result = GreatCirclePointDistance.normaliseLongitude(lonDegrees);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getPoint method, of class GreatCirclePointDistance.
     */
    @Test
    public void testGetPoint_3args_North() {
        System.out.println("getPoint_North");

        double startLat = 48.85341;
        double startLon = -2.3488;
        double distance = 100000.0;
        Point startPoint = GEOMETRY_FACTORY.createPoint(new Coordinate(startLat, startLon));
        double bearing = 0.0;
        Point expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(49.75172528411952, -2.3488));
        Point result = GreatCirclePointDistance.getPoint(startPoint, distance, bearing);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPoint method, of class GreatCirclePointDistance.
     */
    @Test
    public void testGetPoint_4args_North() {
        System.out.println("getPoint_North");
        double startLat = 48.85341;
        double startLon = -2.3488;
        double distance = 100000.0;
        double bearing = 0.0;
        Point expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(49.75172528411952, -2.3488));
        Point result = GreatCirclePointDistance.getPoint(startLat, startLon, distance, bearing);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPoint method, of class GreatCirclePointDistance.
     */
    @Test
    public void testGetPoint_3args_East() {
        System.out.println("getPoint_East");

        double startLat = 48.85341;
        double startLon = -2.3488;
        double distance = 100000.0;
        Point startPoint = GEOMETRY_FACTORY.createPoint(new Coordinate(startLat, startLon));
        double bearing = 90.0;
        Point expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(48.84535149015191, -0.9837003466724981));
        Point result = GreatCirclePointDistance.getPoint(startPoint, distance, bearing);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPoint method, of class GreatCirclePointDistance.
     */
    @Test
    public void testGetPoint_4args_East() {
        System.out.println("getPoint_East");
        double startLat = 48.85341;
        double startLon = -2.3488;
        double distance = 100000.0;
        double bearing = 90.0;
        Point expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(48.84535149015191, -0.9837003466724981));
        Point result = GreatCirclePointDistance.getPoint(startLat, startLon, distance, bearing);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}