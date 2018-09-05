/*
 * Copyright 2018 Greg Albiston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geof.nontopological.filter_functions;

import implementation.GeoSPARQLSupport;
import implementation.datatype.WKTDatatype;
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
public class BoundaryFFTest {

    public BoundaryFFTest() {
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
     * Test of exec method, of class BoundaryFF.
     */
    @Test
    public void testExec() {
        System.out.println("exec");
        GeoSPARQLSupport.loadFunctionsNoIndex();
        NodeValue v = NodeValue.makeNode("POLYGON((-77.089005 38.913574, -77.029953 38.913574, -77.029953 38.886321, -77.089005 38.886321, -77.089005 38.913574))", WKTDatatype.INSTANCE);
        BoundaryFF instance = new BoundaryFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING(-77.089005 38.913574, -77.029953 38.913574, -77.029953 38.886321, -77.089005 38.886321, -77.089005 38.913574)", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class BoundaryFF.
     */
    @Test
    public void testExec2() {
        System.out.println("exec2");
        GeoSPARQLSupport.loadFunctionsNoIndex();
        NodeValue v = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((38.913574 -77.089005, 38.913574 -77.029953, 38.886321 -77.029953, 38.886321 -77.089005, 38.913574 -77.089005))", WKTDatatype.INSTANCE);
        BoundaryFF instance = new BoundaryFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> LINESTRING(38.913574 -77.089005, 38.913574 -77.029953, 38.886321 -77.029953, 38.886321 -77.089005, 38.913574 -77.089005)", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
