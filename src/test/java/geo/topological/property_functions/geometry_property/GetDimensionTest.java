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
package geo.topological.property_functions.geometry_property;

import conformance_test.TestQuerySupport;
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
 */
public class GetDimensionTest {

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

    /**
     * Test of get method, of class GetDimension.
     */
    @Test
    public void testGetDimension() {
        System.out.println("Get Dimension");
        String queryString = "SELECT ?dimension ?empty ?simple ?coordinateDimension WHERE{"
                + " BIND(geom:PointB AS ?geometry) ."
                + " ?geometry geo:dimension ?dimension ."
                + " ?geometry geo:isEmpty ?empty ."
                + " ?geometry geo:isSimple ?simple ."
                + " ?geometry geo:coordinateDimension ?coordinateDimension . "
                + "}";

        String expResult = "0^^http://www.w3.org/2001/XMLSchema#integer false^^http://www.w3.org/2001/XMLSchema#boolean true^^http://www.w3.org/2001/XMLSchema#boolean 2^^http://www.w3.org/2001/XMLSchema#integer";
        String result = TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }
}
