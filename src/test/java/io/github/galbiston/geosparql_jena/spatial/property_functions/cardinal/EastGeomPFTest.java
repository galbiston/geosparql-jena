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
package io.github.galbiston.geosparql_jena.spatial.property_functions.cardinal;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.spatial.CardinalDirection;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexTestData;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.jena.rdf.model.Resource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

/**
 *
 *
 */
public class EastGeomPFTest {

    public EastGeomPFTest() {
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
     * Test of buildSearchEnvelope method, of class EastGeomPF.
     */
    @Test
    public void testBuildSearchEnvelope() {
        System.out.println("buildSearchEnvelope");
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PARIS_GEOMETRY_WRAPPER;
        EastGeomPF instance = new EastGeomPF();
        Envelope expResult = SearchEnvelope.build(geometryWrapper, CardinalDirection.EAST);
        Envelope result = instance.buildSearchEnvelope(geometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkSearchEnvelope method, of class EastGeomPF.
     */
    @Test
    public void testCheckSearchEnvelope_no_wrap() {
        System.out.println("checkSearchEnvelope_no_wrap");
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();

        //Search Envelope
        GeometryWrapper geometryWrapper = SpatialIndexTestData.HONOLULU_GEOMETRY_WRAPPER;
        EastGeomPF instance = new EastGeomPF();
        Envelope envelope = instance.buildSearchEnvelope(geometryWrapper); //Needed to initialise the search.
        HashSet<Resource> expResult = new HashSet<>(Arrays.asList(SpatialIndexTestData.LONDON_FEATURE, SpatialIndexTestData.HONOLULU_FEATURE, SpatialIndexTestData.NEW_YORK_FEATURE));
        HashSet<Resource> result = instance.checkSearchEnvelope(spatialIndex, envelope);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkSearchEnvelope method, of class EastGeomPF.
     */
    @Test
    public void testCheckSearchEnvelope_wrap() {
        System.out.println("checkSearchEnvelope_wrap");
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();

        //Search Envelope
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PERTH_GEOMETRY_WRAPPER;
        EastGeomPF instance = new EastGeomPF();
        Envelope envelope = instance.buildSearchEnvelope(geometryWrapper); //Needed to initialise the search.
        HashSet<Resource> expResult = new HashSet<>(Arrays.asList(SpatialIndexTestData.AUCKLAND_FEATURE, SpatialIndexTestData.PERTH_FEATURE, SpatialIndexTestData.HONOLULU_FEATURE, SpatialIndexTestData.NEW_YORK_FEATURE));
        HashSet<Resource> result = instance.checkSearchEnvelope(spatialIndex, envelope);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
