/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
