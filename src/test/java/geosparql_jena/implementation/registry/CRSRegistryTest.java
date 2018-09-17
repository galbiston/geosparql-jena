/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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
package geosparql_jena.implementation.registry;

import geosparql_jena.implementation.vocabulary.SRS_URI;
import org.apache.sis.referencing.CRS;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class CRSRegistryTest {

    public CRSRegistryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        CRSRegistry.setupDefaultCRS();
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
     * Test of getCRS method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testGetCRS() throws FactoryException {
        System.out.println("getCRS");
        String srsURI = "http://www.opengis.net/def/crs/EPSG/0/4326";
        CoordinateReferenceSystem expResult = CRS.forCode(srsURI);
        CoordinateReferenceSystem result = CRSRegistry.getCRS(srsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCRS method, of class CRSRegistry.
     */
    @Test
    public void testGetDefaultWKTCRS() {
        try {
            System.out.println("getDefaultWKTCRS");
            String srsURI = SRS_URI.DEFAULT_WKT_CRS84;

            String default_CRS_WKT = "GeodeticCRS[\"WGS 84\",\n"
                    + "  Datum[\"World Geodetic System 1984\",\n"
                    + "    Ellipsoid[\"WGS 84\", 6378137.0, 298.257223563]],\n"
                    + "  CS[ellipsoidal, 2],\n"
                    + "    Axis[\"Geodetic longitude (Lon)\", east],\n"
                    + "    Axis[\"Geodetic latitude (Lat)\", north],\n"
                    + "    Unit[\"degree\", 0.017453292519943295],\n"
                    + "  Scope[\"Horizontal component of 3D system. Used by the GPS satellite navigation system and for NATO military geodetic surveying.\"],\n"
                    + "  Area[\"World.\"],\n"
                    + "  BBox[-90.00, -180.00, 90.00, 180.00],\n"
                    + "  Id[\"CRS\", 84, Citation[\"OGC:WMS\"], URI[\"urn:ogc:def:crs:OGC:1.3:CRS84\"]]]";

            CoordinateReferenceSystem expResult = CRS.fromWKT(default_CRS_WKT);
            CoordinateReferenceSystem result = CRSRegistry.getCRS(srsURI);

            //System.out.println("Exp: " + expResult);
            //System.out.println("Res: " + result);
            assertEquals(expResult.toWKT(), result.toWKT());
        } catch (FactoryException ex) {
            System.out.println("Default WKT CRS error: " + ex.getMessage());
        }
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testCheckAxisXY_WGS84() throws FactoryException {
        System.out.println("checkAxisXY_WGS84");
        CoordinateReferenceSystem crs = CRS.forCode(SRS_URI.WGS84_CRS);
        Boolean expResult = false;
        Boolean result = CRSRegistry.checkAxisXY(crs);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     */
    @Test
    public void testCheckAxisXY_WGS84URI() {
        System.out.println("checkAxisXY_WGS84URI");
        String srsURI = SRS_URI.WGS84_CRS;
        Boolean expResult = false;
        Boolean result = CRSRegistry.getAxisXY(srsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testCheckAxisXY_CRS84() throws FactoryException {
        System.out.println("checkAxisXY_CRS84");
        CoordinateReferenceSystem crs = CRS.forCode("CRS:84");
        Boolean expResult = true;
        Boolean result = CRSRegistry.checkAxisXY(crs);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     */
    @Test
    public void testCheckAxisXY_CRS84URI() {
        System.out.println("checkAxisXY_CRS84URI");
        String srsURI = SRS_URI.DEFAULT_WKT_CRS84;
        Boolean expResult = true;
        Boolean result = CRSRegistry.getAxisXY(srsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testCheckAxisXY_OSGB36() throws FactoryException {
        System.out.println("checkAxisXY_OSGB36");
        CoordinateReferenceSystem crs = CRS.forCode(SRS_URI.OSGB36_CRS);
        Boolean expResult = true;
        Boolean result = CRSRegistry.checkAxisXY(crs);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkAxisXY method, of class CRSRegistry.
     *
     */
    @Test
    public void testCheckAxisXY_OSGB36URI() {
        System.out.println("checkAxisXY_OSGB36URI");
        String srsURI = SRS_URI.OSGB36_CRS;
        Boolean expResult = true;
        Boolean result = CRSRegistry.getAxisXY(srsURI);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
