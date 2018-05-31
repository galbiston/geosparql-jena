/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.data_conversion;

import implementation.GeoSPARQLSupport;
import implementation.datatype.WKTDatatype;
import implementation.vocabulary.Geo;
import implementation.vocabulary.GeoSPARQL_URI;
import implementation.vocabulary.Other_URI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Random;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
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

        GeoSPARQLSupport.noIndex();
        Model model = ModelFactory.createDefaultModel();
        Model geosparqlSchema = RDFDataMgr.loadModel(geosparpqlSchema.getAbsolutePath());
        InfModel infModel = ModelFactory.createRDFSModel(geosparqlSchema, model);
        Random random = new Random();

        for (int i = 0; i < tripleCount; i++) {

            //Feature hasDefaultGeometry Geometry .
            Resource feature = ResourceFactory.createResource(FEATURE_URI_BASE + i);
            Resource geometry = ResourceFactory.createResource(GEOMETRY_URI_BASE + i);
            infModel.add(feature, Geo.HAS_DEFAULT_GEOMETRY_PROP, geometry);

            //Geometry hasSerialization GeometryLiteral .
            Integer spaceLimitInt = spaceLimit.intValue();
            String lineString = "LINESTRING(" + random.nextInt(spaceLimitInt) + " " + random.nextInt(spaceLimitInt) + ", " + random.nextInt(spaceLimitInt) + " " + random.nextInt(spaceLimitInt) + ")";
            Literal geometryLiteral = ResourceFactory.createTypedLiteral(lineString, WKTDatatype.INSTANCE);
            infModel.add(geometry, Geo.HAS_SERIALIZATION_PROP, geometryLiteral);
            if (i % 1000 == 0) {
                LOGGER.info("Created Triple: {} - {} {} {}", i, feature, Geo.HAS_DEFAULT_GEOMETRY_PROP, geometry);
                LOGGER.info("Created Triple: {} - {} {} {}", i, geometry, Geo.HAS_SERIALIZATION_PROP, geometryLiteral);
            }
        }

        Dataset dataset = TDBFactory.createDataset(tdbFolder.getAbsolutePath());
        dataset.begin(ReadWrite.WRITE);
        Model defaultModel = dataset.getDefaultModel();
        defaultModel.add(infModel);
        dataset.commit();
        dataset.end();
        dataset.close();
        TDBFactory.release(dataset);
        if (isSerialise) {
            serialiseTDB(tdbFolder, Lang.TTL);
        }
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
        Model defaultModel = dataset.getDefaultModel();

        //Set namespace prefixes on the model.
        HashMap<String, String> prefixes = new HashMap<>();
        prefixes.putAll(GeoSPARQL_URI.getPrefixes());
        prefixes.putAll(Other_URI.getPrefixes());
        prefixes.put("ex", URI_BASE);
        defaultModel.setNsPrefixes(prefixes);

        //Output the model.
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            RDFDataMgr.write(out, defaultModel, rdfLang);
        } catch (IOException ex) {
            LOGGER.error("IOException: {} - {}", ex.getMessage(), outputFile.getAbsolutePath());
        }
        LOGGER.info("----------Serialising: {} to {} Completed----------", tdbFolder, outputFile);
    }

}
