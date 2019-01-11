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
package io.github.galbiston.geosparql_jena.spatial.property_functions;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexTestData;
import java.util.ArrayList;
import java.util.Arrays;
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
public class GenericSpatialPropertyFunctionTest {

    public GenericSpatialPropertyFunctionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        GeoSPARQLConfig.setupSpatial();
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
     * Test of execEvaluated method, of class GenericSpatialPropertyFunction.
     */
    @Test
    public void testExecEvaluated_Nearby_none() {
        System.out.println("execEvaluated_Nearby_none");

        Dataset dataset = SpatialIndexTestData.createTestDataset();
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();
        SpatialIndex.setSpatialIndex(dataset, spatialIndex);

        String query = "PREFIX spatial: <http://jena.apache.org/spatial#>\n"
                + "\n"
                + "SELECT ?subj\n"
                + "WHERE{\n"
                + "    ?subj spatial:nearby(48.857487 2.373047 200) .\n"
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

        List<Resource> expResult = new ArrayList<>();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of execEvaluated method, of class GenericSpatialPropertyFunction.
     */
    @Test
    public void testExecEvaluated_Nearby_one() {
        System.out.println("execEvaluated_Nearby_one");

        Dataset dataset = SpatialIndexTestData.createTestDataset();
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();
        SpatialIndex.setSpatialIndex(dataset, spatialIndex);

        String query = "PREFIX spatial: <http://jena.apache.org/spatial#>\n"
                + "\n"
                + "SELECT ?subj\n"
                + "WHERE{\n"
                + "    ?subj spatial:nearby(48.857487 2.373047 350) .\n"
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

        List<Resource> expResult = Arrays.asList(SpatialIndexTestData.LONDON_FEATURE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of execEvaluated method, of class GenericSpatialPropertyFunction.
     */
    @Test
    public void testExecEvaluated_West() {
        System.out.println("execEvaluated_West");

        Dataset dataset = SpatialIndexTestData.createTestDataset();
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();
        SpatialIndex.setSpatialIndex(dataset, spatialIndex);

        String query = "PREFIX spatial: <http://jena.apache.org/spatial#>\n"
                + "\n"
                + "SELECT ?subj\n"
                + "WHERE{\n"
                + "    ?subj spatial:west(48.857487 2.373047) .\n"
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

    /**
     * Test of execEvaluated method, of class GenericSpatialPropertyFunction.
     */
    @Test
    public void testExecEvaluated_East() {
        System.out.println("execEvaluated_East");

        Dataset dataset = SpatialIndexTestData.createTestDataset();
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();
        SpatialIndex.setSpatialIndex(dataset, spatialIndex);

        String query = "PREFIX spatial: <http://jena.apache.org/spatial#>\n"
                + "\n"
                + "SELECT ?subj\n"
                + "WHERE{\n"
                + "    ?subj spatial:east(48.857487 2.373047) .\n"
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

        List<Resource> expResult = Arrays.asList(SpatialIndexTestData.AUCKLAND_FEATURE, SpatialIndexTestData.PERTH_FEATURE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
