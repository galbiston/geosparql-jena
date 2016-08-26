/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.io.File;
import java.io.IOException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.spatial.EntityDefinition;
import org.apache.jena.query.spatial.SpatialDatasetFactory;
import org.apache.jena.query.spatial.SpatialIndexLucene;
import org.apache.jena.query.spatial.SpatialQuery;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.util.QueryExecUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Prefixes;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class SpatialIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpatialIndex.class);

    private static final String LUCENE_INDEX_PATH = "/Users/haozhechen/NetBeansProjects/GeoSPARQL/src/main/resources/dataset/TDBDatasetWithLuceneSpatialIndex";
    private static final File LUCENE_INDEX_DIR = new File(LUCENE_INDEX_PATH);

    public static void main(String[] args) throws IOException {

        Dataset spatialDataset = initInMemoryDatasetWithLuceneSpatitalIndex(LUCENE_INDEX_DIR);
        loadData(spatialDataset, "/Users/haozhechen/NetBeansProjects/GeoSPARQL/src/main/resources/dataset/itn.rdf");
        queryData(spatialDataset);

        destroy(spatialDataset);

    }

    private static void destroy(Dataset spatialDataset) {

        SpatialIndex index = (SpatialIndex) spatialDataset.getContext().get(SpatialQuery.spatialIndex);
        if (index.getClass().equals(SpatialIndexLucene.class)) {
            deleteOldFiles(LUCENE_INDEX_DIR);

        }
    }

    private static Dataset initInMemoryDatasetWithLuceneSpatitalIndex(File indexDir) throws IOException {
        deleteOldFiles(indexDir);
        indexDir.mkdirs();
        return createDatasetByCode(indexDir);
    }

    private static void emptyAndDeleteDirectory(File dir) {
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File content : contents) {
                if (content.isDirectory()) {
                    emptyAndDeleteDirectory(content);
                } else {
                    content.delete();
                }
            }
        }
        dir.delete();
    }

    private static void deleteOldFiles(File indexDir) {
        if (indexDir.exists()) {
            emptyAndDeleteDirectory(indexDir);
        }
    }

    private static Dataset createDatasetByCode(File indexDir) throws IOException {
        // Base data
        Dataset ds1 = DatasetFactory.create();
        return joinDataset(ds1, indexDir);
    }

    private static Dataset joinDataset(Dataset baseDataset, File indexDir) throws IOException {
        EntityDefinition entDef = new EntityDefinition("entityField", "geoField");

        // you need JTS lib in the classpath to run the examples
        entDef.setSpatialContextFactory(SpatialQuery.JTS_SPATIAL_CONTEXT_FACTORY_CLASS);
        Property asGML = ResourceFactory.createProperty(Vocabulary.GML_URI + "asGML");
        entDef.addWKTPredicate(asGML);

        // Lucene, index in File system.
        Directory dir = FSDirectory.open(indexDir.toPath());

        // Join together into a dataset
        Dataset ds = SpatialDatasetFactory.createLucene(baseDataset, dir, entDef);

        return ds;
    }

    public static void loadData(Dataset spatialDataset, String file) {

        long startTime = System.nanoTime();
        spatialDataset.begin(ReadWrite.WRITE);
        try {
            Model m = spatialDataset.getDefaultModel();
            RDFDataMgr.read(m, file);
            // RDFDataMgr.read(dataset, "D.ttl") ;
            spatialDataset.commit();
        } finally {
            spatialDataset.end();
        }

        long finishTime = System.nanoTime();
        double time = (finishTime - startTime) / 1.0e6;
        LOGGER.info(String.format("Finish loading - %.2fms", time));
    }

    public static void queryData(Dataset spatialDataset) {
        String queryString = "SELECT ?place WHERE{ "
                + "?place gml:asGML ?GML . "
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        spatialDataset.begin(ReadWrite.READ);
        try {
            Query q = QueryFactory.create(query.asQuery());
            QueryExecution qexec = QueryExecutionFactory.create(q, spatialDataset);
            QueryExecUtils.executeQuery(q, qexec);
        } finally {
            spatialDataset.end();
        }

    }
}
