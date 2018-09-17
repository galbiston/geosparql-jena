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
package geosparql_jena.geo.topological.property_functions.geometry_property;

import geosparql_jena.geo.topological.GenericGeometryPropertyFunction;
import geosparql_jena.implementation.GeometryWrapper;
import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 *
 */
public class IsEmptyPF extends GenericGeometryPropertyFunction {

    @Override
    protected Literal applyPredicate(GeometryWrapper geometryWrapper) {
        Boolean isEmpty = geometryWrapper.isEmpty();
        return ResourceFactory.createTypedLiteral(isEmpty.toString(), XSDBaseNumericType.XSDboolean);
    }

}
