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
package geosparql_jena.implementation;

import geosparql_jena.geof.topological.RelateFF;
import geosparql_jena.implementation.data_conversion.CSVConversion;
import geosparql_jena.implementation.datatype.GeometryDatatype;
import geosparql_jena.implementation.function_registration.Egenhofer;
import geosparql_jena.implementation.function_registration.GeometryProperty;
import geosparql_jena.implementation.function_registration.NonTopological;
import geosparql_jena.implementation.function_registration.RCC8;
import geosparql_jena.implementation.function_registration.Relate;
import geosparql_jena.implementation.function_registration.SimpleFeatures;
import geosparql_jena.implementation.index.IndexConfiguration;
import geosparql_jena.implementation.index.IndexConfiguration.IndexOption;
import geosparql_jena.implementation.registry.CRSRegistry;
import geosparql_jena.implementation.vocabulary.Geo;
import java.io.InputStream;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 *
 */
public class GeoSPARQLSupport {

    /**
     * GeoSPARQL schema
     */
    private static final String GEOSPARQL_SCHEMA = "schema/geosparql_vocab_all_v1_0_1_updated.rdf";
    private static Boolean IS_FUNCTIONS_REGISTERED = false;
    private static Boolean IS_QUERY_REWRITE_ENABLED = true;

    /**
     * Prepare an empty GeoSPARQL model with RDFS reasoning.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling setup methods.
     *
     * @return
     */
    public static InfModel prepare() {
        return GeoSPARQLSupport.prepareRDFS(ModelFactory.createDefaultModel());
    }

    /**
     * Prepare a GeoSPARQL model from an existing model with RDFS reasoning.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling setup methods.
     *
     * @param model
     * @return
     */
    public static InfModel prepareRDFS(Model model) {
        return prepare(model, ReasonerRegistry.getRDFSReasoner());
    }

    /**
     * Prepare a GeoSPARQL model from an existing model with alternative
     * Reasoner, e.g. OWL.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling setup methods.
     *
     * @param model
     * @param reasoner
     * @return
     */
    public static InfModel prepare(Model model, Reasoner reasoner) {
        InputStream geosparqlSchemaInputStream = GeoSPARQLSupport.class.getClassLoader().getResourceAsStream(GEOSPARQL_SCHEMA);
        return prepare(model, geosparqlSchemaInputStream, reasoner);
    }

    /**
     * Prepare a GeoSPARQL model from file with RDFS reasoning.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling setup methods.
     *
     * @param inputStream
     * @return
     */
    public static InfModel prepareRDFS(InputStream inputStream) {
        return GeoSPARQLSupport.prepare(inputStream, ReasonerRegistry.getRDFSReasoner());
    }

    /**
     * Prepare a GeoSPARQL model from file with alternative Reasoner, e.g. OWL.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling setup methods.
     *
     * @param inputStream
     * @param reasoner
     * @return
     */
    public static InfModel prepare(InputStream inputStream, Reasoner reasoner) {
        Model model = ModelFactory.createDefaultModel();
        model.read(inputStream, null);

        return GeoSPARQLSupport.prepare(model, reasoner);
    }

    /**
     * Prepare a model from an existing model with alternative GeoSPARQL schema
     * and Reasoner, e.g. OWL.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling setup methods.
     *
     * @param model
     * @param geosparqlSchemaInputStream
     * @param reasoner
     * @return
     */
    public static InfModel prepare(Model model, InputStream geosparqlSchemaInputStream, Reasoner reasoner) {

        //Register GeoSPARQL functions if required.
        GeoSPARQLSupport.setupMemoryIndex();

        //Load GeoSPARQL Schema
        Model schema = ModelFactory.createDefaultModel();
        schema.read(geosparqlSchemaInputStream, null);

        //Apply the schema to the reasoner.
        reasoner = reasoner.bindSchema(schema);

        //Setup inference model.
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        return infModel;
    }

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
     * <br>Use this for in-memory indexing GeoSPARQL setup and to control the
     * index sizes. Expiry is defaulted to 5,000 milliseconds.
     *
     * @param geometryLiteralIndex
     * @param geometryTransformIndex
     * @param queryRewriteIndex
     */
    public static final void setupMemoryIndex(Integer geometryLiteralIndex, Integer geometryTransformIndex, Integer queryRewriteIndex) {
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
    public static final void setupMemoryIndex(Long geometryLiteralIndex, Long geometryTransformIndex, Long queryRewriteIndex) {
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
