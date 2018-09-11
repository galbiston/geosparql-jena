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
package conformance_test.core;

import conformance_test.TestQuerySupport;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 *
 * A.1.2 /conf/core/spatial-object-class Requirement:
 * /req/core/spatial-object-class
 *
 * Implementations shall allow the RDFS class geo:SpatialObject to be used in
 * SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:SpatialObject return the
 * correct result on a test dataset.
 *
 * c.) Reference: Clause 6.2.1 Req 2
 *
 * d.) Test Type: Capabilities
 */
public class SpatialObjectClassTest {

    private static final InfModel SAMPLE_DATA_MODEL = TestQuerySupport.getSampleData_WKT();

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

    @Test
    public void spatialObjectClassTest() {
        System.out.println("Spatial Object Class");

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Feature#A");
        expResult.add("http://example.org/Feature#B");
        expResult.add("http://example.org/Feature#C");
        expResult.add("http://example.org/Feature#C2");
        expResult.add("http://example.org/Feature#D");
        expResult.add("http://example.org/Feature#E");
        expResult.add("http://example.org/Feature#Empty");
        expResult.add("http://example.org/Feature#F");
        expResult.add("http://example.org/Feature#G");
        expResult.add("http://example.org/Feature#H");
        expResult.add("http://example.org/Feature#I");
        expResult.add("http://example.org/Feature#J");
        expResult.add("http://example.org/Feature#K");
        expResult.add("http://example.org/Feature#L");
        expResult.add("http://example.org/Feature#X");
        expResult.add("http://example.org/Feature#Y");
        expResult.add("http://example.org/Geometry#LineStringD");
        expResult.add("http://example.org/Geometry#LineStringE");
        expResult.add("http://example.org/Geometry#LineStringF");
        expResult.add("http://example.org/Geometry#LineStringG");
        expResult.add("http://example.org/Geometry#PointA");
        expResult.add("http://example.org/Geometry#PointB");
        expResult.add("http://example.org/Geometry#PointC");
        expResult.add("http://example.org/Geometry#PointC2");
        expResult.add("http://example.org/Geometry#PointEmpty");
        expResult.add("http://example.org/Geometry#PolygonH");
        expResult.add("http://example.org/Geometry#PolygonI");
        expResult.add("http://example.org/Geometry#PolygonJ");
        expResult.add("http://example.org/Geometry#PolygonK");
        expResult.add("http://example.org/Geometry#PolygonL");

        String queryString = "SELECT ?spatialObject WHERE{"
                + " ?spatialObject rdf:type geo:SpatialObject ."
                + "}ORDER BY ?spatialObject";

        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
