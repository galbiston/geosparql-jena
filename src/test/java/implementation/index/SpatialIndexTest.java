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
        SpatialIndex.setActive(true);
        String datatypeURI = WKTDatatype.URI;

        try {
            SpatialIndex.insert(BOX_A, datatypeURI);
            SpatialIndex.insert(BOX_B, datatypeURI);
            SpatialIndex.insert(BOX_C, datatypeURI);
        } catch (MismatchedDimensionException ex) {
            ex.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        SpatialIndex.setActive(false);
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

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
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

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
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

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
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

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

}
