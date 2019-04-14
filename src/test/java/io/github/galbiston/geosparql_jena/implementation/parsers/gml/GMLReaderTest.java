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
package io.github.galbiston.geosparql_jena.implementation.parsers.gml;

import io.github.galbiston.geosparql_jena.implementation.DimensionInfo;
import io.github.galbiston.geosparql_jena.implementation.jts.CoordinateSequenceDimensions;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomCoordinateSequence;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomGeometryFactory;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import java.io.IOException;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.CoordinateXYZM;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

/**
 *
 *
 */
public class GMLReaderTest {

    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    public GMLReaderTest() {
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
     * Test of getDimensionInfo method, of class GMLReader.
     */
    @Test
    public void testGetDimensionInfo0() {
        System.out.println("getDimensionInfo0");
        GMLReader instance = new GMLReader(GEOMETRY_FACTORY.createPoint(), 2);
        DimensionInfo expResult = new DimensionInfo(2, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class GMLReader.
     */
    @Test
    public void testGetDimensionInfo2() {
        System.out.println("getDimensionInfo");
        GMLReader instance = new GMLReader(GEOMETRY_FACTORY.createPoint(new CoordinateXY(11.0, 12.0)), 2);

        DimensionInfo expResult = new DimensionInfo(2, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class GMLReader.
     */
    @Test
    public void testGetDimensionInfo3a() {
        System.out.println("getDimensionInfo3a");
        GMLReader instance = new GMLReader(GEOMETRY_FACTORY.createPoint(new Coordinate(11.0, 12.0, 13.0)), 3);
        DimensionInfo expResult = new DimensionInfo(3, 3, 0);
        DimensionInfo result = instance.getDimensionInfo();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class GMLReader. GML standards don't
     * seem to define a separate spatial dimension, i.e. no distinct M
     * dimension.<br>
     * 07-036, page 310 states "srsDimension is the dimension of the coordinate
     * reference system as stated in the coordinate reference system
     * definition."<br>
     * 10-100r3, page 22 states "c) coordinate reference systems may have 1, 2
     * or 3 dimensions".
     */
    @Test
    @Ignore
    public void testGetDimensionInfo3b() {
        System.out.println("getDimensionInfo3b");
        GMLReader instance = new GMLReader(GEOMETRY_FACTORY.createPoint(new CoordinateXYM(11.0, 12.0, 13.0)), 3);
        DimensionInfo expResult = new DimensionInfo(3, 2, 0);
        DimensionInfo result = instance.getDimensionInfo();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDimensionInfo method, of class GMLReader.
     */
    @Test
    public void testGetDimensionInfo4() {
        System.out.println("getDimensionInfo4");
        GMLReader instance = new GMLReader(GEOMETRY_FACTORY.createPoint(new CoordinateXYZM(11.0, 12.0, 13.0, 14.0)), 4);
        DimensionInfo expResult = new DimensionInfo(4, 3, 0);
        DimensionInfo result = instance.getDimensionInfo();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeometry method, of class GMLReader.
     */
    @Test
    public void testGetGeometryPoint() {
        System.out.println("getGeometryPoint");
        GMLReader instance = new GMLReader(GEOMETRY_FACTORY.createPoint(new CoordinateXY(11.0, 12.0)), 2);
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "11.0 12.0"));
        Geometry result = instance.getGeometry();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeometry method, of class GMLReader.
     */
    @Test
    public void testGetGeometryPointZ() {
        System.out.println("getGeometryPointZ");
        GMLReader instance = new GMLReader(GEOMETRY_FACTORY.createPoint(new Coordinate(11.0, 12.0, 13.0)), 2);
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CoordinateSequenceDimensions.XYZ, "11.0 12.0 8.0"));
        Geometry result = instance.getGeometry();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractPoint2() throws JDOMException, IOException {
        System.out.println("extractPoint2");
        String gmlText = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>11.0 12.0</gml:pos></gml:Point>";
        GMLReader expResult = new GMLReader(GEOMETRY_FACTORY.createPoint(new CoordinateXY(11.0, 12.0)), 2, SRS_URI.OSGB36_CRS);
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractPoint3() throws JDOMException, IOException {
        System.out.println("extractPoint3");

        String gmlText = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>11.0 12.0 8.0</gml:pos></gml:Point>";
        GMLReader expResult = new GMLReader(GEOMETRY_FACTORY.createPoint(new Coordinate(11.0, 12.0, 8.0)), 2, SRS_URI.OSGB36_CRS);
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    @Ignore
    public void testExtractPoint3b() throws JDOMException, IOException {
        System.out.println("extractPoint3b");

        String gmlText = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>11.0 12.0 5.0</gml:pos></gml:Point>";
        GMLReader expResult = new GMLReader(GEOMETRY_FACTORY.createPoint(new CoordinateXYM(11.0, 12.0, 5.0)), 2, SRS_URI.OSGB36_CRS);
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractPoint4() throws JDOMException, IOException {
        System.out.println("extractPoint4");

        String gmlText = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>11.0 12.0 8.0 5.0</gml:pos></gml:Point>";
        GMLReader expResult = new GMLReader(GEOMETRY_FACTORY.createPoint(new CoordinateXYZM(11.0, 12.0, 8.0, 5.0)), 2, SRS_URI.OSGB36_CRS);
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractPolygon() throws JDOMException, IOException {
        System.out.println("extractPolygon");

        String gmlText = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">30 10 40 40 20 40 10 20 30 10</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>";
        Geometry geometry = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "30 10, 40 40, 20 40, 10 20, 30 10"));
        GMLReader expResult = new GMLReader(geometry, 2, SRS_URI.OSGB36_CRS);
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractPolygonHole() throws JDOMException, IOException {
        System.out.println("extractPolygonHole");

        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "30 10, 40 40, 20 40, 10 20, 30 10"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "20 30, 35 35, 30 20, 20 30"))};
        Geometry geometry = GEOMETRY_FACTORY.createPolygon(shell, holes);
        GMLReader expResult = new GMLReader(geometry, 2, SRS_URI.OSGB36_CRS);

        String gmlText = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">30 10 40 40 20 40 10 20 30 10</gml:posList></gml:LinearRing></gml:exterior><gml:interior><gml:LinearRing><gml:posList srsDimension=\"2\">20 30 35 35 30 20 20 30</gml:posList></gml:LinearRing></gml:interior></gml:Polygon>";
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractLineString() throws JDOMException, IOException {
        System.out.println("extractLineString");

        Geometry geometry = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "11.0 12.1, 15.0 8.0"));
        GMLReader expResult = new GMLReader(geometry, 2, SRS_URI.OSGB36_CRS);

        String gmlText = "<gml:LineString xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:posList srsDimension=\"2\">11 12.1 15 8</gml:posList></gml:LineString>";
        GMLReader result = GMLReader.extract(gmlText);

        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractMultiPoint() throws JDOMException, IOException {
        System.out.println("extractMultiPoint");

        Geometry geometry = GEOMETRY_FACTORY.createMultiPoint(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "10 40, 40 30, 20 20, 30 10"));
        GMLReader expResult = new GMLReader(geometry, 2, SRS_URI.OSGB36_CRS);

        String gmlText = "<gml:MultiPoint xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:PointMember><gml:Point srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>10 40</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>40 30</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>20 20</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>30 10</gml:pos></gml:Point></gml:PointMember></gml:MultiPoint>";
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractMutliLineString() throws JDOMException, IOException {
        System.out.println("extractMultiLineString");

        LineString[] lineStrings = new LineString[2];
        lineStrings[0] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "10 10, 20 20, 10 40"));
        lineStrings[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "40 40, 30 30, 40 20, 30 10"));
        Geometry geometry = GEOMETRY_FACTORY.createMultiLineString(lineStrings);
        GMLReader expResult = new GMLReader(geometry, 2, SRS_URI.OSGB36_CRS);

        String gmlText = "<gml:MultiLineString xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:LineStringMember><gml:LineString srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:posList srsDimension=\"2\">10 10 20 20 10 40</gml:posList></gml:LineString></gml:LineStringMember><gml:LineStringMember><gml:LineString srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:posList srsDimension=\"2\">40 40 30 30 40 20 30 10</gml:posList></gml:LineString></gml:LineStringMember></gml:MultiLineString>";
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractMultiPolygon() throws JDOMException, IOException {
        System.out.println("extractMultiPolygon");

        Polygon[] polygons = new Polygon[2];
        polygons[0] = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "40 40, 20 45, 45 30, 40 40"));
        polygons[1] = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "20 35, 10 30, 10 10, 30 5, 45 20, 20 35"));
        Geometry geometry = GEOMETRY_FACTORY.createMultiPolygon(polygons);
        GMLReader expResult = new GMLReader(geometry, 2, SRS_URI.OSGB36_CRS);

        String gmlText = "<gml:MultiPolygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:PolygonMember><gml:Polygon srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">40 40 20 45 45 30 40 40</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon></gml:PolygonMember><gml:PolygonMember><gml:Polygon srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">20 35 10 30 10 10 30 5 45 20 20 35</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon></gml:PolygonMember></gml:MultiPolygon>";
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractMultiPolygon2() throws JDOMException, IOException {
        System.out.println("extractMultiPolygon2");

        Polygon[] polygons = new Polygon[2];
        polygons[0] = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "40 40, 20 45, 45 30, 40 40"));
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "20 35, 10 30, 10 10, 30 5, 45 20, 20 35"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "30 20, 20 15, 20 25, 30 20"))};
        polygons[1] = GEOMETRY_FACTORY.createPolygon(shell, holes);
        Geometry geometry = GEOMETRY_FACTORY.createMultiPolygon(polygons);
        GMLReader expResult = new GMLReader(geometry, 2, SRS_URI.OSGB36_CRS);

        String gmlText = "<gml:MultiPolygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:PolygonMember><gml:Polygon srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">40 40 20 45 45 30 40 40</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon></gml:PolygonMember><gml:PolygonMember><gml:Polygon srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">20 35 10 30 10 10 30 5 45 20 20 35</gml:posList></gml:LinearRing></gml:exterior><gml:interior><gml:LinearRing><gml:posList srsDimension=\"2\">30 20 20 15 20 25 30 20</gml:posList></gml:LinearRing></gml:interior></gml:Polygon></gml:PolygonMember></gml:MultiPolygon>";
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extract method, of class GMLReader.
     */
    @Test
    public void testExtractGeometryCollection() throws JDOMException, IOException {
        System.out.println("extractGeometryCollection");

        Geometry[] geometries = new Geometry[2];
        geometries[0] = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "4 6"));
        geometries[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CoordinateSequenceDimensions.XY, "4 6,7 10"));
        Geometry geometry = GEOMETRY_FACTORY.createGeometryCollection(geometries);
        GMLReader expResult = new GMLReader(geometry, 2, SRS_URI.OSGB36_CRS);

        String gmlText = "<gml:GeometryCollection xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:GeometryMember><gml:Point srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:pos>4 6</gml:pos></gml:Point></gml:GeometryMember><gml:GeometryMember><gml:LineString srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"><gml:posList srsDimension=\"2\">4 6 7 10</gml:posList></gml:LineString></gml:GeometryMember></gml:GeometryCollection>";
        GMLReader result = GMLReader.extract(gmlText);

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildPointEmpty method, of class GMLReader.
     */
    @Test
    public void testBuildPointEmpty() throws JDOMException, IOException {
        System.out.println("buildPointEmpty");
        GMLReader instance = GMLReader.extract("<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"></gml:Point>");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createPoint();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildLineStringEmpty method, of class GMLReader.
     */
    @Test
    public void testBuildLineStringEmpty() throws JDOMException, IOException {
        System.out.println("buildLineEmpty");
        GMLReader instance = GMLReader.extract("<gml:LineString xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"></gml:LineString>");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createLineString();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildPolygonEmpty method, of class GMLReader.
     */
    @Test
    public void testBuildPolygonEmpty() throws JDOMException, IOException {
        System.out.println("buildPolygonEmpty");
        GMLReader instance = GMLReader.extract("<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"></gml:Polygon>");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createPolygon();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildMultiPointEmpty method, of class GMLReader.
     */
    @Test
    public void testBuildMultiPointEmpty() throws JDOMException, IOException {
        System.out.println("buildMultiPointEmpty");
        GMLReader instance = GMLReader.extract("<gml:MultiPoint xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"></gml:MultiPoint>");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createMultiPoint();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildMultiLineString method, of class GMLReader.
     */
    @Test
    public void testBuildMultiLineStringEmpty() throws JDOMException, IOException {
        System.out.println("buildMultiLineStringEmpty");
        GMLReader instance = GMLReader.extract("<gml:MultiLineString xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"></gml:MultiLineString>");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createMultiLineString();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildMultiPolygonEmpty method, of class GMLReader.
     */
    @Test
    public void testBuildMultiPolygonEmpty() throws JDOMException, IOException {
        System.out.println("buildMultiPolygonEmpty");
        GMLReader instance = GMLReader.extract("<gml:MultiPolygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"></gml:MultiPolygon>");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createMultiPolygon();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildGeometryCollectionEmpty method, of class GMLReader.
     */
    @Test
    public void testBuildGeometryCollectionEmpty() throws JDOMException, IOException {
        System.out.println("buildGeometryCollectionEmpty");
        GMLReader instance = GMLReader.extract("<gml:GeometryCollection xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\"></gml:GeometryCollection>");
        Geometry result = instance.getGeometry();

        Geometry expResult = GEOMETRY_FACTORY.createGeometryCollection();

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

}
