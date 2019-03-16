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
package io.github.galbiston.geosparql_jena.implementation.function_registration;

import io.github.galbiston.geosparql_jena.geo.topological.property_functions.geometry_property.CoordinateDimensionPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.geometry_property.DimensionPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.geometry_property.IsEmptyPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.geometry_property.IsSimplePF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.geometry_property.IsValidPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.geometry_property.SpatialDimensionPF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.geometry_property.CoordinateDimensionFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.geometry_property.DimensionFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.geometry_property.IsEmptyFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.geometry_property.IsSimpleFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.geometry_property.IsValidFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.geometry_property.SpatialDimensionFF;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 *
 */
public class GeometryProperty {

    /**
     * This method loads all the Geometry property property functions.
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        registry.put(Geo.DIMENSION, DimensionPF.class);
        registry.put(Geo.COORDINATE_DIMENSION, CoordinateDimensionPF.class);
        registry.put(Geo.SPATIAL_DIMENSION, SpatialDimensionPF.class);
        registry.put(Geo.IS_SIMPLE, IsSimplePF.class);
        registry.put(Geo.IS_EMPTY, IsEmptyPF.class);
        registry.put(Geo.IS_VALID, IsValidPF.class);
    }

    /**
     * This method loads all the Geometry property filter functions.<br>
     * N.B. These functions are not part of the GeoSPARQL standard but have been
     * included for convenience using GeometryLiterals.
     *
     * @param registry - the FunctionRegistry to be used
     */
    public static void loadFilterFunctions(FunctionRegistry registry) {

        registry.put(Geof.DIMENSION, DimensionFF.class);
        registry.put(Geof.COORDINATE_DIMENSION, CoordinateDimensionFF.class);
        registry.put(Geof.SPATIAL_DIMENSION, SpatialDimensionFF.class);
        registry.put(Geof.IS_SIMPLE, IsSimpleFF.class);
        registry.put(Geof.IS_EMPTY, IsEmptyFF.class);
        registry.put(Geof.IS_VALID, IsValidFF.class);
    }

}
