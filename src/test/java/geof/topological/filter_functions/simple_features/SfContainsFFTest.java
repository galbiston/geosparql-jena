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
package geof.topological.filter_functions.simple_features;

import conformance_test.TestQuerySupport;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.sparql.lang.sparql_11.ParseException;
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
public class SfContainsFFTest {

    private static final InfModel SAMPLE_DATA_MODEL = TestQuerySupport.getSampleData_WKT();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ParseException {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSfContainFilterFunction() {
        System.out.println("SfContains Filter Function");
        String queryString = "SELECT ?geometry WHERE{"
                + " ?geometry geo:asWKT ?aWKT ."
                + " FILTER geof:sfContains(?aWKT, \"<http://www.opengis.net/def/crs/EPSG/0/27700> Point(35 55)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> ) ."
                + "}";

        List<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/Geometry#PolygonH");
        List<String> result = TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
