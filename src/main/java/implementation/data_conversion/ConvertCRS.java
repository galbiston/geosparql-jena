/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.data_conversion;

import implementation.GeometryWrapper;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.support.GeoSerialisationEnum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.datatypes.BaseDatatype;
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
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gerg
 */
public class ConvertCRS {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String WKT_DATATYPE_URI = WKTDatatype.THE_WKT_DATATYPE.getURI();
    private static final String GML_DATATYPE_URI = GMLDatatype.THE_GML_DATATYPE.getURI();

    public static final Model convert(Model inputModel, String outputSrsURI) {
        //Iterate through all statements: convert geometry literals and just add the rest.
        Model outputModel = ModelFactory.createDefaultModel();
        Iterator<Statement> statementIt = inputModel.listStatements();
        while (statementIt.hasNext()) {
            Statement statement = statementIt.next();
            RDFNode object = statement.getObject();
            if (object.isLiteral()) {
                handleLiteral(statement, outputModel, outputSrsURI);
            } else {
                //Not a statement of interest so store for output.
                outputModel.add(statement);
            }
        }
        return outputModel;
    }

    private static void handleLiteral(Statement statement, Model outputModel, String outputSrsURI) {
        Literal literal = statement.getLiteral();

        RDFDatatype datatype = literal.getDatatype();
        String datatypeURI = datatype.getURI();
        //Check whether a supported geometry literal.
        if (datatypeURI.equals(WKT_DATATYPE_URI) | datatypeURI.equals(GML_DATATYPE_URI)) {
            GeometryWrapper originalGeom = GeometryWrapper.extract(literal);
            GeometryWrapper convertedGeom;
            try {
                convertedGeom = originalGeom.convertCRS(outputSrsURI);
            } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                LOGGER.error("CRS Conversion Exception: {} - Literal: {}, Output SRS URI: {}. Reusing original literal for output.", ex.getMessage(), literal, outputSrsURI);
                convertedGeom = originalGeom;
            }
            Statement outputStatement = ResourceFactory.createStatement(statement.getSubject(), statement.getPredicate(), convertedGeom.asLiteral());
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

        LOGGER.info("Converting File: {} to {} in srs URI: {} - Started", inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), outputSrsURI);

        Model inputModel = ModelFactory.createDefaultModel();
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            RDFDataMgr.read(inputModel, inputStream, inputLang);
        } catch (IOException ex) {
            LOGGER.error("Input File IO Exception: {} - {}", inputFile.getAbsolutePath(), ex.getMessage());
        }

        Model outputModel = convert(inputModel, outputSrsURI);

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

        LOGGER.info("Converting Folder {} to {} in srs URI: {} - Started", inputFolder.getAbsolutePath(), outputFolder.getAbsolutePath(), outputSrsURI);
        if (inputFolder.exists()) {
            File[] inputFiles = inputFolder.listFiles();

            if (inputFiles.length > 0) {
                outputFolder.mkdir();

                for (File inputFile : inputFiles) {
                    File outputFile = new File(outputFolder, inputFile.getName());
                    try {
                        convertFile(inputFile, inputLang, outputFile, outputLang, outputSrsURI);
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
     * @param serialisation
     * @return
     */
    public static final List<String> convertGeometryLiterals(List<String> geometryLiterals, String outputSrsURI, GeoSerialisationEnum serialisation) {

        List<String> outputGeometryLiterals = new ArrayList<>(geometryLiterals.size());

        BaseDatatype datatype;
        switch (serialisation) {
            case GML:
                datatype = GMLDatatype.THE_GML_DATATYPE;
                break;
            case WKT:
                datatype = WKTDatatype.THE_WKT_DATATYPE;
                break;
            default:
                LOGGER.error("Unknown Serialisation: {}", serialisation);
                throw new AssertionError("Unknown Serialisation: " + serialisation);
        }

        for (String geometryLiteral : geometryLiterals) {
            Literal lit = ResourceFactory.createTypedLiteral(geometryLiteral, datatype);
            GeometryWrapper geometryWrapper = GeometryWrapper.extract(lit);
            try {
                GeometryWrapper transformedGeometryWrapper = geometryWrapper.convertCRS(outputSrsURI);
                Literal transformedLit = transformedGeometryWrapper.asLiteral();
                outputGeometryLiterals.add(transformedLit.getLexicalForm());
            } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                LOGGER.error("{} : {} : {}", ex.getMessage(), geometryLiteral, outputSrsURI);
            }
        }

        return outputGeometryLiterals;
    }

    /**
     * Convert a string representation of a geometry literal to another
     * coordinate reference system.
     *
     * @param geometryLiteral
     * @param outputSrsURI Coordinate reference system URI
     * @param serialisation
     * @return
     */
    public static final String convertGeometryLiteral(String geometryLiteral, String outputSrsURI, GeoSerialisationEnum serialisation) {

        BaseDatatype datatype;
        switch (serialisation) {
            case GML:
                datatype = GMLDatatype.THE_GML_DATATYPE;
                break;
            case WKT:
                datatype = WKTDatatype.THE_WKT_DATATYPE;
                break;
            default:
                LOGGER.error("Unknown Serialisation: {}", serialisation);
                throw new AssertionError("Unknown Serialisation: " + serialisation);
        }

        Literal lit = ResourceFactory.createTypedLiteral(geometryLiteral, datatype);
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
