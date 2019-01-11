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
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.jena.rdf.model.Resource;
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
        SearchEnvelope expResult = new SearchEnvelope(new Envelope(X1, X2, 10, Y2));
        SearchEnvelope result = SearchEnvelope.build(geometryWrapper, direction);

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
        SearchEnvelope expResult = new SearchEnvelope(new Envelope(X1, X2, Y1, 10));
        SearchEnvelope result = SearchEnvelope.build(geometryWrapper, direction);

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
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 -20.0)", WKTDatatype.URI);
        CardinalDirection direction = CardinalDirection.EAST;
        SearchEnvelope expResult = new SearchEnvelope(new Envelope(-20, 160, Y1, Y2));
        SearchEnvelope result = SearchEnvelope.build(geometryWrapper, direction);

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
        SearchEnvelope expResult = new SearchEnvelope(new Envelope(-160, 20, Y1, Y2));
        SearchEnvelope result = SearchEnvelope.build(geometryWrapper, direction);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of build method, of class SearchEnvelope.
     */
    @Test
    public void testBuildWrap_EAST() {
        System.out.println("buildWrap_EAST");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 100.0)", WKTDatatype.URI);
        CardinalDirection direction = CardinalDirection.EAST;
        SearchEnvelope expResult = new SearchEnvelope(new Envelope(100, 280, Y1, Y2));
        SearchEnvelope result = SearchEnvelope.build(geometryWrapper, direction);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of build method, of class SearchEnvelope.
     */
    @Test
    public void testBuildWrap_WEST() {
        System.out.println("buildWrap_WEST");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 -20.0)", WKTDatatype.URI);
        CardinalDirection direction = CardinalDirection.WEST;
        SearchEnvelope expResult = new SearchEnvelope(new Envelope(-200, -20, Y1, Y2));
        SearchEnvelope result = SearchEnvelope.build(geometryWrapper, direction);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMainEnvelope method, of class SearchEnvelope.
     */
    @Test
    public void testGetMainEnvelope() {
        System.out.println("getMainEnvelope");
        SearchEnvelope instance = new SearchEnvelope(new Envelope(0, 10, 0, 10));
        Envelope expResult = new Envelope(0, 10, 0, 10);
        Envelope result = instance.getMainEnvelope();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMainEnvelope method, of class SearchEnvelope.
     */
    @Test
    public void testGetMainEnvelope2() {
        System.out.println("getMainEnvelope2");
        SearchEnvelope instance = new SearchEnvelope(new Envelope(40, 220, 0, 10));
        Envelope expResult = new Envelope(40, 180, 0, 10);
        Envelope result = instance.getMainEnvelope();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMainEnvelope method, of class SearchEnvelope.
     */
    @Test
    public void testGetMainEnvelope3() {
        System.out.println("getMainEnvelope3");
        SearchEnvelope instance = new SearchEnvelope(new Envelope(-220, -40, 0, 10));
        Envelope expResult = new Envelope(-180, -40, 0, 10);
        Envelope result = instance.getMainEnvelope();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWrapEnvelope method, of class SearchEnvelope.
     */
    @Test
    public void testGetWrapEnvelope() {
        System.out.println("getWrapEnvelope");
        SearchEnvelope instance = new SearchEnvelope(new Envelope(0, 10, 0, 10));

        Envelope expResult = null;
        Envelope result = instance.getWrapEnvelope();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWrapEnvelope method, of class SearchEnvelope.
     */
    @Test
    public void testGetWrapEnvelope2() {
        System.out.println("getWrapEnvelope2");
        SearchEnvelope instance = new SearchEnvelope(new Envelope(40, 220, 0, 10));

        Envelope expResult = new Envelope(-180, -140, 0, 10);
        Envelope result = instance.getWrapEnvelope();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWrapEnvelope method, of class SearchEnvelope.
     */
    @Test
    public void testGetWrapEnvelope3() {
        System.out.println("getWrapEnvelope3");
        SearchEnvelope instance = new SearchEnvelope(new Envelope(-220, -40, 0, 10));

        Envelope expResult = new Envelope(140, 180, 0, 10);
        Envelope result = instance.getWrapEnvelope();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of build method, of class SearchEnvelope.
     */
    @Test
    public void testBuild_3args() {
        System.out.println("build");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(0 0)", WKTDatatype.URI);
        double radius = 10;
        String unitsURI = Unit_URI.KILOMETER_URL;
        SearchEnvelope expResult = new SearchEnvelope(new Envelope(-0.08983152841195216, 0.08983152841195216, -0.08983152841195216, 0.08983152841195216));
        SearchEnvelope result = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of build method, of class SearchEnvelope.
     */
    @Test
    public void testBuild_GeometryWrapper() {
        System.out.println("build");
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PARIS_GEOMETRY_WRAPPER;
        SearchEnvelope expResult = new SearchEnvelope(geometryWrapper.getEnvelope());
        SearchEnvelope result = SearchEnvelope.build(geometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of testSearchEnvelope method, of class NearbyGeomPF.
     */
    @Test
    public void testCheck() {
        System.out.println("check");
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();

        //Search Envelope
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PARIS_GEOMETRY_WRAPPER;
        float radius = 345;
        String unitsURI = Unit_URI.KILOMETER_URL;
        SearchEnvelope instance = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        //Function Test
        HashSet<Resource> expResult = new HashSet<>(Arrays.asList(SpatialIndexTestData.LONDON_FEATURE));
        HashSet<Resource> result = instance.check(spatialIndex);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of testSearchEnvelope method, of class NearbyGeomPF.
     */
    @Test
    public void testCheck_empty() {
        System.out.println("check_empty");
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();

        //Search Envelope
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PARIS_GEOMETRY_WRAPPER;
        float radius = 2;
        String unitsURI = Unit_URI.KILOMETER_URL;
        SearchEnvelope instance = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        //Function Test
        HashSet<Resource> expResult = new HashSet<>();
        HashSet<Resource> result = instance.check(spatialIndex);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
