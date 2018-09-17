/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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
package geosparql_jena.geo.topological.property_functions.geometry_property;

import geosparql_jena.implementation.GeometryWrapper;
import geosparql_jena.implementation.datatype.WKTDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
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
 * @author Gerg
 */
public class IsEmptyPFTest {

    public IsEmptyPFTest() {
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
     * Test of applyPredicate method, of class IsEmptyPF.
     */
    @Test
    public void testApplyPredicate_false() {
        System.out.println("applyPredicate_false");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(90 60)", WKTDatatype.URI);
        IsEmptyPF instance = new IsEmptyPF();
        Literal expResult = ResourceFactory.createTypedLiteral("false", XSDDatatype.XSDboolean);
        Literal result = instance.applyPredicate(geometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of applyPredicate method, of class IsEmptyPF.
     */
    @Test
    public void testApplyPredicate_true() {
        System.out.println("applyPredicate_true");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT EMPTY", WKTDatatype.URI);
        IsEmptyPF instance = new IsEmptyPF();
        Literal expResult = ResourceFactory.createTypedLiteral("true", XSDDatatype.XSDboolean);
        Literal result = instance.applyPredicate(geometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
