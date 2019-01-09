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
    public void testExecEvaluated() {
        System.out.println("execEvaluated");

        GeoSPARQLConfig.setupSpatial();
        Dataset dataset = SpatialIndexTestData.createTestDataset();
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();
        SpatialIndex.setSpatialIndex(dataset, spatialIndex);

        dataset.begin();
        String query = "PREFIX spatial: <http://jena.apache.org/function/spatial#>\n"
                + "\n"
                + "SELECT ?subject\n"
                + "WHERE{\n"
                + "    ?subject spatial:nearby(48.857487 2.373047 200) .\n"
                + "}ORDER by ?subject";

        List<Resource> result = new ArrayList<>();
        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                Resource feature = qs.getResource("feature");
                result.add(feature);
            }
        }

        dataset.end();
        List<Resource> expResult = Arrays.asList(SpatialIndexTestData.LONDON_FEATURE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
