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
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
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
public class GenericCardinalGeomPropertyFunctionTest {

    public GenericCardinalGeomPropertyFunctionTest() {
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
     * Test of testRelation method, of class
     * GenericCardinalGeomPropertyFunction.
     */
    @Test
    public void testTestRelation() {
        System.out.println("testRelation");
        GeometryWrapper geometryWrapper = null;
        GeometryWrapper targetGeometryWrapper = null;
        NorthGeomPF instance = new NorthGeomPF();
        boolean expResult = false;
        boolean result = instance.testRelation(geometryWrapper, targetGeometryWrapper);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of search method, of class GenericCardinalGeomPropertyFunction.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
        Binding binding = null;
        ExecutionContext execCxt = null;
        Node subject = null;
        int limit = 0;
        NorthGeomPF instance = new NorthGeomPF();
        QueryIterator expResult = null;
        QueryIterator result = instance.search(binding, execCxt, subject, limit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
