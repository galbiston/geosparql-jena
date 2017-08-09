/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.datatype.WKTDatatype;
import implementation.support.GeoSerialisationEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Greg Albiston
 */
public class GeometryUtilTest {

    public GeometryUtilTest() {
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
     * Test of pointsToLineString method, of class GeometryUtil.
     */
    @Test
    public void testPointsToLineString_List() {
        System.out.println("pointsToLineString");

        List<Literal> points = new ArrayList<>();
        points.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT (11.0 12.0)", WKTDatatype.THE_WKT_DATATYPE));
        points.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT (8.0 5.0)", WKTDatatype.THE_WKT_DATATYPE));
        points.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT (3.0 1.0)", WKTDatatype.THE_WKT_DATATYPE));

        Literal expResult = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (11 12, 8 5, 3 1)", WKTDatatype.THE_WKT_DATATYPE);
        Literal result = GeometryUtil.pointsToLineString(points);

        assertEquals(expResult, result);
    }

    /**
     * Test of pointsToLineString method, of class GeometryUtil.
     */
    @Test
    public void testPointsToLineString_List_GeoSerialisationEnum() {
        System.out.println("pointsToLineString");

        GeoSerialisationEnum geoSerialisationEnum = GeoSerialisationEnum.WKT;
        List<Literal> points = new ArrayList<>();
        points.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT (11.0 12.0)", WKTDatatype.THE_WKT_DATATYPE));
        points.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT (8.0 5.0)", WKTDatatype.THE_WKT_DATATYPE));
        points.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT (3.0 1.0)", WKTDatatype.THE_WKT_DATATYPE));

        Literal expResult = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (11 12, 8 5, 3 1)", WKTDatatype.THE_WKT_DATATYPE);
        Literal result = GeometryUtil.pointsToLineString(points, geoSerialisationEnum);

        assertEquals(expResult, result);
    }

    /**
     * Test of splitLineString method, of class GeometryUtil.
     */
    @Test
    public void testSplitLineString_Literal_GeoSerialisationEnum() {
        System.out.println("splitLineString");

        Literal lineString = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (11 12, 8 5, 3 1, 4 6)", WKTDatatype.THE_WKT_DATATYPE);
        GeoSerialisationEnum geoSerialisationEnum = GeoSerialisationEnum.WKT;

        List<Literal> expResult = new ArrayList<>();
        expResult.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (11 12, 8 5)", WKTDatatype.THE_WKT_DATATYPE));
        expResult.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (8 5, 3 1)", WKTDatatype.THE_WKT_DATATYPE));
        expResult.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (3 1, 4 6)", WKTDatatype.THE_WKT_DATATYPE));

        List<Literal> result = GeometryUtil.splitLineString(lineString, geoSerialisationEnum);

        assertEquals(expResult, result);
    }

    /**
     * Test of splitLineString method, of class GeometryUtil.
     */
    @Test
    public void testSplitLineString_Literal() {
        System.out.println("splitLineString");

        Literal lineString = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (11 12, 8 5, 3 1, 4 6)", WKTDatatype.THE_WKT_DATATYPE);

        List<Literal> expResult = new ArrayList<>();
        expResult.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (11 12, 8 5)", WKTDatatype.THE_WKT_DATATYPE));
        expResult.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (8 5, 3 1)", WKTDatatype.THE_WKT_DATATYPE));
        expResult.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (3 1, 4 6)", WKTDatatype.THE_WKT_DATATYPE));

        List<Literal> result = GeometryUtil.splitLineString(lineString);

        System.out.println("Exp: " + expResult);
        System.out.println("Res: " + result);

        assertEquals(expResult, result);
    }

    /**
     * Test of selectNearest method, of class GeometryUtil.
     */
    @Test
    public void testSelectNearest() {
        System.out.println("selectNearest");

        Literal targetGeometry = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> POINT (11.2 12.1)", WKTDatatype.THE_WKT_DATATYPE);

        Collection<Literal> candidateGeometries = new ArrayList<>();
        candidateGeometries.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (11 12, 8 5)", WKTDatatype.THE_WKT_DATATYPE));
        candidateGeometries.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (8 5, 3 1)", WKTDatatype.THE_WKT_DATATYPE));
        candidateGeometries.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (3 1, 4 6)", WKTDatatype.THE_WKT_DATATYPE));

        Literal expResult = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING (11 12, 8 5)", WKTDatatype.THE_WKT_DATATYPE);
        Literal result = GeometryUtil.selectNearest(targetGeometry, candidateGeometries);

        assertEquals(expResult, result);
    }

}
