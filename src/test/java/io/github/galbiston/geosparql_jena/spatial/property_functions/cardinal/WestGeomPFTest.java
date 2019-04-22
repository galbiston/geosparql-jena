/*
 * Copyright 2019 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.spatial.CardinalDirection;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexTestData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 */
public class WestGeomPFTest {

    public WestGeomPFTest() {
    }

    @BeforeClass
    public static void setUpClass() {
         GeoSPARQLConfig.setupNoIndex();
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
     * Test of buildSearchEnvelope method, of class WestGeomPF.
     */
    @Test
    public void testBuildSearchEnvelope() {
        System.out.println("buildSearchEnvelope");
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PARIS_GEOMETRY_WRAPPER;
        WestGeomPF instance = new WestGeomPF();
        SearchEnvelope expResult = SearchEnvelope.build(geometryWrapper, SpatialIndexTestData.WGS_84_SRS_INFO, CardinalDirection.WEST);
        SearchEnvelope result = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_SRS_INFO);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkSearchEnvelope method, of class WestGeomPF.
     */
    @Test
    public void testCheckSearchEnvelope_no_wrap() {
        System.out.println("checkSearchEnvelope_no_wrap");
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();

        //Search Envelope
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PERTH_GEOMETRY_WRAPPER;
        WestGeomPF instance = new WestGeomPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_SRS_INFO); //Needed to initialise the search.
        HashSet<Resource> expResult = new HashSet<>(Arrays.asList(SpatialIndexTestData.LONDON_FEATURE, SpatialIndexTestData.PERTH_FEATURE));
        HashSet<Resource> result = searchEnvelope.check(spatialIndex);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkSearchEnvelope method, of class WestGeomPF.
     */
    @Test
    public void testCheckSearchEnvelope_wrap() {
        System.out.println("checkSearchEnvelope_wrap");
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();

        //Search Envelope
        GeometryWrapper geometryWrapper = SpatialIndexTestData.HONOLULU_GEOMETRY_WRAPPER;
        WestGeomPF instance = new WestGeomPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_SRS_INFO); //Needed to initialise the search.
        HashSet<Resource> expResult = new HashSet<>(Arrays.asList(SpatialIndexTestData.AUCKLAND_FEATURE, SpatialIndexTestData.PERTH_FEATURE, SpatialIndexTestData.HONOLULU_FEATURE));
        HashSet<Resource> result = searchEnvelope.check(spatialIndex);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of execEvaluated method, of class WestGeomPF.
     */
    @Test
    public void testExecEvaluated() {
        System.out.println("execEvaluated");

        Dataset dataset = SpatialIndexTestData.createTestDataset();

        String query = "PREFIX spatial: <http://jena.apache.org/spatial#>\n"
                + "\n"
                + "SELECT ?subj\n"
                + "WHERE{\n"
                + "    BIND( \"<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(48.857487 2.373047)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> AS ?geom)"
                + "    ?subj spatial:westGeom(?geom) .\n"
                + "}ORDER by ?subj";

        List<Resource> result = new ArrayList<>();
        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                Resource feature = qs.getResource("subj");
                result.add(feature);
            }
        }

        List<Resource> expResult = Arrays.asList(SpatialIndexTestData.HONOLULU_FEATURE, SpatialIndexTestData.LONDON_FEATURE, SpatialIndexTestData.NEW_YORK_FEATURE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
