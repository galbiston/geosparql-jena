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

import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialExtension;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonBoxFF;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonFF;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.NearbyFF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.IntersectBoxGeomPF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.IntersectBoxPF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.NearbyGeomPF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.NearbyPF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.WithinBoxGeomPF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.WithinBoxPF;
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

        registry.put(SpatialExtension.NEARBY_PROP, NearbyPF.class);
        registry.put(SpatialExtension.NEARBY_GEOM_PROP, NearbyGeomPF.class);
        registry.put(SpatialExtension.WITHIN_CIRCLE_PROP, NearbyPF.class);
        registry.put(SpatialExtension.WITHIN_CIRCLE_GEOM_PROP, NearbyGeomPF.class);
        registry.put(SpatialExtension.WITHIN_BOX_PROP, WithinBoxPF.class);
        registry.put(SpatialExtension.WITHIN_BOX_GEOM_PROP, WithinBoxGeomPF.class);
        registry.put(SpatialExtension.INTERSECT_BOX_PROP, IntersectBoxPF.class);
        registry.put(SpatialExtension.INTERSECT_BOX_GEOM_PROP, IntersectBoxGeomPF.class);
    }

    /**
     * This method loads all the Jena Spatial Filter Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(SpatialExtension.CONVERT_LAT_LON, ConvertLatLonFF.class);
        functionRegistry.put(SpatialExtension.CONVERT_LAT_LON_BOX, ConvertLatLonBoxFF.class);
        functionRegistry.put(SpatialExtension.NEARBY, NearbyFF.class);
        functionRegistry.put(SpatialExtension.WITHIN_CIRCLE, NearbyFF.class);
    }

}
