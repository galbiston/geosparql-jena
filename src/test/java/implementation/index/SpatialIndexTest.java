/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeoSPARQLSupport;
import implementation.datatype.WKTDatatype;
import static implementation.index.CollisionResult.*;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Gerg
 */
public class SpatialIndexTest {

    public SpatialIndexTest() {
    }

    private static final String BOX_A = "POLYGON(0.0 0.0, 0.0 10.0, 10.0 10.0, 10.0 0.0, 0.0 0.0)";
    private static final String BOX_B = "POLYGON(5.0 5.0, 5.0 15.0, 15.0 15.0, 15.0 5.0, 5.0 5.0)";
    private static final String BOX_C = "POLYGON(20.0 20.0, 20.0 30.0, 30.0 30.0, 30.0 20.0, 20.0 20.0)";

    @BeforeClass
    public static void setUpClass() {
        GeoSPARQLSupport.loadFunctionsMemoryIndex();
        String datatypeURI = WKTDatatype.URI;

        try {
            SpatialIndex.insert(BOX_A, datatypeURI);
            SpatialIndex.insert(BOX_B, datatypeURI);
            SpatialIndex.insert(BOX_C, datatypeURI);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            ex.printStackTrace();
        }
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
     * Test of checkCollisionNotDisjoint method, of class SpatialIndex.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCheckCollision_collision_notDisjoint() throws Exception {
        System.out.println("checkCollisionNotDisjoint");
        String sourceGeometryLiteral = BOX_A;
        String targetGeometryLiteral = BOX_B;
        Boolean isDisjoint = false;
        CollisionResult expResult = CHECK_RELATION;
        CollisionResult result = SpatialIndex.checkCollision(sourceGeometryLiteral, targetGeometryLiteral, isDisjoint);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkCollisionDisjoint method, of class SpatialIndex.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCheckCollision_collision_Disjoint() throws Exception {
        System.out.println("checkCollisionDisjoint");
        String sourceGeometryLiteral = BOX_A;
        String targetGeometryLiteral = BOX_B;
        Boolean isDisjoint = true;
        CollisionResult expResult = CHECK_RELATION;
        CollisionResult result = SpatialIndex.checkCollision(sourceGeometryLiteral, targetGeometryLiteral, isDisjoint);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkNotCollisionNotDisjoint method, of class SpatialIndex.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCheckCollision_notCollision_notDisjoint() throws Exception {
        System.out.println("checkNotCollisionNotDisjoint");
        String sourceGeometryLiteral = BOX_A;
        String targetGeometryLiteral = BOX_C;
        Boolean isDisjoint = false;
        CollisionResult expResult = FALSE_RELATION;
        CollisionResult result = SpatialIndex.checkCollision(sourceGeometryLiteral, targetGeometryLiteral, isDisjoint);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkNotCollisionDisjoint method, of class SpatialIndex.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCheckCollision_notCollision_Disjoint() throws Exception {
        System.out.println("checkNotCollisionDisjoint");
        String sourceGeometryLiteral = BOX_A;
        String targetGeometryLiteral = BOX_C;
        Boolean isDisjoint = true;
        CollisionResult expResult = TRUE_RELATION;
        CollisionResult result = SpatialIndex.checkCollision(sourceGeometryLiteral, targetGeometryLiteral, isDisjoint);
        assertEquals(expResult, result);
    }

}
