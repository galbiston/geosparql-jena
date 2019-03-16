/*
 * Copyright 2018 the original author or authors.
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
package io.github.galbiston.geosparql_jena.geof.topological.filter_functions.geometry_property;

import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import org.apache.jena.sparql.expr.NodeValue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 */
public class DimensionFFTest {

    public DimensionFFTest() {
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
     * Test of exec method, of class DimensionFF.
     */
    @Test
    public void testExec_0_Dimension() {
        System.out.println("exec_0_Dimension");
        NodeValue geometryLiteral = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(90 60)", WKTDatatype.INSTANCE);
        DimensionFF instance = new DimensionFF();
        NodeValue expResult = NodeValue.makeNodeInteger(0);
        NodeValue result = instance.exec(geometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class DimensionFF.
     */
    @Test
    public void testExec_1_Dimension() {
        System.out.println("exec_1_Dimension");
        NodeValue geometryLiteral = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING(90 60, 100 70)", WKTDatatype.INSTANCE);
        DimensionFF instance = new DimensionFF();
        NodeValue expResult = NodeValue.makeNodeInteger(1);
        NodeValue result = instance.exec(geometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class DimensionFF.
     */
    @Test
    public void testExec_2_Dimension() {
        System.out.println("exec_2_Dimension");
        NodeValue geometryLiteral = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))", WKTDatatype.INSTANCE);
        DimensionFF instance = new DimensionFF();
        NodeValue expResult = NodeValue.makeNodeInteger(2);
        NodeValue result = instance.exec(geometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
