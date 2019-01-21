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

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.data_conversion.ConvertData;
import io.github.galbiston.geosparql_jena.implementation.index.GeometryLiteralIndex;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import static io.github.galbiston.geosparql_jena.implementation.vocabulary.GeoSPARQL_URI.GEO_URI;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialExtension;
import io.github.galbiston.geosparql_jena.spatial.ConvertLatLon;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GeoSPARQLOperations {

    private static final String GEOSPARQL_SCHEMA_FILE = "schema/geosparql_vocab_all_v1_0_1_updated.rdf";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Every subProperty of hasGeometry is made a subProperty of
     * hasDefaultGeometry.<br>
     * Assumption that each Feature has a single hasGeometry property.<br>
     * Requires RDFS inferencing to propagate through the data.
     *
     * @param model
     */
    public static final void applySubPropertyDefaultGeometry(Model model) {

        try {
            ResIterator resIt = model.listResourcesWithProperty(RDFS.subPropertyOf, Geo.HAS_GEOMETRY_PROP);
            while (resIt.hasNext()) {
                Resource res = resIt.nextResource();
                res.addProperty(RDFS.subPropertyOf, Geo.HAS_DEFAULT_GEOMETRY_PROP);
            }

        } catch (Exception ex) {
            LOGGER.error("Inserting GeoSPARQL predicates error: {}", ex.getMessage());
        }
    }

    /**
     * Apply hasDefaultGeometry for every Feature with a single hasGeometry
     * property.
     *
     * @param dataset
     */
    public static final void applyDefaultGeometry(Dataset dataset) {

        try {
            LOGGER.info("Applying hasDefaultGeometry - Started");
            //Default Model
            dataset.begin(ReadWrite.WRITE);
            Model defaultModel = dataset.getDefaultModel();
            GeoSPARQLOperations.applyDefaultGeometry(defaultModel);

            //Named Models
            Iterator<String> graphNames = dataset.listNames();
            while (graphNames.hasNext()) {
                String graphName = graphNames.next();
                Model namedModel = dataset.getNamedModel(graphName);
                GeoSPARQLOperations.applyDefaultGeometry(namedModel);
            }

            dataset.commit();
            LOGGER.info("Applying hasDefaultGeometry - Completed");
        } catch (Exception ex) {
            LOGGER.error("Write Error: {}", ex.getMessage());
        } finally {
            dataset.end();
        }

    }

    /**
     * Apply hasDefaultGeometry for every Feature with a single hasGeometry
     * property.
     *
     * @param model
     */
    public static final void applyDefaultGeometry(Model model) {

        ResIterator featureIt = model.listResourcesWithProperty(Geo.HAS_GEOMETRY_PROP);
        while (featureIt.hasNext()) {
            Resource feature = featureIt.nextResource();
            if (!feature.hasProperty(Geo.HAS_DEFAULT_GEOMETRY_PROP)) {
                List<Statement> statement = feature.listProperties(Geo.HAS_GEOMETRY_PROP).toList();
                if (statement.size() == 1) {
                    try {
                        Resource geometry = statement.get(0).getResource();
                        feature.addProperty(Geo.HAS_DEFAULT_GEOMETRY_PROP, geometry);
                    } catch (Exception ex) {
                        LOGGER.error("Error creating default geometry: {}", ex.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Apply hasDefaultGeometry for every Feature with a single hasGeometry
     * property.
     *
     * @param inputFile
     * @param inputLang
     * @param outputFile
     * @param outputLang
     */
    public static final void applyDefaultGeometry(File inputFile, Lang inputLang, File outputFile, Lang outputLang) {

        LOGGER.info("Applying Predicates from File: {} to {} - Started", inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

        Model model = applyDefaultGeometry(inputFile, inputLang);
        //Write the output.
        ConvertData.writeOutputModel(model, outputFile, outputLang, inputFile);

        LOGGER.info("Applying Predicates from File: {} to {} - Completed", inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
    }

    /**
     * Apply hasDefaultGeometry for every Feature with a single hasGeometry
     * property.
     *
     * @param inputFile
     * @param inputLang
     * @return Output model.
     */
    public static final Model applyDefaultGeometry(File inputFile, Lang inputLang) {
        Model model = ModelFactory.createDefaultModel();
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            RDFDataMgr.read(model, inputStream, inputLang);
            applyDefaultGeometry(model);
        } catch (IOException ex) {
            LOGGER.error("Input File IO Exception: {} - {}", inputFile.getAbsolutePath(), ex.getMessage());
        }

        return model;
    }

    /**
     * Apply (to a folder of RDF files) hasDefaultGeometry for every Feature
     * with a single hasGeometry property.
     * <br> Only RDF files should be in the input folder and must all be the
     * same RDF * language.
     *
     * @param inputFolder
     * @param inputLang
     * @param outputFolder
     * @param outputLang
     */
    public static final void applyDefaultGeometryFolder(File inputFolder, Lang inputLang, File outputFolder, Lang outputLang) {

        LOGGER.info("Applying Predicates from Folder {} to {} - Started", inputFolder.getAbsolutePath(), outputFolder.getAbsolutePath());
        if (inputFolder.exists()) {
            File[] inputFiles = inputFolder.listFiles();

            if (inputFiles.length > 0) {
                outputFolder.mkdir();

                for (File inputFile : inputFiles) {
                    File outputFile = new File(outputFolder, inputFile.getName());
                    try {
                        applyDefaultGeometry(inputFile, inputLang, outputFile, outputLang);
                    } catch (Exception ex) {
                        LOGGER.error("{} for input {}. The output file {} may not be created.", ex.getMessage(), inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
                    }
                }

            } else {
                LOGGER.warn("{} is empty. {} is not created.", inputFolder.getAbsolutePath(), outputFolder.getAbsolutePath());
            }

        } else {
            LOGGER.warn("{} does not exist. {} is not created.", inputFolder.getAbsolutePath(), outputFolder.getAbsolutePath());
        }
        LOGGER.info("Applying Predicates from Folder {} to {} - Completed", inputFolder.getAbsolutePath(), outputFolder.getAbsolutePath());
    }

    /**
     * Load GeoSPARQL v1.0 (corrected version) as a Model.
     *
     * @return Output model.
     */
    public static final Model loadGeoSPARQLSchema() {
        Model geosparqlSchema = ModelFactory.createDefaultModel();
        InputStream inputStream = GeoSPARQLOperations.class.getClassLoader().getResourceAsStream(GEOSPARQL_SCHEMA_FILE);
        RDFDataMgr.read(geosparqlSchema, inputStream, Lang.RDFXML);

        return geosparqlSchema;
    }

    /**
     * Apply GeoSPARQL inferencing using GeoSPARPQL v1.0 (corrected version) and
     * RDFS reasoner.
     *
     * @param dataset
     */
    public static final void applyInferencing(Dataset dataset) {
        Model geosparqlSchema = loadGeoSPARQLSchema();
        applyInferencing(geosparqlSchema, dataset);
    }

    /**
     * Apply GeoSPARQL inferencing using schema model and RDFS reasoner.
     *
     * @param geosparqlSchema
     * @param dataset
     */
    public static final void applyInferencing(Model geosparqlSchema, Dataset dataset) {

        LOGGER.info("Applying GeoSPARQL Schema - Started");

        try {
            //Default Model
            dataset.begin(ReadWrite.WRITE);
            Model defaultModel = dataset.getDefaultModel();
            applyInferencing(geosparqlSchema, defaultModel, "default");

            //Named Models
            Iterator<String> graphNames = dataset.listNames();
            while (graphNames.hasNext()) {
                String graphName = graphNames.next();
                Model namedModel = dataset.getNamedModel(graphName);
                applyInferencing(geosparqlSchema, namedModel, graphName);
            }

            dataset.commit();
            LOGGER.info("Applying GeoSPARQL Schema - Completed");
        } catch (Exception ex) {
            LOGGER.error("Inferencing Error: {}", ex.getMessage());
        } finally {
            dataset.end();
        }
    }

    /**
     * Apply GeoSPARQL inferencing using GeoSPARPQL v1.0 (corrected version) and
     * RDFS reasoner.
     *
     * @param dataModel
     */
    public static final void applyInferencing(Model dataModel) {
        Model geosparqlSchema = loadGeoSPARQLSchema();
        GeoSPARQLOperations.applyInferencing(geosparqlSchema, dataModel);
    }

    /**
     * Apply GeoSPARQL inferencing using schema model and RDFS reasoner.
     *
     * @param geosparqlSchema
     * @param dataModel
     */
    public static final void applyInferencing(Model geosparqlSchema, Model dataModel) {
        applyInferencing(geosparqlSchema, dataModel, "unnamed");
    }

    /**
     * Apply GeoSPARQL inferencing using schema model and RDFS reasoner.
     * <br> Graph name supplied for logging purposes only.
     *
     * @param geosparqlSchema
     * @param model
     * @param graphName
     */
    public static final void applyInferencing(Model geosparqlSchema, Model model, String graphName) {
        if (!model.isEmpty()) {
            InfModel infModel = ModelFactory.createRDFSModel(geosparqlSchema, model);
            model.add(infModel);
            LOGGER.info("GeoSPARQL schema applied to graph: {}", graphName);
        } else {
            LOGGER.info("GeoSPARQL schema not applied to empty graph: {}", graphName);
        }
    }

    /**
     * Prepare an empty GeoSPARQL model with RDFS reasoning.
     * <br> In-memory indexing applied by default.
     * <br> This can be changed by calling GeoSPARQLConfig methods.
     *
     * @return Output model.
     */
    public static final InfModel prepare() {
        return prepareRDFS(ModelFactory.createDefaultModel());
    }

    /**
     * Prepare a GeoSPARQL model from an existing model with RDFS reasoning.
     * <br> In-memory indexing applied by default.
     * <br> This can be changed by calling GeoSPARQLConfig methods.
     *
     * @param model
     * @return Output model.
     */
    public static final InfModel prepareRDFS(Model model) {
        return prepare(model, ReasonerRegistry.getRDFSReasoner());
    }

    /**
     * Prepare a GeoSPARQL model from an existing model with alternative
     * Reasoner, e.g. OWL.
     * <br> In-memory indexing applied by default.
     * <br> This can be changed by calling GeoSPARQLConfig methods.
     *
     * @param model
     * @param reasoner
     * @return Output model.
     */
    public static final InfModel prepare(Model model, Reasoner reasoner) {
        InputStream geosparqlSchemaInputStream = GeoSPARQLOperations.class.getClassLoader().getResourceAsStream(GEOSPARQL_SCHEMA_FILE);
        return prepare(geosparqlSchemaInputStream, model, reasoner);
    }

    /**
     * Prepare a GeoSPARQL model from file with RDFS reasoning.
     * <br> In-memory indexing applied by default.
     * <br> This can be changed by calling GeoSPARQLConfig methods.
     *
     * @param inputStream
     * @return Output model.
     */
    public static final InfModel prepareRDFS(InputStream inputStream) {
        return prepare(inputStream, ReasonerRegistry.getRDFSReasoner());
    }

    /**
     * Prepare a GeoSPARQL model from file with alternative Reasoner, e.g. OWL.
     * <br> In-memory indexing applied by default.
     * <br> This can be changed by calling GeoSPARQLConfig methods.
     *
     * @param inputStream
     * @param reasoner
     * @return Output model.
     */
    public static final InfModel prepare(InputStream inputStream, Reasoner reasoner) {
        Model model = ModelFactory.createDefaultModel();
        model.read(inputStream, null);

        return prepare(model, reasoner);
    }

    /**
     * Prepare a model from an existing model with alternative GeoSPARQL schema
     * and Reasoner, e.g. OWL.
     * <br> In-memory indexing applied by default.
     * <br> This can be changed by calling GeoSPARQLConfig methods.
     *
     * @param geosparqlSchemaInputStream
     * @param model
     * @param reasoner
     * @return Output model.
     */
    public static final InfModel prepare(InputStream geosparqlSchemaInputStream, Model model, Reasoner reasoner) {

        //Register GeoSPARQL functions if required.
        GeoSPARQLConfig.setupMemoryIndex();

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
     * Validate Geometry Literal in Dataset.
     *
     * @param dataset
     * @return Whether dataset is valid.
     */
    public static final boolean validateGeometryLiteral(Dataset dataset) {

        boolean isValid = true;

        LOGGER.info("Validate Geometry Literal - Started");
        //Default Model
        dataset.begin(ReadWrite.READ);
        Model defaultModel = dataset.getDefaultModel();
        GeoSPARQLOperations.validateGeometryLiteral(defaultModel);

        //Named Models
        Iterator<String> graphNames = dataset.listNames();
        while (graphNames.hasNext()) {
            String graphName = graphNames.next();
            Model namedModel = dataset.getNamedModel(graphName);
            boolean isModelValid = GeoSPARQLOperations.validateGeometryLiteral(namedModel);

            if (!isModelValid) {
                isValid = false;
            }
        }

        LOGGER.info("Validate Geometry Literal - Completed");
        dataset.end();

        return isValid;
    }

    /**
     * Validate Geometry Literal in Model.
     *
     * @param model
     * @return Whether model is valid.
     */
    public static final boolean validateGeometryLiteral(Model model) {

        //Get current state of index and switch it off temporarily.
        boolean isIndexActive = GeometryLiteralIndex.isIndexActive();
        GeometryLiteralIndex.setIndexActive(false);

        boolean isValid = true;
        NodeIterator nodeIt = model.listObjectsOfProperty(Geo.HAS_DEFAULT_GEOMETRY_PROP);
        while (nodeIt.hasNext()) {
            RDFNode node = nodeIt.nextNode();

            try {
                Literal geometryLiteral = node.asLiteral();
                GeometryWrapper.extract(geometryLiteral);
            } catch (DatatypeFormatException ex) {
                //Error messages should already have been issued. Catch exception so can continue on whole dataset.
                isValid = false;
            }
        }

        //Switch index back on if it was on.
        GeometryLiteralIndex.setIndexActive(isIndexActive);

        return isValid;
    }

    /**
     * Convert Geo Predicates (Lat/Lon) in Dataset to WKT Geometry Literal.
     *
     * @param dataset
     *
     */
    public static final void convertGeoPredicates(Dataset dataset) {

        LOGGER.info("Convert Geo Predicates - Started");
        //Default Model
        dataset.begin(ReadWrite.WRITE);
        Model defaultModel = dataset.getDefaultModel();
        convertGeoPredicates(defaultModel);

        //Named Models
        Iterator<String> graphNames = dataset.listNames();
        while (graphNames.hasNext()) {
            String graphName = graphNames.next();
            Model namedModel = dataset.getNamedModel(graphName);
            convertGeoPredicates(namedModel);
        }

        LOGGER.info("Convert Geo Predicates - Completed");
        dataset.commit();
        dataset.end();
    }

    public static final void convertGeoPredicates(Model model) {

        if (model.containsResource(SpatialExtension.GEO_LAT_PROP)) {

            ResIterator resIt = model.listSubjectsWithProperty(SpatialExtension.GEO_LAT_PROP);
            while (resIt.hasNext()) {
                Resource feature = resIt.nextResource();
                if (feature.hasProperty(SpatialExtension.GEO_LONG_PROP) && feature.hasProperty(SpatialExtension.GEO_LAT_PROP)) {

                    //Create a GeometryLiteral from Lat/Lon
                    Literal lat = feature.getProperty(SpatialExtension.GEO_LAT_PROP).getLiteral();
                    Literal lon = feature.getProperty(SpatialExtension.GEO_LONG_PROP).getLiteral();
                    try {
                        Literal latLonPoint = ConvertLatLon.toLiteral(lat.getFloat(), lon.getFloat());

                        //Create a Geometry - re-use Feature if a URI or build a URI for blank node.
                        String geometryURI;
                        if (feature.isURIResource()) {
                            geometryURI = feature.getURI() + "-Geom-" + UUID.randomUUID().toString();
                        } else {
                            geometryURI = GEO_URI + "Geom-" + UUID.randomUUID().toString();
                        }
                        Resource geometry = ResourceFactory.createResource(geometryURI);

                        //Add Geometry to Feature and GeometryLiteral to Geometry.
                        feature.addProperty(Geo.HAS_GEOMETRY_PROP, geometry);
                        geometry.addLiteral(Geo.HAS_SERIALIZATION_PROP, latLonPoint);
                    } catch (DatatypeFormatException ex) {
                        LOGGER.error("Feature: {} has geo lat/lon out of bounds. Lat: {}, Lon: {}", feature, lat, lon);
                    }
                }
            }
        }

    }

}
