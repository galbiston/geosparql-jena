/*
 * Copyright 2018 Greg Albiston
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

import implementation.GeoSPARQLSupport;
import implementation.GeometryWrapper;
import implementation.datatype.GeometryDatatype;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class ConvertData {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Convert the input model to the output coordinate reference system.
     *
     * @param inputModel
     * @param outputSrsURI
     * @return
     */
    public static final Model convert(Model inputModel, String outputSrsURI) {
        return convertCRSDatatype(inputModel, outputSrsURI, null);
    }

    /**
     * Convert the input model to the output geometry literal datatype.
     *
     * @param inputModel
     * @param outputDatatype
     * @return
     */
    public static final Model convert(Model inputModel, GeometryDatatype outputDatatype) {
        return convertCRSDatatype(inputModel, null, outputDatatype);
    }

    /**
     * Convert the input model to the output coordinate reference system and
     * geometry literal datatype.
     *
     * @param inputModel
     * @param outputSrsURI
     * @param outputDatatype
     * @return
     */
    public static final Model convert(Model inputModel, String outputSrsURI, GeometryDatatype outputDatatype) {
        return convertCRSDatatype(inputModel, outputSrsURI, outputDatatype);
    }

    private static Model convertCRSDatatype(Model inputModel, String outputSrsURI, GeometryDatatype outputDatatype) {

        if (!GeometryDatatype.check(outputDatatype)) {
            LOGGER.error("Output datatype {} is not a recognised Geometry Literal", outputDatatype);
            return null;
        }

        //Setup CRS registries but without indexing.
        GeoSPARQLSupport.noIndex();

        //Iterate through all statements: convert geometry literals and just add the rest.
        Model outputModel = ModelFactory.createDefaultModel();
        Iterator<Statement> statementIt = inputModel.listStatements();
        while (statementIt.hasNext()) {
            Statement statement = statementIt.next();
            RDFNode object = statement.getObject();
            if (object.isLiteral()) {
                handleLiteral(statement, outputModel, outputSrsURI, outputDatatype);
            } else {
                //Not a statement of interest so store for output.
                outputModel.add(statement);
            }
        }
        return outputModel;
    }

    private static void handleLiteral(Statement statement, Model outputModel, String outputSrsURI, GeometryDatatype outputDatatype) {
        Literal literal = statement.getLiteral();

        RDFDatatype datatype = literal.getDatatype();
        //Check whether a supported geometry literal.
        if (GeometryDatatype.check(datatype)) {
            GeometryWrapper originalGeom = GeometryWrapper.extract(literal);
            GeometryWrapper convertedGeom;
            try {

                if (outputSrsURI != null) {
                    convertedGeom = originalGeom.convertCRS(outputSrsURI);
                } else {
                    convertedGeom = originalGeom;
                }
            } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                LOGGER.error("CRS Conversion Exception: {} - Literal: {}, Output SRS URI: {}. Reusing original literal for output.", ex.getMessage(), literal, outputSrsURI);
                convertedGeom = originalGeom;
            }

            if (outputDatatype == null) {
                outputDatatype = GeometryDatatype.get(datatype);
            }

            Statement outputStatement = ResourceFactory.createStatement(statement.getSubject(), statement.getPredicate(), convertedGeom.asLiteral(outputDatatype));
            outputModel.add(outputStatement);
        } else {
            //Not a statement of interest so store for output.
            outputModel.add(statement);
        }
    }

    /**
     * Converts all geometry literals (WKT or GML) from current CRS to the
     * specified CRS.
     *
     * @param inputFile
     * @param inputLang
     * @param outputFile
     * @param outputLang
     * @param outputSrsURI
     */
    public static final void convertFile(File inputFile, Lang inputLang, File outputFile, Lang outputLang, String outputSrsURI) {
        convertFileCRSDatatype(inputFile, inputLang, outputFile, outputLang, outputSrsURI, null);
    }

    /**
     * Converts file between serialisations.
     *
     * @param inputFile
     * @param inputLang
     * @param outputFile
     * @param outputLang
     */
    public static final void convertFile(File inputFile, Lang inputLang, File outputFile, Lang outputLang) {
        convertFileCRSDatatype(inputFile, inputLang, outputFile, outputLang, null, null);
    }

    /**
     * Converts all geometry literals (WKT or GML) from current CRS to the
     * specified CRS and datatype.
     *
     * @param inputFile
     * @param inputLang
     * @param outputFile
     * @param outputLang
     * @param outputSrsURI
     * @param outputDatatype
     */
    public static final void convertFile(File inputFile, Lang inputLang, File outputFile, Lang outputLang, String outputSrsURI, GeometryDatatype outputDatatype) {
        convertFileCRSDatatype(inputFile, inputLang, outputFile, outputLang, outputSrsURI, outputDatatype);
    }

    /**
     * Converts all geometry literals (WKT or GML) to the specified datatype.
     *
     * @param inputFile
     * @param inputLang
     * @param outputFile
     * @param outputLang
     * @param outputDatatype
     */
    public static final void convertFile(File inputFile, Lang inputLang, File outputFile, Lang outputLang, GeometryDatatype outputDatatype) {
        convertFileCRSDatatype(inputFile, inputLang, outputFile, outputLang, null, outputDatatype);
    }

    private static void convertFileCRSDatatype(File inputFile, Lang inputLang, File outputFile, Lang outputLang, String outputSrsURI, GeometryDatatype outputDatatype) {

        LOGGER.info("Converting File: {} to {} in srs URI: {} - Started", inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), outputSrsURI);

        Model inputModel = ModelFactory.createDefaultModel();
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            RDFDataMgr.read(inputModel, inputStream, inputLang);
        } catch (IOException ex) {
            LOGGER.error("Input File IO Exception: {} - {}", inputFile.getAbsolutePath(), ex.getMessage());
        }

        Model outputModel = convertCRSDatatype(inputModel, outputSrsURI, outputDatatype);

        //Write the output.
        writeOutputModel(outputModel, outputFile, outputLang, inputFile);

        LOGGER.info("Converting File: {} to {} in srs URI: {} - Completed", inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), outputSrsURI);
    }

    protected static void writeOutputModel(Model outputModel, File outputFile, Lang outputLang, File inputFile) {
        if (!outputModel.isEmpty()) {
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                RDFDataMgr.write(outputStream, outputModel, outputLang);
            } catch (IOException ex) {
                LOGGER.error("Output File IO Exception: {} - {}", outputFile.getAbsolutePath(), ex.getMessage());
            }
        } else {
            LOGGER.warn("Output Model is empty for {}: Did not create: {}", inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }
    }

    /**
     * Only RDF files should be in the input folder and must all be the same RDF
     * language.
     *
     * @param inputFolder
     * @param inputLang
     * @param outputFolder
     * @param outputLang
     * @param outputSrsURI
     */
    public static final void convertFolder(File inputFolder, Lang inputLang, File outputFolder, Lang outputLang, String outputSrsURI) {
        convertFolderCRSDatatype(inputFolder, inputLang, outputFolder, outputLang, outputSrsURI, null);
    }

    public static final void convertFolder(File inputFolder, Lang inputLang, File outputFolder, Lang outputLang) {
        convertFolderCRSDatatype(inputFolder, inputLang, outputFolder, outputLang, null, null);
    }

    public static final void convertFolder(File inputFolder, Lang inputLang, File outputFolder, Lang outputLang, String outputSrsURI, GeometryDatatype outputDatatype) {
        convertFolderCRSDatatype(inputFolder, inputLang, outputFolder, outputLang, outputSrsURI, outputDatatype);
    }

    public static final void convertFolder(File inputFolder, Lang inputLang, File outputFolder, Lang outputLang, GeometryDatatype outputDatatype) {
        convertFolderCRSDatatype(inputFolder, inputLang, outputFolder, outputLang, null, outputDatatype);
    }

    private static void convertFolderCRSDatatype(File inputFolder, Lang inputLang, File outputFolder, Lang outputLang, String outputSrsURI, GeometryDatatype outputDatatype) {

        LOGGER.info("Converting Folder {} to {} in srs URI: {} - Started", inputFolder.getAbsolutePath(), outputFolder.getAbsolutePath(), outputSrsURI);
        if (inputFolder.exists()) {
            File[] inputFiles = inputFolder.listFiles();

            if (inputFiles.length > 0) {
                outputFolder.mkdir();

                for (File inputFile : inputFiles) {
                    File outputFile = new File(outputFolder, inputFile.getName());
                    try {
                        convertFile(inputFile, inputLang, outputFile, outputLang, outputSrsURI, outputDatatype);
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
        LOGGER.info("Converting Folder {} to {} in srs URI: {} - Completed", inputFolder.getAbsolutePath(), outputFolder.getAbsolutePath(), outputSrsURI);
    }

    /**
     * Convert a list of strings representation of geometry literals to another
     * coordinate reference system.
     *
     * @param geometryLiterals
     * @param outputSrsURI Coordinate reference system URI
     * @param outputDatatype
     * @return
     */
    public static final List<String> convertGeometryLiterals(List<String> geometryLiterals, String outputSrsURI, GeometryDatatype outputDatatype) {

        List<String> outputGeometryLiterals = new ArrayList<>(geometryLiterals.size());

        for (String geometryLiteral : geometryLiterals) {
            String convertedGeometryLiteral = convertGeometryLiteral(geometryLiteral, outputSrsURI, outputDatatype);
            outputGeometryLiterals.add(convertedGeometryLiteral);
        }

        return outputGeometryLiterals;
    }

    /**
     * Convert a string representation of a geometry literal to another
     * coordinate reference system.
     *
     * @param geometryLiteral
     * @param outputSrsURI Coordinate reference system URI
     * @param outputDatatype
     * @return
     */
    public static final String convertGeometryLiteral(String geometryLiteral, String outputSrsURI, GeometryDatatype outputDatatype) {

        Literal lit = ResourceFactory.createTypedLiteral(geometryLiteral, outputDatatype);
        GeometryWrapper geometryWrapper = GeometryWrapper.extract(lit);
        try {
            GeometryWrapper transformedGeometryWrapper = geometryWrapper.convertCRS(outputSrsURI);
            Literal transformedLit = transformedGeometryWrapper.asLiteral();
            return transformedLit.getLexicalForm();
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("{} : {} : {}", ex.getMessage(), geometryLiteral, outputSrsURI);
            return null;
        }
    }

}
