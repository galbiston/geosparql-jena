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
package io.github.galbiston.geosparql_jena.spatial.property_functions.nearby;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialExtension;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexTestData;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonFF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.SpatialArguments;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.pfunction.PropFuncArg;
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
public class NearbyGeomPFTest {

    public NearbyGeomPFTest() {
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
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test
    public void testExtractObjectArguments_4args() {
        System.out.println("extractObjectArguments_4args");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test
    public void testExtractObjectArguments_3args() {
        System.out.println("extractObjectArguments_3args");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = -1;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI));
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test
    public void testExtractObjectArguments_2args() {
        System.out.println("extractObjectArguments_2args");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5;
        String unitsURI = Unit_URI.KILOMETER_URL;
        int limit = -1;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_1args_fail() {
        System.out.println("extractObjectArguments_1args_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5;
        String unitsURI = Unit_URI.KILOMETER_URL;
        int limit = -1;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_5args_fail() {
        System.out.println("extractObjectArguments_5args_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode(), NodeValue.makeBoolean(false).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_pos0_fail() {
        System.out.println("extractObjectArguments_4args_pos0_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeString("0").asNode(), NodeValue.makeFloat(lon).asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_pos1_fail() {
        System.out.println("extractObjectArguments_4args_pos1_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeFloat(lat).asNode(), NodeValue.makeString("1").asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_pos2_fail() {
        System.out.println("extractObjectArguments_4args_pos2_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeString("5000").asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_pos3_fail() {
        System.out.println("extractObjectArguments_4args_pos3_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode(), NodeValue.makeString(unitsURI).asNode(), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, envelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of testRelation method, of class NearbyGeomPF.
     */
    @Test
    public void testTestRelation() {
        System.out.println("testRelation");

        NearbyGeomPF instance = new NearbyGeomPF();

        //Property Function
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        //Geometry and Envelope parameters
        float lat = 0;
        float lon = 1;
        float radius = 5;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        Literal targetGeometry = ConvertLatLonFF.toLiteral(lat + 0.0001f, lon);

        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        //Function arguments
        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        GeometryWrapper targetGeometryWrapper = GeometryWrapper.extract(targetGeometry);

        //Test arguments
        instance.extractObjectArguments(predicate, object);
        boolean expResult = true;
        boolean result = instance.testRelation(geometryWrapper, targetGeometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of testRelation method, of class NearbyGeomPF.
     */
    @Test
    public void testTestRelation_fail() {
        System.out.println("testRelation_fail");

        NearbyGeomPF instance = new NearbyGeomPF();

        //Property Function
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        //Geometry and Envelope parameters
        float lat = 0;
        float lon = 1;
        float radius = 5;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        Literal targetGeometry = ConvertLatLonFF.toLiteral(lat + 10f, lon);

        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        //Function arguments
        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        GeometryWrapper targetGeometryWrapper = GeometryWrapper.extract(targetGeometry);

        //Test arguments
        instance.extractObjectArguments(predicate, object);
        boolean expResult = false;
        boolean result = instance.testRelation(geometryWrapper, targetGeometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of testSearchEnvelope method, of class NearbyGeomPF.
     */
    @Test
    public void testCheckSearchEnvelope() {
        System.out.println("checkSearchEnvelope");
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();

        //Search Envelope
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PARIS_GEOMETRY;
        float radius = 200;
        String unitsURI = Unit_URI.KILOMETER_URL;
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        //Function Test
        NearbyGeomPF instance = new NearbyGeomPF();
        HashSet<Resource> expResult = new HashSet<>(Arrays.asList(SpatialIndexTestData.LONDON_FEATURE));
        HashSet<Resource> result = instance.checkSearchEnvelope(spatialIndex, envelope);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of testSearchEnvelope method, of class NearbyGeomPF.
     */
    @Test
    public void testCheckSearchEnvelope_empty() {
        System.out.println("checkSearchEnvelope_empty");
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();

        //Search Envelope
        GeometryWrapper geometryWrapper = SpatialIndexTestData.PARIS_GEOMETRY;
        float radius = 2;
        String unitsURI = Unit_URI.KILOMETER_URL;
        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        //Function Test
        NearbyGeomPF instance = new NearbyGeomPF();
        HashSet<Resource> expResult = new HashSet<>();
        HashSet<Resource> result = instance.checkSearchEnvelope(spatialIndex, envelope);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
