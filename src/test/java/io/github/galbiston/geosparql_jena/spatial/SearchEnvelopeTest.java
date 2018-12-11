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

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

/**
 *
 * @author Gerg
 */
public class SearchEnvelopeTest {

    public SearchEnvelopeTest() {
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

    public static final double X1 = -180;
    public static final double X2 = 180;
    public static final double Y1 = -90;
    public static final double Y2 = 90;

    /**
     * Test of build method, of class SearchEnvelope.
     */
    @Test
    public void testBuild_NORTH() {
        System.out.println("build_NORTH");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)", WKTDatatype.URI);
        CardinalDirection direction = CardinalDirection.NORTH;
        Envelope expResult = new Envelope(X1, X2, 10, Y2);
        Envelope result = SearchEnvelope.build(geometryWrapper, direction);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of build method, of class SearchEnvelope.
     */
    @Test
    public void testBuild_SOUTH() {
        System.out.println("build_SOUTH");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)", WKTDatatype.URI);
        CardinalDirection direction = CardinalDirection.SOUTH;
        Envelope expResult = new Envelope(X1, X2, Y1, 10);
        Envelope result = SearchEnvelope.build(geometryWrapper, direction);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of build method, of class SearchEnvelope.
     */
    @Test
    public void testBuild_EAST() {
        System.out.println("build_EAST");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)", WKTDatatype.URI);
        CardinalDirection direction = CardinalDirection.EAST;
        Envelope expResult = new Envelope(20, X2, Y1, Y2);
        Envelope result = SearchEnvelope.build(geometryWrapper, direction);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of build method, of class SearchEnvelope.
     */
    @Test
    public void testBuild_WEST() {
        System.out.println("build_WEST");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)", WKTDatatype.URI);
        CardinalDirection direction = CardinalDirection.WEST;
        Envelope expResult = new Envelope(X1, 20, Y1, Y2);
        Envelope result = SearchEnvelope.build(geometryWrapper, direction);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
