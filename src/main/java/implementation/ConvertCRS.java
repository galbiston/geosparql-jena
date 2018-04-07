/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gerg
 */
public class ConvertCRS {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Converts all geometry literals (WKT or GML) from current CRS to the
     * specified CRS.
     *
     * @param inputFile
     * @param inputLang
     * @param outputFile
     * @param outputLang
     * @param srsURI
     */
    public static final void convertFile(File inputFile, Lang inputLang, File outputFile, Lang outputLang, String srsURI) {

        WKTDatatype wktDatatype = WKTDatatype.THE_WKT_DATATYPE;
        GMLDatatype gmlDatatype = GMLDatatype.THE_GML_DATATYPE;

        Model model = ModelFactory.createDefaultModel();
        try (InputStream inputStream = new FileInputStream(inputFile)) {
            RDFDataMgr.read(model, inputStream, inputLang);
        } catch (IOException ex) {
            LOGGER.error("Input File IO Exception: {} - {}", inputFile, ex.getMessage());
        }

        Model outputModel = ModelFactory.createDefaultModel();
        //Iterate through all statements: convert geometry literals and just add the rest.
        Iterator<Statement> statementIt = model.listStatements();
        while (statementIt.hasNext()) {
            Statement statement = statementIt.next();
            Literal literal = statement.getLiteral();

            RDFDatatype datatype = literal.getDatatype();

            //Check whether a supported geometry literal.
            if (datatype.equals(wktDatatype) | datatype.equals(gmlDatatype)) {
                GeometryWrapper originalGeom = GeometryWrapper.extract(literal);
                GeometryWrapper convertedGeom;
                try {
                    convertedGeom = originalGeom.convertCRS(srsURI);
                } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                    LOGGER.error("CRS Conversion Exception: {} - Literal: {}, Output SRS URI: {}. Reusing original literal for output.", ex.getMessage(), literal, srsURI);
                    convertedGeom = originalGeom;
                }
                Statement outputStatement = ResourceFactory.createStatement(statement.getSubject(), statement.getPredicate(), convertedGeom.asLiteral());
                outputModel.add(outputStatement);
            } else {
                //Not a statement of interest so store for output.
                outputModel.add(statement);
            }
        }

        //Write the output.
        if (!outputModel.isEmpty()) {
            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                RDFDataMgr.write(outputStream, outputModel, outputLang);
            } catch (IOException ex) {
                LOGGER.error("Output File IO Exception: {} - {}", outputFile, ex.getMessage());
            }
        }

    }

}
