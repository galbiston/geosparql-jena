/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.data_conversion;

import implementation.GeoSPARQLSupport;
import implementation.datatype.WKTDatatype;
import implementation.vocabulary.Geo;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Random;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
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

    public static void createLineStrings(Integer spaceLimit, Integer tripleCount, File tdbFolder, File geosparpqlSchema) {

        GeoSPARQLSupport.noIndex();
        Model model = ModelFactory.createDefaultModel();
        Model geosparqlSchema = RDFDataMgr.loadModel(geosparpqlSchema.getAbsolutePath());
        InfModel infModel = ModelFactory.createRDFSModel(geosparqlSchema, model);
        Random random = new Random();

        Property property = Geo.HAS_SERIALIZATION_PROP;
        for (int i = 0; i < tripleCount; i++) {
            Resource subject = ResourceFactory.createResource("http:example.org#Subject" + i);
            String geometry = "LINESTRING(" + random.nextInt(spaceLimit) + " " + random.nextInt(spaceLimit) + ", " + random.nextInt(spaceLimit) + " " + random.nextInt(spaceLimit) + ")";
            Literal object = ResourceFactory.createTypedLiteral(geometry, WKTDatatype.INSTANCE);
            infModel.add(subject, property, object);
            if (i % 1000 == 0) {
                LOGGER.info("Created Triple: {} - {} {} {}", i, subject, property, object);
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
    }

}
