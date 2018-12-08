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

import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geof;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonFF;
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

        //registry.put(Geo.SF_CONTAINS_NAME, SfContainsPF.class);
    }

    /**
     * This method loads all the Jena Spatial Filter Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.CONVERT_LAT_LON, ConvertLatLonFF.class);

    }
}
