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
package geosparql_jena.geo.topological;

import geosparql_jena.geo.topological.property_functions.simple_features.SfContainsPF;
import geosparql_jena.configuration.GeoSPARQLConfig;
import geosparql_jena.implementation.datatype.WKTDatatype;
import geosparql_jena.implementation.index.IndexConfiguration.IndexOption;
import geosparql_jena.implementation.vocabulary.Geo;
import org.apache.jena.graph.BlankNodeId;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
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
public class GenericPropertyFunctionTest {

    public GenericPropertyFunctionTest() {
    }

    private static final Model MODEL = ModelFactory.createDefaultModel();
    private static final Resource GEOMETRY_A = ResourceFactory.createResource("http://example.org#GeometryA");
    private static final Resource GEOMETRY_B = ResourceFactory.createResource("http://example.org#GeometryB");
    private static final Resource GEOMETRY_C_BLANK = ResourceFactory.createResource();
    private static final Resource GEOMETRY_D = ResourceFactory.createResource("http://example.org#GeometryD");
    private static final Resource GEOMETRY_E = ResourceFactory.createResource("http://example.org#GeometryE");
    private static final Resource GEOMETRY_F = ResourceFactory.createResource("http://example.org#GeometryF");
    private static final Resource FEATURE_A = ResourceFactory.createResource("http://example.org#FeatureA");
    private static final Resource FEATURE_B = ResourceFactory.createResource("http://example.org#FeatureB");
    private static final Resource FEATURE_C = ResourceFactory.createResource("http://example.org#FeatureC");
    private static final Resource FEATURE_D = ResourceFactory.createResource("http://example.org#FeatureD");

    private static final Literal LITERAL_B = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(1 1)", WKTDatatype.INSTANCE);

    @BeforeClass
    public static void setUpClass() {

        //Geometry
        MODEL.add(GEOMETRY_A, Geo.HAS_SERIALIZATION_PROP, ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))", WKTDatatype.INSTANCE));
        MODEL.add(GEOMETRY_B, Geo.HAS_SERIALIZATION_PROP, LITERAL_B);
        MODEL.add(GEOMETRY_C_BLANK, Geo.HAS_SERIALIZATION_PROP, ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(5 5)", WKTDatatype.INSTANCE));
        MODEL.add(GEOMETRY_D, Geo.HAS_SERIALIZATION_PROP, ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(11 11)", WKTDatatype.INSTANCE));
        MODEL.add(GEOMETRY_A, RDF.type, Geo.GEOMETRY_RES);
        MODEL.add(GEOMETRY_B, RDF.type, Geo.GEOMETRY_RES);
        MODEL.add(GEOMETRY_C_BLANK, RDF.type, Geo.GEOMETRY_RES);
        MODEL.add(GEOMETRY_D, RDF.type, Geo.GEOMETRY_RES);
        MODEL.add(GEOMETRY_E, RDF.type, Geo.GEOMETRY_RES);
        MODEL.add(GEOMETRY_F, RDF.type, Geo.GEOMETRY_RES);

        //Feature
        MODEL.add(FEATURE_A, Geo.HAS_DEFAULT_GEOMETRY_PROP, GEOMETRY_A);
        MODEL.add(FEATURE_B, Geo.HAS_DEFAULT_GEOMETRY_PROP, GEOMETRY_B);
        MODEL.add(FEATURE_C, Geo.HAS_DEFAULT_GEOMETRY_PROP, GEOMETRY_C_BLANK);
        MODEL.add(FEATURE_D, Geo.HAS_DEFAULT_GEOMETRY_PROP, GEOMETRY_D);
        MODEL.add(FEATURE_A, RDF.type, Geo.FEATURE_RES);
        MODEL.add(FEATURE_B, RDF.type, Geo.FEATURE_RES);
        MODEL.add(FEATURE_C, RDF.type, Geo.FEATURE_RES);
        MODEL.add(FEATURE_D, RDF.type, Geo.FEATURE_RES);

        //Contains
        MODEL.add(GEOMETRY_A, Geo.SF_CONTAINS_PROP, GEOMETRY_F);

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
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_geometry() {
        System.out.println("queryRewrite_geometry_geometry");
        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = GEOMETRY_B.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = true;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_geometry_blank() {
        System.out.println("queryRewrite_geometry_geometry_blank");
        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = GEOMETRY_C_BLANK.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = true;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_blank() {
        System.out.println("queryRewrite_blank");

        Graph graph = MODEL.getGraph();

        Boolean expResult = true;
        BlankNodeId id = GEOMETRY_C_BLANK.asNode().getBlankNodeId();
        Node node = NodeFactory.createBlankNode(id);

        Boolean result = graph.contains(node, RDF.type.asNode(), Geo.GEOMETRY_NODE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_geometry_disabled() {
        System.out.println("queryRewrite_geometry_geometry_disabled");
        GeoSPARQLConfig.setup(IndexOption.MEMORY, Boolean.FALSE);
        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = FEATURE_B.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = false;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        GeoSPARQLConfig.setup(IndexOption.MEMORY, Boolean.TRUE);
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_feature_geometry() {
        System.out.println("queryRewrite_feature_geometry");
        Graph graph = MODEL.getGraph();
        Node subject = FEATURE_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = GEOMETRY_B.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = true;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_feature_feature() {
        System.out.println("queryRewrite_feature_feature");
        Graph graph = MODEL.getGraph();
        Node subject = FEATURE_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = FEATURE_B.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = true;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_feature() {
        System.out.println("queryRewrite_geometry_feature");
        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = FEATURE_B.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = true;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_feature_disabled() {
        System.out.println("queryRewrite_geometry_feature_disabled");
        GeoSPARQLConfig.setup(IndexOption.MEMORY, Boolean.FALSE);
        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = FEATURE_B.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = false;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        GeoSPARQLConfig.setup(IndexOption.MEMORY, Boolean.TRUE);
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_geometry_false() {
        System.out.println("queryRewrite_geometry_geometry_false");
        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = GEOMETRY_D.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = false;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_feature_geometry_false() {
        System.out.println("queryRewrite_feature_geometry_false");
        Graph graph = MODEL.getGraph();
        Node subject = FEATURE_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = GEOMETRY_D.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = false;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_feature_feature_false() {
        System.out.println("queryRewrite_feature_feature_false");
        Graph graph = MODEL.getGraph();
        Node subject = FEATURE_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = FEATURE_D.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = false;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_feature_false() {
        System.out.println("queryRewrite_geometry_feature_false");
        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = FEATURE_D.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = false;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieveGeometryLiteral method, of class GenericPropertyFunction.
     */
    @Test
    public void testRetrieveGeometryLiteral_geometry() {
        System.out.println("retrieveGeometryLiteral_geometry");

        Resource targetSpatialObject = GEOMETRY_B;
        SpatialObjectGeometryLiteral expResult = new SpatialObjectGeometryLiteral(GEOMETRY_B.asNode(), LITERAL_B.asNode());
        SpatialObjectGeometryLiteral result = GenericPropertyFunction.retrieveGeometryLiteral(MODEL.getGraph(), targetSpatialObject.asNode());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieveGeometryLiteral method, of class GenericPropertyFunction.
     */
    @Test
    public void testRetrieveGeometryLiteral_feature() {
        System.out.println("retrieveGeometryLiteral_feature");

        Resource targetSpatialObject = FEATURE_B;
        SpatialObjectGeometryLiteral expResult = new SpatialObjectGeometryLiteral(FEATURE_B.asNode(), LITERAL_B.asNode());
        SpatialObjectGeometryLiteral result = GenericPropertyFunction.retrieveGeometryLiteral(MODEL.getGraph(), targetSpatialObject.asNode());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieveGeometryLiteral method, of class GenericPropertyFunction.
     */
    @Test
    public void testRetrieveGeometryLiteral_missing_property() {
        System.out.println("retrieveGeometryLiteral_missing_property");

        Resource targetSpatialObject = ResourceFactory.createResource("http://example.org#GeometryE");
        SpatialObjectGeometryLiteral expResult = null;
        SpatialObjectGeometryLiteral result = GenericPropertyFunction.retrieveGeometryLiteral(MODEL.getGraph(), targetSpatialObject.asNode());

        System.out.println("Exp: " + expResult);
        System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieveGeometryLiteral method, of class GenericPropertyFunction.
     */
    @Test
    public void testRetrieveGeometryLiteral_not_feature_geometry() {
        System.out.println("retrieveGeometryLiteral_not_feature_geometry");

        Resource targetSpatialObject = ResourceFactory.createResource("http://example.org#X");
        SpatialObjectGeometryLiteral expResult = null;
        SpatialObjectGeometryLiteral result = GenericPropertyFunction.retrieveGeometryLiteral(MODEL.getGraph(), targetSpatialObject.asNode());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_geometry_asserted() {
        System.out.println("queryRewrite_geometry__geometry_asserted");

        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = GEOMETRY_F.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = true;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of queryRewrite method, of class GenericPropertyFunction.
     */
    @Test
    public void testQueryRewrite_geometry_geometry_asserted_disabled() {
        System.out.println("queryRewrite_geometry__geometry_asserted_disabled");
        GeoSPARQLConfig.setup(IndexOption.MEMORY, Boolean.FALSE);
        Graph graph = MODEL.getGraph();
        Node subject = GEOMETRY_A.asNode();
        Node predicate = Geo.SF_CONTAINS_NODE;
        Node object = GEOMETRY_F.asNode();
        GenericPropertyFunction instance = new SfContainsPF();
        Boolean expResult = true;
        Boolean result = instance.queryRewrite(graph, subject, predicate, object);

        GeoSPARQLConfig.setup(IndexOption.MEMORY, Boolean.TRUE);
        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
