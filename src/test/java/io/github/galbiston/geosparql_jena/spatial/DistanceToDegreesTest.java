/*
 * Copyright 2018 .
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
package io.github.galbiston.geosparql_jena.spatial;

import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gerg
 */
public class DistanceToDegreesTest {

    public DistanceToDegreesTest() {
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
     * Test of convert method, of class DistanceToDegrees.
     */
    @Test
    public void testConvert_0_degree() {
        System.out.println("convert_0_degree");
        double distance = 111319.9;
        String unitsURI = Unit_URI.METRE_URL;
        double latitude = 0.0;
        double expResult = 1.0;
        double result = DistanceToDegrees.convert(distance, unitsURI, latitude);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of convert method, of class DistanceToDegrees.
     */
    @Test
    public void testConvert_23_degree() {
        System.out.println("convert_23_degree");
        double distance = 102470.508;
        String unitsURI = Unit_URI.METRE_URL;
        double latitude = 23.0;
        double expResult = 1.0;
        double result = DistanceToDegrees.convert(distance, unitsURI, latitude);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of convert method, of class DistanceToDegrees.
     */
    @Test
    public void testConvert_45_degree() {
        System.out.println("convert_45_degree");
        double distance = 78715.05;
        String unitsURI = Unit_URI.METRE_URL;
        double latitude = 45.0;
        double expResult = 1.0;
        double result = DistanceToDegrees.convert(distance, unitsURI, latitude);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of convert method, of class DistanceToDegrees.
     */
    @Test
    public void testConvert_67_degree() {
        System.out.println("convert_67_degree");
        double distance = 43496.15;
        String unitsURI = Unit_URI.METRE_URL;
        double latitude = 67.0;
        double expResult = 1.0;
        double result = DistanceToDegrees.convert(distance, unitsURI, latitude);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of convert method, of class DistanceToDegrees.
     */
    @Test
    public void testConvert_67_degree2() {
        System.out.println("convert_67_degree2");
        double distance = 1.0;
        String unitsURI = Unit_URI.DEGREE_URL;
        double latitude = 67.0;
        double expResult = 1.0;
        double result = DistanceToDegrees.convert(distance, unitsURI, latitude);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.00001);
    }

}
