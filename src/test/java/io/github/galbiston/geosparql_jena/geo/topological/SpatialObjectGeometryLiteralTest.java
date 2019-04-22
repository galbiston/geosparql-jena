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
package io.github.galbiston.geosparql_jena.geo.topological;

import static io.github.galbiston.geosparql_jena.geo.topological.QueryRewriteTestData.FEATURE_B;
import static io.github.galbiston.geosparql_jena.geo.topological.QueryRewriteTestData.GEOMETRY_B;
import static io.github.galbiston.geosparql_jena.geo.topological.QueryRewriteTestData.GEO_FEATURE_LITERAL;
import static io.github.galbiston.geosparql_jena.geo.topological.QueryRewriteTestData.GEO_FEATURE_Y;
import static io.github.galbiston.geosparql_jena.geo.topological.QueryRewriteTestData.LITERAL_B;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
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
public class SpatialObjectGeometryLiteralTest {

    private static Model MODEL;

    public SpatialObjectGeometryLiteralTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        MODEL = QueryRewriteTestData.createTestData();
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
     * Test of retrieve method, of class SpatialObjectGeometryLiteral.
     */
    @Test
    public void testRetrieve() {
        System.out.println("retrieve");

        Graph graph = MODEL.getGraph();
        Node targetSpatialObject = null;
        SpatialObjectGeometryLiteral instance = SpatialObjectGeometryLiteral.retrieve(graph, targetSpatialObject);

        boolean expResult = false;
        boolean result = instance.isValid();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieve method, of class SpatialObjectGeometryLiteral.
     */
    @Test
    public void testRetrieveGeometryLiteral_geometry() {
        System.out.println("retrieve_geometry");

        Graph graph = MODEL.getGraph();
        Resource targetSpatialObject = GEOMETRY_B;
        SpatialObjectGeometryLiteral expResult = new SpatialObjectGeometryLiteral(GEOMETRY_B.asNode(), LITERAL_B.asNode());
        SpatialObjectGeometryLiteral result = SpatialObjectGeometryLiteral.retrieve(graph, targetSpatialObject.asNode());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieve method, of class SpatialObjectGeometryLiteral.
     */
    @Test
    public void testRetrieveGeometryLiteral_feature() {
        System.out.println("retrieve_feature");

        Resource targetSpatialObject = FEATURE_B;
        SpatialObjectGeometryLiteral expResult = new SpatialObjectGeometryLiteral(FEATURE_B.asNode(), LITERAL_B.asNode());
        SpatialObjectGeometryLiteral result = SpatialObjectGeometryLiteral.retrieve(MODEL.getGraph(), targetSpatialObject.asNode());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieve method, of class SpatialObjectGeometryLiteral.
     */
    @Test
    public void testRetrieveGeometryLiteral_missing_property() {
        System.out.println("retrieve_missing_property");

        Resource targetSpatialObject = ResourceFactory.createResource("http://example.org#GeometryE");

        SpatialObjectGeometryLiteral instance = SpatialObjectGeometryLiteral.retrieve(MODEL.getGraph(), targetSpatialObject.asNode());

        boolean expResult = false;
        boolean result = instance.isValid();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieve method, of class SpatialObjectGeometryLiteral.
     */
    @Test
    public void testRetrieveGeometryLiteral_not_feature_geometry() {
        System.out.println("retrieve_not_feature_geometry");

        Resource targetSpatialObject = ResourceFactory.createResource("http://example.org#X");

        SpatialObjectGeometryLiteral instance = SpatialObjectGeometryLiteral.retrieve(MODEL.getGraph(), targetSpatialObject.asNode());

        boolean expResult = false;
        boolean result = instance.isValid();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieve method, of class SpatialObjectGeometryLiteral.
     */
    @Test
    public void testRetrieveGeometryLiteral_feature_lat_lon() {
        System.out.println("retrieve_feature");

        Resource targetSpatialObject = GEO_FEATURE_Y;
        SpatialObjectGeometryLiteral expResult = new SpatialObjectGeometryLiteral(GEO_FEATURE_Y.asNode(), GEO_FEATURE_LITERAL.asNode());
        SpatialObjectGeometryLiteral result = SpatialObjectGeometryLiteral.retrieve(MODEL.getGraph(), targetSpatialObject.asNode());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
