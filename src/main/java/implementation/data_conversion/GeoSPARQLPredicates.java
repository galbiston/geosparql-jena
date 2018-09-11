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
package implementation.data_conversion;

import static implementation.data_conversion.ConvertData.writeOutputModel;
import implementation.vocabulary.Geo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GeoSPARQLPredicates {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Infer and add GeoSPARQL predicates to the model. geo:hasDefaultGeometry,
     * geo:hasGeometry, geo:asWKT, geo:asGML. TODO - only geo:hasDefaultGeometry
     * applied based on single geo:hasGeometry.
     *
     * @param model
     */
    public static final void apply(Model model) {

        //TODO - only applying Assumption1 as that is needed in test data. Ideally insert update method would be best.
        //Assumption 1: that each geo:Feature has one geo:hasGeometry relationship so that geo:hasDefaultGeometry only exists once for each.
        //Assumption 2: that Feature predicate1 Thing and Thing predicate2 (wktLiteral||gmlLiteral) makes Thing a geo:Geometry.
        //Find the gmlLiteral and wktLiteral datatypes in the data. Then find
        try {
            //String insertString = QueryLoader.readResource("sparql_query/InsertDatatypeSubProperty.spl");
            //UpdateAction.parseExecute(insertString, model);

            ResIterator resIt = model.listResourcesWithProperty(RDFS.subPropertyOf, Geo.HAS_GEOMETRY_PROP);
            while (resIt.hasNext()) {
                Resource res = resIt.nextResource();
                res.addProperty(RDFS.subPropertyOf, Geo.HAS_DEFAULT_GEOMETRY_PROP);
            }

        } catch (Exception ex) {
            LOGGER.error("Inserting GeoSPARQL predicates error: {}", ex.getMessage());
        }
    }

    public static final void applyFile(File inputFile, Lang inputLang, File outputFile, Lang outputLang) {

        LOGGER.info("Applying Predicates from File: {} to {} - Started", inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

        Model model = ModelFactory.createDefaultModel();
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            RDFDataMgr.read(model, inputStream, inputLang);
            apply(model);
            //Write the output.
            writeOutputModel(model, outputFile, outputLang, inputFile);
        } catch (IOException ex) {
            LOGGER.error("Input File IO Exception: {} - {}", inputFile.getAbsolutePath(), ex.getMessage());
        }

        LOGGER.info("Applying Predicates from File: {} to {} - Completed", inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

    }

    /**
     * Only RDF files should be in the input folder and must all be the same RDF
     * language.
     *
     * @param inputFolder
     * @param inputLang
     * @param outputFolder
     * @param outputLang
     */
    public static final void convertFolder(File inputFolder, Lang inputLang, File outputFolder, Lang outputLang) {

        LOGGER.info("Applying Predicates from Folder {} to {} - Started", inputFolder.getAbsolutePath(), outputFolder.getAbsolutePath());
        if (inputFolder.exists()) {
            File[] inputFiles = inputFolder.listFiles();

            if (inputFiles.length > 0) {
                outputFolder.mkdir();

                for (File inputFile : inputFiles) {
                    File outputFile = new File(outputFolder, inputFile.getName());
                    try {
                        applyFile(inputFile, inputLang, outputFile, outputLang);
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

}
