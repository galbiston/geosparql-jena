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
package io.github.galbiston.geosparql_jena.configuration;

import io.github.galbiston.geosparql_jena.geof.topological.RelateFF;
import io.github.galbiston.geosparql_jena.implementation.data_conversion.CSVConversion;
import io.github.galbiston.geosparql_jena.implementation.datatype.GeometryDatatype;
import io.github.galbiston.geosparql_jena.implementation.function_registration.Egenhofer;
import io.github.galbiston.geosparql_jena.implementation.function_registration.GeometryProperty;
import io.github.galbiston.geosparql_jena.implementation.function_registration.NonTopological;
import io.github.galbiston.geosparql_jena.implementation.function_registration.RCC8;
import io.github.galbiston.geosparql_jena.implementation.function_registration.Relate;
import io.github.galbiston.geosparql_jena.implementation.function_registration.SimpleFeatures;
import io.github.galbiston.geosparql_jena.implementation.index.IndexConfiguration;
import io.github.galbiston.geosparql_jena.implementation.index.IndexConfiguration.IndexOption;
import io.github.galbiston.geosparql_jena.implementation.registry.CRSRegistry;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 *
 */
public class GeoSPARQLConfig {

    /**
     * GeoSPARQL schema
     */
    private static Boolean IS_FUNCTIONS_REGISTERED = false;
    private static Boolean IS_QUERY_REWRITE_ENABLED = true;

    /**
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup. Query re-write
     * enabled.
     */
    public static final void setupMemoryIndex() {
        setup(IndexOption.MEMORY, true);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup. Query re-write
     * enabled.
     *
     * @param isQueryRewriteEnabled
     */
    public static final void setupMemoryIndex(Boolean isQueryRewriteEnabled) {
        setup(IndexOption.MEMORY, isQueryRewriteEnabled);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup and to control the
     * index sizes. Expiry is defaulted to 5,000 milliseconds.
     *
     * @param geometryLiteralIndex
     * @param geometryTransformIndex
     * @param queryRewriteIndex
     */
    public static final void setupMemoryIndexSize(Integer geometryLiteralIndex, Integer geometryTransformIndex, Integer queryRewriteIndex) {
        setup(IndexOption.MEMORY, true);
        IndexConfiguration.setIndexMaxSize(geometryLiteralIndex, geometryTransformIndex, queryRewriteIndex);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup and to control the
     * index expiry rate (milliseconds). Size is defaulted to unlimited.
     *
     * @param geometryLiteralIndex
     * @param geometryTransformIndex
     * @param queryRewriteIndex
     */
    public static final void setupMemoryIndexExpiry(Long geometryLiteralIndex, Long geometryTransformIndex, Long queryRewriteIndex) {
        setup(IndexOption.MEMORY, true);
        IndexConfiguration.setIndexExpiry(geometryLiteralIndex, geometryTransformIndex, queryRewriteIndex);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup and to control the
     * index sizes (default: unlimited) and expiry rate (default: 5,000
     * milliseconds).
     *
     * @param geometryLiteralIndex
     * @param geometryTransformIndex
     * @param queryRewriteIndex
     * @param geometryLiteralIndexExpiry
     * @param geometryTransformIndexExpiry
     * @param queryRewriteIndexExpiry
     * @param isQueryRewriteEnabled
     */
    public static final void setupMemoryIndex(Integer geometryLiteralIndex, Integer geometryTransformIndex, Integer queryRewriteIndex, Long geometryLiteralIndexExpiry, Long geometryTransformIndexExpiry, Long queryRewriteIndexExpiry, Boolean isQueryRewriteEnabled) {
        setup(IndexOption.MEMORY, isQueryRewriteEnabled);
        IndexConfiguration.setIndexMaxSize(geometryLiteralIndex, geometryTransformIndex, queryRewriteIndex);
        IndexConfiguration.setIndexExpiry(geometryLiteralIndexExpiry, geometryTransformIndexExpiry, queryRewriteIndexExpiry);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with no indexing.
     * <br>Use this for no indexing GeoSPARQL setup.
     */
    public static final void setupNoIndex() {
        setup(IndexOption.NONE, true);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with no indexing.
     * <br>Use this for no indexing GeoSPARQL setup.
     *
     * @param isQueryRewriteEnabled
     */
    public static final void setupNoIndex(Boolean isQueryRewriteEnabled) {
        setup(IndexOption.NONE, isQueryRewriteEnabled);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions. Query rewrite
     * enabled.
     *
     * @param indexOption
     */
    public static final void setup(IndexOption indexOption) {

    }

    /**
     * Initialise all GeoSPARQL property and filter functions.
     *
     * @param indexOption
     * @param isQueryRewriteEnabled
     */
    public static final void setup(IndexOption indexOption, Boolean isQueryRewriteEnabled) {

        IS_QUERY_REWRITE_ENABLED = isQueryRewriteEnabled;

        //Set the configuration for indexing.
        IndexConfiguration.setConfig(indexOption);

        //Only register functions once.
        if (!IS_FUNCTIONS_REGISTERED) {

            //Setup Default Cordinate Reference Systems
            CRSRegistry.setupDefaultCRS();

            //Register GeometryLiteral datatypes for CSV conversion
            CSVConversion.registerDatatypes();

            //Register GeometryDatatypes with the TypeMapper.
            GeometryDatatype.registerDatatypes();

            PropertyFunctionRegistry propertyRegistry = PropertyFunctionRegistry.get();
            FunctionRegistry functionRegistry = FunctionRegistry.get();
            NonTopological.loadFilterFunctions(functionRegistry);
            functionRegistry.put(Geo.RELATE_NAME, RelateFF.class);
            SimpleFeatures.loadPropertyFunctions(propertyRegistry);
            SimpleFeatures.loadFilterFunctions(functionRegistry);
            Egenhofer.loadPropertyFunctions(propertyRegistry);
            Egenhofer.loadFilterFunctions(functionRegistry);
            RCC8.loadPropertyFunctions(propertyRegistry);
            RCC8.loadFilterFunctions(functionRegistry);
            Relate.loadRelateFunction(functionRegistry);
            GeometryProperty.loadPropertyFunctions(propertyRegistry);
            IS_FUNCTIONS_REGISTERED = true;
        }
    }

    /**
     * Empty all indexes and registries currently in use.
     */
    public static final void reset() {
        //Convenience method so that setup and clearing in one class.
        IndexConfiguration.resetIndexesAndRegistries();
    }

    public static Boolean isQueryRewriteEnabled() {
        return IS_QUERY_REWRITE_ENABLED;
    }

}
