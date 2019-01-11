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
package io.github.galbiston.geosparql_jena.spatial.property_functions.nearby;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialExtension;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexTestData;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonFF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.SpatialArguments;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.pfunction.PropFuncArg;
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
public class NearbyGeomPFTest {

    public NearbyGeomPFTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        GeoSPARQLConfig.setupSpatial();
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
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test
    public void testExtractObjectArguments_4args() {
        System.out.println("extractObjectArguments_4args");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test
    public void testExtractObjectArguments_3args() {
        System.out.println("extractObjectArguments_3args");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = -1;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI));
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test
    public void testExtractObjectArguments_2args() {
        System.out.println("extractObjectArguments_2args");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5;
        String unitsURI = Unit_URI.KILOMETER_URL;
        int limit = -1;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_1args_fail() {
        System.out.println("extractObjectArguments_1args_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5;
        String unitsURI = Unit_URI.KILOMETER_URL;
        int limit = -1;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_5args_fail() {
        System.out.println("extractObjectArguments_5args_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode(), NodeValue.makeBoolean(false).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_pos0_fail() {
        System.out.println("extractObjectArguments_4args_pos0_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeString("0").asNode(), NodeValue.makeFloat(lon).asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_pos1_fail() {
        System.out.println("extractObjectArguments_4args_pos1_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(NodeValue.makeFloat(lat).asNode(), NodeValue.makeString("1").asNode(), NodeValue.makeFloat(radius).asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_pos2_fail() {
        System.out.println("extractObjectArguments_4args_pos2_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeString("5000").asNode(), NodeFactory.createURI(unitsURI), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of extractObjectArguments method, of class NearbyGeomPF.
     */
    @Test(expected = ExprEvalException.class)
    public void testExtractObjectArguments_4args_pos3_fail() {
        System.out.println("extractObjectArguments_4args_pos3_fail");
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        float lat = 0;
        float lon = 1;
        float radius = 5000;
        String unitsURI = Unit_URI.METRE_URL;
        int limit = 10;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode(), NodeValue.makeString(unitsURI).asNode(), NodeValue.makeInteger(limit).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometry);
        SearchEnvelope searchEnvelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        NearbyGeomPF instance = new NearbyGeomPF();
        SpatialArguments expResult = new SpatialArguments(limit, geometryWrapper, searchEnvelope);
        SpatialArguments result = instance.extractObjectArguments(predicate, object);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of testRelation method, of class NearbyGeomPF.
     */
    @Test
    public void testTestRelation() {
        System.out.println("testRelation");

        NearbyGeomPF instance = new NearbyGeomPF();

        //Property Function
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        //Geometry and Envelope parameters
        float lat = 0;
        float lon = 1;
        float radius = 5;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        Literal targetGeometry = ConvertLatLonFF.toLiteral(lat + 0.0001f, lon);

        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        //Function arguments
        SpatialArguments spatialArguments = instance.extractObjectArguments(predicate, object);
        GeometryWrapper targetGeometryWrapper = GeometryWrapper.extract(targetGeometry);

        //Test arguments
        boolean expResult = true;
        boolean result = instance.testRelation(spatialArguments, targetGeometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of testRelation method, of class NearbyGeomPF.
     */
    @Test
    public void testTestRelation_fail() {
        System.out.println("testRelation_fail");

        NearbyGeomPF instance = new NearbyGeomPF();

        //Property Function
        Node predicate = NodeFactory.createURI(SpatialExtension.NEARBY_GEOM_PROP);

        //Geometry and Envelope parameters
        float lat = 0;
        float lon = 1;
        float radius = 5;

        Literal geometry = ConvertLatLonFF.toLiteral(lat, lon);
        Literal targetGeometry = ConvertLatLonFF.toLiteral(lat + 10f, lon);

        List<Node> objectNodes = Arrays.asList(geometry.asNode(), NodeValue.makeFloat(radius).asNode());
        PropFuncArg object = new PropFuncArg(objectNodes);

        //Function arguments
        SpatialArguments spatialArguments = instance.extractObjectArguments(predicate, object);
        GeometryWrapper targetGeometryWrapper = GeometryWrapper.extract(targetGeometry);

        //Test arguments
        boolean expResult = false;
        boolean result = instance.testRelation(spatialArguments, targetGeometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of execEvaluated method, of class NearbyGeomPF.
     */
    @Test
    public void testExecEvaluated() {
        System.out.println("execEvaluated");

        Dataset dataset = SpatialIndexTestData.createTestDataset();
        SpatialIndex spatialIndex = SpatialIndexTestData.createTestIndex();
        SpatialIndex.setSpatialIndex(dataset, spatialIndex);

        String query = "PREFIX spatial: <http://jena.apache.org/spatial#>\n"
                + "\n"
                + "SELECT ?subj\n"
                + "WHERE{\n"
                + "    BIND( \"<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(48.857487 2.373047)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> AS ?geom)"
                + "    ?subj spatial:nearbyGeom(?geom 350) .\n"
                + "}ORDER by ?subj";

        List<Resource> result = new ArrayList<>();
        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                Resource feature = qs.getResource("subj");
                result.add(feature);
            }
        }

        List<Resource> expResult = Arrays.asList(SpatialIndexTestData.LONDON_FEATURE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
