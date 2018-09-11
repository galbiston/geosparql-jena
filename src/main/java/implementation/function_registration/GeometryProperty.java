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
package implementation.function_registration;

import geo.topological.property_functions.geometry_property.CoordinateDimensionPF;
import geo.topological.property_functions.geometry_property.DimensionPF;
import geo.topological.property_functions.geometry_property.IsEmptyPF;
import geo.topological.property_functions.geometry_property.IsSimplePF;
import geo.topological.property_functions.geometry_property.SpatialDimensionPF;
import implementation.vocabulary.Geo;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 *
 */
public class GeometryProperty {

    /**
     * This method loads all the Geometry property base function
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        registry.put(Geo.DIMENSION, DimensionPF.class);
        registry.put(Geo.COORDINATE_DIMENSION, CoordinateDimensionPF.class);
        registry.put(Geo.SPATIAL_DIMENSION, SpatialDimensionPF.class);
        registry.put(Geo.IS_SIMPLE, IsSimplePF.class);
        registry.put(Geo.IS_EMPTY, IsEmptyPF.class);
    }

}
