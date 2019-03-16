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
package io.github.galbiston.geosparql_jena.spatial.filter_functions;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexTestData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.NodeFactory;
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
public class GreatCircleGeomFFTest {

    public GreatCircleGeomFFTest() {
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
     * Test of exec method, of class GreatCircleGeomFF.
     */
    @Test
    public void testExec() {
        System.out.println("exec");
        NodeValue v1 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)", WKTDatatype.INSTANCE);
        NodeValue v2 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 21.0)", WKTDatatype.INSTANCE);
        NodeValue v3 = NodeValue.makeNode(NodeFactory.createURI(Unit_URI.KILOMETER_URL));
        GreatCircleGeomFF instance = new GreatCircleGeomFF();
        double expResult = 109.5057;
        double result = instance.exec(v1, v2, v3).getDouble();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0001);
    }

    /**
     * Test of exec method, of class GreatCircleGeomFF.
     */
    @Test
    public void testExec2() {
        System.out.println("exec2");
        NodeValue v1 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)", WKTDatatype.INSTANCE);
        NodeValue v2 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(11.0 20.0)", WKTDatatype.INSTANCE);
        NodeValue v3 = NodeValue.makeString(Unit_URI.KILOMETER_URL);
        GreatCircleGeomFF instance = new GreatCircleGeomFF();
        double expResult = 111.1950;
        double result = instance.exec(v1, v2, v3).getDouble();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0001);
    }

    /**
     * Test of exec method, of class GreatCircleGeomFF.
     */
    @Test
    public void testExec_Paris_London() {
        System.out.println("exec_Paris_London");
        NodeValue v1 = SpatialIndexTestData.PARIS_GEOMETRY_WRAPPER.asNodeValue();
        NodeValue v2 = SpatialIndexTestData.LONDON_GEOMETRY_WRAPPER.asNodeValue();
        NodeValue v3 = NodeValue.makeString(Unit_URI.KILOMETER_URL);
        GreatCircleGeomFF instance = new GreatCircleGeomFF();
        double expResult = 343.7713;
        double result = instance.exec(v1, v2, v3).getDouble();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0001);
    }

    /**
     * Test of exec method, of class GreatCircleGeomFF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExec_pos0_fail() {
        System.out.println("exec_pos0_fail");
        NodeValue v1 = NodeValue.makeString("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)");
        NodeValue v2 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0001)", WKTDatatype.INSTANCE);
        NodeValue v3 = NodeValue.makeNode(NodeFactory.createURI(Unit_URI.KILOMETER_URL));
        GreatCircleGeomFF instance = new GreatCircleGeomFF();
        NodeValue expResult = NodeValue.makeDouble(20);
        NodeValue result = instance.exec(v1, v2, v3);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class GreatCircleGeomFF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExec_pos1_fail() {
        System.out.println("exec_pos1_fail");
        NodeValue v1 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)", WKTDatatype.INSTANCE);
        NodeValue v2 = NodeValue.makeString("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0001)");
        NodeValue v3 = NodeValue.makeNode(NodeFactory.createURI(Unit_URI.KILOMETER_URL));
        GreatCircleGeomFF instance = new GreatCircleGeomFF();
        NodeValue expResult = NodeValue.makeDouble(20);
        NodeValue result = instance.exec(v1, v2, v3);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class GreatCircleGeomFF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExec_pos2_fail() {
        System.out.println("exec_pos2_fail");
        NodeValue v1 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0)", WKTDatatype.INSTANCE);
        NodeValue v2 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(10.0 20.0001)", WKTDatatype.INSTANCE);
        NodeValue v3 = NodeValue.makeInteger(20);
        GreatCircleGeomFF instance = new GreatCircleGeomFF();
        NodeValue expResult = NodeValue.makeDouble(20);
        NodeValue result = instance.exec(v1, v2, v3);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class GreatCircleGeomFF.
     */
    @Test
    public void testExec_query() {
        System.out.println("exec_query");

        Dataset dataset = SpatialIndexTestData.createTestDataset();

        String query = "PREFIX spatialF: <http://jena.apache.org/function/spatial#>\n"
                + "\n"
                + "SELECT ?dist\n"
                + "WHERE{\n"
                + "    BIND( \"<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(51.50853 -0.12574)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> AS ?geom1)"
                + "    BIND( \"<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(48.857487 2.373047)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> AS ?geom2)"
                + "    BIND( spatialF:greatCircleGeom(?geom1, ?geom2, <http://www.opengis.net/def/uom/OGC/1.0/kilometer>) AS ?dist) \n"
                + "}ORDER by ?dist";

        List<Literal> results = new ArrayList<>();
        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                Literal result = qs.getLiteral("dist");
                results.add(result);
            }
        }

        List<Literal> expResults = Arrays.asList(ResourceFactory.createTypedLiteral("344.2664230368865e0", XSDDatatype.XSDdouble));

        //System.out.println("Exp: " + expResults);
        //System.out.println("Res: " + results);
        assertEquals(expResults, results);
    }

}
