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
package implementation;

import geof.topological.RelateFF;
import implementation.data_conversion.CSVConversion;
import implementation.datatype.GeometryDatatype;
import implementation.function_registration.Egenhofer;
import implementation.function_registration.GeometryProperty;
import implementation.function_registration.NonTopological;
import implementation.function_registration.RCC8;
import implementation.function_registration.Relate;
import implementation.function_registration.SimpleFeatures;
import implementation.index.IndexConfiguration;
import implementation.index.IndexConfiguration.IndexOption;
import implementation.index.SpatialIndex;
import implementation.registry.CRSRegistry;
import implementation.vocabulary.Geo;
import java.io.File;
import java.io.InputStream;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import org.apache.jena.tdb.TDBFactory;

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

    /**
     * Prepare an empty GeoSPARQL model with RDFS reasoning.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling loadFunctions methods.
     *
     * @return
     */
    public static InfModel prepare() {
        return GeoSPARQLSupport.prepareRDFS(ModelFactory.createDefaultModel());
    }

    /**
     * Prepare a GeoSPARQL model from an existing model with RDFS reasoning.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling loadFunctions methods.
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
     * This can be changed by calling loadFunctions methods.
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
     * This can be changed by calling loadFunctions methods.
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
     * This can be changed by calling loadFunctions methods.
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
     * This can be changed by calling loadFunctions methods.
     *
     * @param model
     * @param geosparqlSchemaInputStream
     * @param reasoner
     * @return
     */
    public static InfModel prepare(Model model, InputStream geosparqlSchemaInputStream, Reasoner reasoner) {

        //Register GeoSPARQL functions if required.
        loadFunctionsMemoryIndex();

        //Load GeoSPARQL Schema
        Model schema = ModelFactory.createDefaultModel();
        schema.read(geosparqlSchemaInputStream, null);

        //Apply the schema to the reasoner.
        reasoner = reasoner.bindSchema(schema);

        //Setup inference model.
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        //Build spatial index.
        SpatialIndex.build(infModel, "Model");

        return infModel;
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup.
     */
    public static final void loadFunctionsMemoryIndex() {
        loadFunctions(IndexOption.MEMORY);
        IndexConfiguration.setupMemoryIndex();
    }

    public static final void loadFunctionsMemoryIndex(Integer geometryLiteralIndex, Integer geometryTransformIndex, Integer queryRewriteIndex, Boolean spatialIndexActive) {
        loadFunctions(IndexOption.MEMORY);
        IndexConfiguration.setIndexMaxSize(geometryLiteralIndex, geometryTransformIndex, queryRewriteIndex, spatialIndexActive);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with no indexing.
     * <br>Use this for no indexing GeoSPARQL setup.
     * <br>Warning: Any previously setup index folders will be deleted.
     */
    public static final void loadFunctionsNoIndex() {
        loadFunctions(IndexOption.NONE);
    }

    /**
     * Switch off indexing when parsing Geometry Literals etc. No GeoSPARQL
     * functions loaded.
     */
    public static final void noIndex() {
        IndexConfiguration.setConfig(IndexOption.NONE);
        //Setup Default Cordinate Reference Systems
        CRSRegistry.setupDefaultCRS();
        CSVConversion.registerDatatypes();
        GeometryDatatype.registerDatatypes();
    }

    public static final void loadFunctions(IndexOption indexOption, File tdbFolder) {
        Dataset dataset = TDBFactory.createDataset(tdbFolder.getAbsolutePath());
        loadFunctions(indexOption, dataset);
    }

    public static final void loadFunctions(IndexOption indexOption, Dataset dataset) {
        loadFunctions(indexOption);
        SpatialIndex.prepare(dataset);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions. Store any indexes
     * in the specified folder.
     * <br>Use this for persisting indexes such as a TDB setup or storing memory
     * indexes to file at shutdown.
     * <br>Warning: When set to NONE, any previously setup index folders will be
     * deleted.
     *
     * @param indexOption
     */
    public static final void loadFunctions(IndexOption indexOption) {

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
    public static final void resetIndexesAndRegistries() {
        //Convenience method so that setup and clearing in one class.
        IndexConfiguration.resetIndexesAndRegistries();
    }

}
