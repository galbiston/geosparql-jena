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
package io.github.galbiston.geosparql_jena.implementation.data_conversion;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.GeoSPARQL_URI;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Other_URI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class RandomData {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String URI_BASE = "http://example.org/randomLineString#";
    public static final String FEATURE_URI_BASE = URI_BASE + "Feature";
    public static final String GEOMETRY_URI_BASE = URI_BASE + "Geometry";

    public static void generateLineStrings(Double spaceLimit, Integer tripleCount, File tdbFolder, File geosparpqlSchema, Boolean isSerialise) {

        if (tdbFolder.exists()) {
            if (tdbFolder.isDirectory() && tdbFolder.listFiles().length != 0) {
                LOGGER.error("Target TDB folder is not empty: {}", tdbFolder);
                throw new AssertionError();
            } else if (!tdbFolder.isDirectory()) {
                LOGGER.error("Target TDB folder is a file: {}", tdbFolder);
                throw new AssertionError();
            }
        }

        GeoSPARQLConfig.setupNoIndex();
        Model model = ModelFactory.createDefaultModel();
        Model geosparqlSchema = RDFDataMgr.loadModel(geosparpqlSchema.getAbsolutePath());
        InfModel infModel = ModelFactory.createRDFSModel(geosparqlSchema, model);

        //Set namespace prefixes on the model.
        setNSPrefixes(infModel);

        Random random = new Random();

        for (int i = 0; i < tripleCount; i++) {

            //Feature hasDefaultGeometry Geometry .
            Resource feature = ResourceFactory.createResource(FEATURE_URI_BASE + i);
            Resource geometry = ResourceFactory.createResource(GEOMETRY_URI_BASE + i);
            infModel.add(feature, Geo.HAS_DEFAULT_GEOMETRY_PROP, geometry);

            //Geometry hasSerialization GeometryLiteral .
            Double minX = random.nextDouble() * spaceLimit;
            Double minY = random.nextDouble() * spaceLimit;
            Double maxX = random.nextDouble() * spaceLimit;
            Double maxY = random.nextDouble() * spaceLimit;

            Literal geometryLiteral = WKTCreation.createLineString(minX, minY, maxX, maxY);
            infModel.add(geometry, Geo.HAS_SERIALIZATION_PROP, geometryLiteral);
            int tripleProgress = i + 1;
            if (tripleProgress % 1000 == 0 || tripleProgress == tripleCount) {
                LOGGER.info("Created Triple: {} - {} {} {}", tripleProgress, feature, Geo.HAS_DEFAULT_GEOMETRY_PROP, geometry);
                LOGGER.info("Created Triple: {} - {} {} {}", tripleProgress, geometry, Geo.HAS_SERIALIZATION_PROP, geometryLiteral);
            }
        }
        writeTDB(infModel, tdbFolder);
        checkUniqueGeometryLiterals(tdbFolder);
        if (isSerialise) {
            serialiseTDB(tdbFolder, Lang.TTL);
        }
    }

    private static void setNSPrefixes(Model model) {
        HashMap<String, String> prefixes = new HashMap<>();
        prefixes.putAll(GeoSPARQL_URI.getPrefixes());
        prefixes.putAll(Other_URI.getPrefixes());
        prefixes.put("ex", URI_BASE);
        model.setNsPrefixes(prefixes);
    }

    public static final void writeTDB(Model model, File tdbFolder) {
        LOGGER.info("----------Writing to TDB: {} Started----------", tdbFolder);
        Dataset dataset = TDBFactory.createDataset(tdbFolder.getAbsolutePath());
        dataset.begin(ReadWrite.WRITE);
        Model defaultModel = dataset.getDefaultModel();
        defaultModel.add(model);
        dataset.commit();
        dataset.end();
        dataset.close();
        TDBFactory.release(dataset);
        LOGGER.info("----------Writing to TDB: {} Completed----------", tdbFolder);
    }

    public static final void serialiseTDB(File tdbFolder, Lang rdfLang) {

        String tdbFolderParent = tdbFolder.getParent();
        if (tdbFolderParent == null) {
            tdbFolderParent = ".";
        }
        String fileExtension;
        if (rdfLang.getFileExtensions().isEmpty()) {
            fileExtension = "rdf";
        } else {
            fileExtension = rdfLang.getFileExtensions().get(0);
        }
        File outputFile = new File(tdbFolderParent, tdbFolder.getName() + "." + fileExtension);
        LOGGER.info("----------Serialising: {} to {} Started----------", tdbFolder, outputFile);
        Dataset dataset = TDBFactory.createDataset(tdbFolder.getAbsolutePath());
        dataset.begin(ReadWrite.READ);
        Model defaultModel = dataset.getDefaultModel();

        //Output the model.
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            RDFDataMgr.write(out, defaultModel, rdfLang);
        } catch (IOException ex) {
            LOGGER.error("IOException: {} - {}", ex.getMessage(), outputFile.getAbsolutePath());
        }
        dataset.end();
        dataset.close();
        TDBFactory.release(dataset);
        LOGGER.info("----------Serialising: {} to {} Completed----------", tdbFolder, outputFile);
    }

    public static final void checkUniqueGeometryLiterals(File tdbFolder) {
        LOGGER.info("----------Checking Geometry Literal count in TDB: {} Started----------", tdbFolder);
        Dataset dataset = TDBFactory.createDataset(tdbFolder.getAbsolutePath());

        dataset.begin(ReadWrite.READ);
        Model defaultModel = dataset.getDefaultModel();
        countGeometryLiterals(defaultModel, "Default Model");
        Iterator<String> iterator = dataset.listNames();
        while (iterator.hasNext()) {
            String graphName = iterator.next();
            Model model = dataset.getNamedModel(graphName);
            countGeometryLiterals(model, graphName);
        }

        dataset.end();
        dataset.close();
        TDBFactory.release(dataset);
        LOGGER.info("----------Checking Geometry Literal count in TDB: {} Completed----------", tdbFolder);
    }

    private static void countGeometryLiterals(Model model, String graphName) {
        Set<String> literalStrings = new TreeSet<>();
        Iterator<Statement> iterator = model.listStatements(null, Geo.HAS_SERIALIZATION_PROP, (RDFNode) null);
        int count = 0;
        while (iterator.hasNext()) {
            Statement st = iterator.next();
            String literalString = st.getLiteral().getString();
            literalStrings.add(literalString);
            count++;
        }
        LOGGER.info("Graph: {} has {} unique out of {} Geometry Literals.", graphName, literalStrings.size(), count);

    }

}
