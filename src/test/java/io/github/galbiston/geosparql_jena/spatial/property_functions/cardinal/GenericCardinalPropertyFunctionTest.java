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
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialExtension;
import io.github.galbiston.geosparql_jena.spatial.ConvertLatLon;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexTestData;
import io.github.galbiston.geosparql_jena.spatial.property_functions.SpatialArguments;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.pfunction.PropFuncArg;
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
public class GenericCardinalPropertyFunctionTest {

    public GenericCardinalPropertyFunctionTest() {
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
     * Test of extractObjectArguments method, of class
     * GenericCardinalPropertyFunction.
     */
    @Test
    public void testExtractObjectArguments_3args() {
        System.out.println("extractObjectArguments_3args");

        Node predicate = NodeFactory.createURI(SpatialExtension.NORTH_PROP);

        float lat = 0;
        float lon = 1;
        int limit = 10;

        Literal geometry = ConvertLatLon.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeFloat(lat).asNode(), NodeValue.makeFloat(lon).asNode(), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);

        NorthPF instance = new NorthPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_CRS_INFO);
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object, SpatialIndexTestData.WGS_84_CRS_INFO);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class
     * GenericCardinalPropertyFunction.
     */
    @Test
    public void testExtractObjectArguments_2args() {
        System.out.println("extractObjectArguments_2args");

        Node predicate = NodeFactory.createURI(SpatialExtension.NORTH_PROP);

        float lat = 0;
        float lon = 1;
        int limit = -1;

        Literal geometry = ConvertLatLon.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeFloat(lat).asNode(), NodeValue.makeFloat(lon).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);

        NorthPF instance = new NorthPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_CRS_INFO);
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object, SpatialIndexTestData.WGS_84_CRS_INFO);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class
     * GenericCardinalPropertyFunction.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_1args_fail() {
        System.out.println("extractObjectArguments_1args_fail");

        Node predicate = NodeFactory.createURI(SpatialExtension.NORTH_PROP);

        float lat = 0;
        float lon = 1;
        int limit = -1;

        Literal geometry = ConvertLatLon.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeFloat(lat).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);

        NorthPF instance = new NorthPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_CRS_INFO);
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object, SpatialIndexTestData.WGS_84_CRS_INFO);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class
     * GenericCardinalPropertyFunction.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_fail() {
        System.out.println("extractObjectArguments_4args_fail");

        Node predicate = NodeFactory.createURI(SpatialExtension.NORTH_PROP);

        float lat = 0;
        float lon = 1;
        int limit = 10;

        Literal geometry = ConvertLatLon.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeFloat(lat).asNode(), NodeValue.makeFloat(lon).asNode(), NodeValue.makeInteger(limit).asNode(), NodeValue.makeBoolean(false).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);

        NorthPF instance = new NorthPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_CRS_INFO);
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object, SpatialIndexTestData.WGS_84_CRS_INFO);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class
     * GenericCardinalPropertyFunction.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_3args_pos0_fail() {
        System.out.println("extractObjectArguments_3args_pos0_fail");

        Node predicate = NodeFactory.createURI(SpatialExtension.NORTH_PROP);

        float lat = 0;
        float lon = 1;
        int limit = 10;

        Literal geometry = ConvertLatLon.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeString("0").asNode(), NodeValue.makeFloat(lon).asNode(), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);

        NorthPF instance = new NorthPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_CRS_INFO);
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object, SpatialIndexTestData.WGS_84_CRS_INFO);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class
     * GenericCardinalPropertyFunction.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_3args_pos1_fail() {
        System.out.println("extractObjectArguments_3args_pos1_fail");

        Node predicate = NodeFactory.createURI(SpatialExtension.NORTH_PROP);

        float lat = 0;
        float lon = 1;
        int limit = 10;

        Literal geometry = ConvertLatLon.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeFloat(lat).asNode(), NodeValue.makeString("1").asNode(), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);

        NorthPF instance = new NorthPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_CRS_INFO);
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object, SpatialIndexTestData.WGS_84_CRS_INFO);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class
     * GenericCardinalPropertyFunction.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_3args_pos2_fail() {
        System.out.println("extractObjectArguments_3args_pos2_fail");

        Node predicate = NodeFactory.createURI(SpatialExtension.NORTH_PROP);

        float lat = 0;
        float lon = 1;
        int limit = 10;

        Literal geometry = ConvertLatLon.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeFloat(lat).asNode(), NodeValue.makeFloat(lon).asNode(), NodeValue.makeString("10").asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);

        NorthPF instance = new NorthPF();
        SearchEnvelope searchEnvelope = instance.buildSearchEnvelope(geometryWrapper, SpatialIndexTestData.WGS_84_CRS_INFO);
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object, SpatialIndexTestData.WGS_84_CRS_INFO);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
