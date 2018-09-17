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
package geosparql_jena.geof.nontopological.filter_functions;

import geosparql_jena.implementation.datatype.WKTDatatype;
import geosparql_jena.implementation.vocabulary.Unit_URI;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.expr.NodeValue;
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
public class DistanceFFTest {

    public DistanceFFTest() {
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
     * Test of exec method, of class DistanceFF.
     */
    @Test
    public void testExec_metres() {
        System.out.println("exec_metres");
        NodeValue v1 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(60 60)", WKTDatatype.INSTANCE);
        NodeValue v2 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(90 60)", WKTDatatype.INSTANCE);
        NodeValue v3 = NodeValue.makeNode(NodeFactory.createURI(Unit_URI.METRE_URL));
        DistanceFF instance = new DistanceFF();
        double expResult = 30;
        double result = instance.exec(v1, v2, v3).getDouble();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0);
    }

    /**
     * Test of exec method, of class DistanceFF.
     */
    @Test
    public void testExec_radians() {
        System.out.println("exec_radians");
        NodeValue v1 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(60 60)", WKTDatatype.INSTANCE);
        NodeValue v2 = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(90 60)", WKTDatatype.INSTANCE);
        NodeValue v3 = NodeValue.makeNode(NodeFactory.createURI(Unit_URI.RADIAN_URL));
        DistanceFF instance = new DistanceFF();
        double expResult = 7.2449E-6;
        double result = instance.exec(v1, v2, v3).getDouble();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 1E-10);
    }

}
