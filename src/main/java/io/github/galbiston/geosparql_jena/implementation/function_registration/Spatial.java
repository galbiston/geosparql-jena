/*
 * Copyright 2018 .
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
package io.github.galbiston.geosparql_jena.implementation.function_registration;

import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialFunction;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialProp;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonFF;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.NearbyFF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.NearbyGeomPF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.NearbyPF;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 *
 */
public class Spatial {

    /**
     * This method loads all the Jena Spatial Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        registry.put(SpatialProp.NEARBY, NearbyPF.class);
        registry.put(SpatialProp.NEARBY_GEOM, NearbyGeomPF.class);
        registry.put(SpatialProp.WITHIN_CIRCLE, NearbyPF.class);
        registry.put(SpatialProp.NEARBY_GEOM, NearbyGeomPF.class);
    }

    /**
     * This method loads all the Jena Spatial Filter Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(SpatialFunction.CONVERT_LAT_LON, ConvertLatLonFF.class);
        functionRegistry.put(SpatialFunction.NEARBY, NearbyFF.class);
        functionRegistry.put(SpatialFunction.WITHIN_CIRCLE, NearbyFF.class);
    }

}
