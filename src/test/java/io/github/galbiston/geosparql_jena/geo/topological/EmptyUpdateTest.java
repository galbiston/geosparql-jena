/*
 * Copyright 2019 the original author or authors.
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
package io.github.galbiston.geosparql_jena.geo.topological;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
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
public class EmptyUpdateTest {

    private static Dataset dataset;

    public EmptyUpdateTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        GeoSPARQLConfig.setupNoIndex();
        dataset = DatasetFactory.create(ModelFactory.createDefaultModel());
        //Add data
        String update = "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n"
                + "\n"
                + "INSERT DATA{"
                + "<http://example.org/Geometry#LineStringA> geo:hasSerialization \"LINESTRING(0 0, 10 10)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>;"
                + " a geo:Geometry ;"
                + " a geo:SpatialObject ."
                + "<http://example.org/Geometry#LineStringB> geo:hasSerialization \"LINESTRING(0 5, 10 5)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>;"
                + " a geo:Geometry ;"
                + " a geo:SpatialObject ."
                + "<http://example.org/Geometry#PointC> geo:hasSerialization \"POINT(5 5)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>;"
                + " a geo:Geometry ;"
                + " a geo:SpatialObject ."
                + "<http://example.org/Geometry#PolygonD> geo:hasSerialization \"POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>;"
                + " a geo:Geometry ;"
                + " a geo:SpatialObject ."
                + "}";

        UpdateRequest updateRequest = UpdateFactory.create(update);
        UpdateProcessor updateProcessor = UpdateExecutionFactory.create(updateRequest, dataset);
        updateProcessor.execute();

        //System.out.println("Dataset Updated");
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
     * Test of empty dataset.
     */
    @Test
    public void testEmpty() {
        System.out.println("empty update");

        String query = "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n"
                + "\n"
                + "SELECT ?obj\n"
                + "WHERE{\n"
                + "    <http://example.org/Geometry#PolygonD> geo:sfContains ?obj .\n"
                + "}ORDER by ?obj";
        List<Resource> result = new ArrayList<>();
        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            ResultSet rs = qe.execSelect();

            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                Resource obj = qs.getResource("obj");
                result.add(obj);
            }

            //ResultSetFormatter.outputAsTSV(rs);
        }

        List<Resource> expResult = new ArrayList<>();
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#LineStringA"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#LineStringB"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#PointC"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#PolygonD"));

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of empty dataset.
     */
    /*
    @Test
    public void testEmpty2() {
        System.out.println("empty2");

        String query = "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n"
                + "\n"
                + "SELECT ?subj ?obj\n"
                + "WHERE{\n"
                + "    ?subj geo:sfIntersects ?obj .\n"
                + "}ORDER by ?subj";

        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            ResultSet rs = qe.execSelect();
            ResultSetFormatter.outputAsTSV(rs);
        }
    }
     */
    /**
     * Test of empty dataset.
     */
    /*
    @Test
    public void testEmpty3() {
        System.out.println("empty3");

        String query = "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n"
                + "\n"
                + "SELECT ?s ?p ?o\n"
                + "WHERE{\n"
                + "    ?s ?p ?o .\n"
                + "}";

        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            ResultSet rs = qe.execSelect();
            ResultSetFormatter.outputAsTSV(rs);
        }
    }
     */
}
