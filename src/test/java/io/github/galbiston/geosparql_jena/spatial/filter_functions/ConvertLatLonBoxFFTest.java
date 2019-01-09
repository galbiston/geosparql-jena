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

import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
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
     * Test of toWKT method, of class ConvertLatLonBoxFF.
     */
    @Test
    public void testToWKT() {
        System.out.println("toWKT");
        float latMin = 0.0F;
        float lonMin = 0.0F;
        float latMax = 10.0F;
        float lonMax = 10.0F;
        String expResult = "<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)";
        String result = ConvertLatLonBoxFF.toWKT(latMin, lonMin, latMax, lonMax);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toLiteral method, of class ConvertLatLonBoxFF.
     */
    @Test
    public void testToLiteral() {
        System.out.println("toLiteral");
        float latMin = 0.0F;
        float lonMin = 0.0F;
        float latMax = 10.0F;
        float lonMax = 10.0F;
        Literal expResult = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)", WKTDatatype.INSTANCE);
        Literal result = ConvertLatLonBoxFF.toLiteral(latMin, lonMin, latMax, lonMax);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of convert method, of class ConvertLatLonBoxFF.
     */
    @Test
    public void testConvert() {
        System.out.println("convert");
        Node n1 = NodeValue.makeFloat(0.0f).asNode();
        Node n2 = NodeValue.makeFloat(0.0f).asNode();
        Node n3 = NodeValue.makeFloat(10.0f).asNode();
        Node n4 = NodeValue.makeFloat(10.0f).asNode();
        Node expResult = NodeFactory.createLiteral("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)", WKTDatatype.INSTANCE);
        Node result = ConvertLatLonBoxFF.convert(n1, n2, n3, n4);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class ConvertLatLonBoxFF.
     */
    @Test
    public void testExec() {
        System.out.println("exec");
        NodeValue v1 = NodeValue.makeFloat(0.0f);
        NodeValue v2 = NodeValue.makeFloat(0.0f);
        NodeValue v3 = NodeValue.makeFloat(10.0f);
        NodeValue v4 = NodeValue.makeFloat(10.0f);
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)", WKTDatatype.INSTANCE);
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
        NodeValue v2 = NodeValue.makeFloat(0.0f);
        NodeValue v3 = NodeValue.makeFloat(10.0f);
        NodeValue v4 = NodeValue.makeFloat(10.0f);
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)", WKTDatatype.INSTANCE);
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
        NodeValue v2 = NodeValue.makeString("0.0");
        NodeValue v3 = NodeValue.makeFloat(10.0f);
        NodeValue v4 = NodeValue.makeFloat(10.0f);
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)", WKTDatatype.INSTANCE);
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
        NodeValue v2 = NodeValue.makeFloat(0.0f);
        NodeValue v3 = NodeValue.makeString("10.0");
        NodeValue v4 = NodeValue.makeFloat(10.0f);
        ConvertLatLonBoxFF instance = new ConvertLatLonBoxFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)", WKTDatatype.INSTANCE);
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
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v1, v2, v3, v4);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }
}