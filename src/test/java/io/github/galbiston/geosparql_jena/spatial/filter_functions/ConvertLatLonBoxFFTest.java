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
package io.github.galbiston.geosparql_jena.spatial.filter_functions;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexTestData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
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
public class ConvertLatLonBoxFFTest {

    public ConvertLatLonBoxFFTest() {
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
     * Test of exec method, of class ConvertLatLonBoxFF.
     */
    @Test
    public void testExec() {
        System.out.println("exec");
        NodeValue v1 = NodeValue.makeFloat(0.0f);
        NodeValue v2 = NodeValue.makeFloat(1.0f);
        NodeValue v3 = NodeValue.makeFloat(10.0f);
        NodeValue v4 = NodeValue.makeFloat(11.0f);
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((0 1, 10 1, 10 11, 0 11, 0 1))", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v1, v2, v3, v4);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class ConvertLatLonBoxFF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExec_pos0_fail() {
        System.out.println("exec_pos0_fail");
        NodeValue v1 = NodeValue.makeString("0.0");
        NodeValue v2 = NodeValue.makeFloat(1.0f);
        NodeValue v3 = NodeValue.makeFloat(10.0f);
        NodeValue v4 = NodeValue.makeFloat(11.0f);
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((0.0 1.0, 10.0 1.0, 10.0 11.0, 0.0 11.0, 0.0 1.0))", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v1, v2, v3, v4);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class ConvertLatLonBoxFF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExec_pos1_fail() {
        System.out.println("exec_pos1_fail");
        NodeValue v1 = NodeValue.makeFloat(0.0f);
        NodeValue v2 = NodeValue.makeString("1.0");
        NodeValue v3 = NodeValue.makeFloat(10.0f);
        NodeValue v4 = NodeValue.makeFloat(11.0f);
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((0.0 1.0, 10.0 1.0, 10.0 11.0, 0.0 11.0, 0.0 1.0))", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v1, v2, v3, v4);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class ConvertLatLonBoxFF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExec_pos2_fail() {
        System.out.println("exec_pos2_fail");
        NodeValue v1 = NodeValue.makeFloat(0.0f);
        NodeValue v2 = NodeValue.makeFloat(1.0f);
        NodeValue v3 = NodeValue.makeString("10.0");
        NodeValue v4 = NodeValue.makeFloat(11.0f);
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((0.0 1.0, 10.0 1.0, 10.0 11.0, 0.0 11.0, 0.0 1.0))", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v1, v2, v3, v4);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class ConvertLatLonBoxFF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExec_pos3_fail() {
        System.out.println("exec_pos3_fail");
        NodeValue v1 = NodeValue.makeFloat(0.0f);
        NodeValue v2 = NodeValue.makeFloat(0.0f);
        NodeValue v3 = NodeValue.makeFloat(10.0f);
        NodeValue v4 = NodeValue.makeString("10.0");
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((0.0 1.0, 10.0 1.0, 10.0 11.0, 0.0 11.0, 0.0 1.0))", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v1, v2, v3, v4);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class ConvertLatLonBoxFF.
     */
    @Test
    public void testExec_query() {
        System.out.println("exec_query");

        Dataset dataset = SpatialIndexTestData.createTestDataset();

        String query = "PREFIX spatialF: <http://jena.apache.org/function/spatial#>\n"
                + "\n"
                + "SELECT ?result\n"
                + "WHERE{\n"
                + "    BIND( spatialF:convertLatLonBox(0.0, 1.0, 10.0, 11.0) AS ?result) \n"
                + "}ORDER by ?result";

        List<Literal> results = new ArrayList<>();
        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                Literal result = qs.getLiteral("result");
                results.add(result);
            }
        }

        List<Literal> expResults = Arrays.asList(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((0 1, 10 1, 10 11, 0 11, 0 1))", WKTDatatype.INSTANCE));

        //System.out.println("Exp: " + expResults);
        //System.out.println("Res: " + results);
        assertEquals(expResults, results);
    }
}
