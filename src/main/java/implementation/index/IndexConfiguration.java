/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import java.io.File;
import java.lang.invoke.MethodHandles;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class IndexConfiguration {

    /*
     * Indexing and Registry Sizes
     */
    public static final Integer CRS_REGISTRY_MAX_SIZE = 20;
    public static final Integer UNITS_REGISTRY_MAX_SIZE = CRS_REGISTRY_MAX_SIZE;
    public static final Integer MATH_TRANSFORM_REGISTRY_MAX_SIZE = CRS_REGISTRY_MAX_SIZE;
    public static final Integer GEOMETRY_LITERAL_INDEX_MAX_SIZE = 100000;
    public static final Integer GEOMETRY_TRANSFORM_INDEX_MAX_SIZE = 100000;
    public static final Integer QUERY_REWRITE_INDEX_MAX_SIZE = 100000;

    /*
     * Index Storage Filenames
     */
    public static final String CRS_REGISTRY_FILENAME = "geosparql-CRS.registry";
    public static final String UNITS_REGISTRY_FILENAME = "geosparql-Units.registry";
    public static final String MATH_TRANSFORM_REGISTRY_FILENAME = "geosparql-MathTransform.registry";
    public static final String GEOMETRY_TRANSFORM_INDEX_FILENAME = "geosparql-GeometryTransform.index";
    public static final String GEOMETRY_LITERAL_INDEX_FILENAME = "geosparql-GeometryLiteral.index";
    public static final String QUERY_REWRITE_INDEX_FILENAME = "geosparql-QueryRewrite.index";

    /*
     * Index Configuration Parameters
     */
    private static File indexStorageFolder = null;
    private static Thread shutdownStorageThread = null;
    private static IndexOption indexOptionEnum = IndexOption.MEMORY;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final void setConfig(IndexOption indexOption, File indexFolder) {
        indexOptionEnum = indexOption;

        switch (indexOptionEnum) {
            case MEMORY:
                IndexConfiguration.setupMemoryIndex(indexFolder);
                break;
            case TDB:
                IndexConfiguration.setupTDBIndex(indexFolder);
                break;
            default:
                IndexConfiguration.setupNoIndex();
        }
    }

    public static final void clearAllIndexesAndRegistries() {
        GeometryLiteralIndex.clear();
        GeometryTransformIndex.clear();
        QueryRewriteIndex.clear();
        CRSRegistry.clearAll();
        MathTransformRegistry.clear();
    }

    private static void removeMemoryIndexStorageThread() {
        if (shutdownStorageThread != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownStorageThread);
        }
    }

    public static final void setIndexMaxSize(Integer geometryLiteralIndexMaxSize, Integer geometryTransformIndexMaxSize, Integer queryRewriteIndexMaxSize) {
        GeometryLiteralIndex.setMaxSize(geometryLiteralIndexMaxSize);
        GeometryTransformIndex.setMaxSize(geometryTransformIndexMaxSize);
        QueryRewriteIndex.setMaxSize(queryRewriteIndexMaxSize);
    }

    /**
     * Override the default maximum sizes of registries.
     * <br>Registries are small but contain frequently re-used or generally
     * useful data.
     * <br>Any existing in-memory registries will be emptied.
     *
     * @param crsRegistryMaxSize - default max size: 20
     * @param unitsRegistryMaxSize - default max size: 20
     * @param mathTransformRegistryMaxSize - default max size: 20
     */
    public static final void setRegistryMaxSize(Integer crsRegistryMaxSize, Integer unitsRegistryMaxSize, Integer mathTransformRegistryMaxSize) {
        CRSRegistry.setCRSRegistryMaxSize(crsRegistryMaxSize);
        CRSRegistry.setUnitsRegistryMaxSize(unitsRegistryMaxSize);
        MathTransformRegistry.setRegistryMaxSize(mathTransformRegistryMaxSize);
    }

    private static void loadMemoryIndexes(File indexFolder) {
        //CRS Registry
        File crsRegistryFile = new File(indexFolder, CRS_REGISTRY_FILENAME);
        if (crsRegistryFile.exists()) {
            CRSRegistry.readCRSRegistry(crsRegistryFile);
        }
        //Units Registry
        File unitsRegistryFile = new File(indexFolder, UNITS_REGISTRY_FILENAME);
        if (unitsRegistryFile.exists()) {
            CRSRegistry.readUnitsRegistry(unitsRegistryFile);
        }
        //Math Transform Registry
        File mathTransformRegistryFile = new File(indexFolder, MATH_TRANSFORM_REGISTRY_FILENAME);
        if (mathTransformRegistryFile.exists()) {
            MathTransformRegistry.read(mathTransformRegistryFile);
        }
        //Geometry Transform Index
        File geometryTransformIndexFile = new File(indexFolder, GEOMETRY_TRANSFORM_INDEX_FILENAME);
        if (geometryTransformIndexFile.exists()) {
            GeometryTransformIndex.read(geometryTransformIndexFile);
        }
        //Geometry Literal Index
        File geometryLiteralIndexFile = new File(indexFolder, GEOMETRY_LITERAL_INDEX_FILENAME);
        if (geometryLiteralIndexFile.exists()) {
            GeometryLiteralIndex.read(geometryLiteralIndexFile);
        }
        //Query Rewrite Index
        File queryRewriteIndexFile = new File(indexFolder, QUERY_REWRITE_INDEX_FILENAME);
        if (queryRewriteIndexFile.exists()) {
            QueryRewriteIndex.read(queryRewriteIndexFile);
        }
    }

    public static void setupTDBIndex(File indexFolder) {
        removeMemoryIndexStorageThread();
        zeroMemoryIndexMaxSize();
    }

    public static void defaultMemoryIndexMaxSize() {
        GeometryLiteralIndex.setMaxSize(GEOMETRY_LITERAL_INDEX_MAX_SIZE);
        GeometryTransformIndex.setMaxSize(GEOMETRY_TRANSFORM_INDEX_MAX_SIZE);
        QueryRewriteIndex.setMaxSize(QUERY_REWRITE_INDEX_MAX_SIZE);
    }

    public static void setupNoIndex() {
        removeMemoryIndexStorageThread();
        zeroMemoryIndexMaxSize();
        if (indexStorageFolder != null) {
            FileUtils.deleteQuietly(indexStorageFolder);
            indexStorageFolder = null;
        }
    }

    public static void setupMemoryIndex(File indexFolder) {
        if (indexFolder != null) {
            //Only load and setup storage once per filename.
            if (indexStorageFolder == null | !indexFolder.equals(indexStorageFolder)) {
                loadMemoryIndexes(indexFolder);
                storeMemoryIndexesAtShutdown(indexFolder);
                indexStorageFolder = indexFolder;
            }
        }
    }

    private static void storeMemoryIndexesAtShutdown(File indexFolder) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                //CRS Registry
                File crsRegistryFile = new File(indexFolder, CRS_REGISTRY_FILENAME);
                CRSRegistry.writeCRSRegistry(crsRegistryFile);
                //Units Registry
                File unitsRegistryFile = new File(indexFolder, UNITS_REGISTRY_FILENAME);
                CRSRegistry.writeUnitsRegistry(unitsRegistryFile);
                //Math Transform Registry
                File mathTransformRegistryFile = new File(indexFolder, MATH_TRANSFORM_REGISTRY_FILENAME);
                MathTransformRegistry.write(mathTransformRegistryFile);
                //Geometry Transform Index
                File geometryTransformIndexFile = new File(indexFolder, GEOMETRY_TRANSFORM_INDEX_FILENAME);
                GeometryTransformIndex.write(geometryTransformIndexFile);
                //Geometry Literal Index
                File geometryLiteralIndexFile = new File(indexFolder, GEOMETRY_LITERAL_INDEX_FILENAME);
                GeometryLiteralIndex.write(geometryLiteralIndexFile);
                //Query Rewrite Index
                File queryRewriteIndex = new File(indexFolder, QUERY_REWRITE_INDEX_FILENAME);
                QueryRewriteIndex.write(queryRewriteIndex);
            }
        };
        Runtime.getRuntime().addShutdownHook(thread);
        //Remove any previous shutdown storage thread.
        removeMemoryIndexStorageThread();
        //Retain the thread in case called again.
        shutdownStorageThread = thread;
    }

    private static void zeroMemoryIndexMaxSize() {
        GeometryLiteralIndex.setMaxSize(0);
        GeometryTransformIndex.setMaxSize(0);
        QueryRewriteIndex.setMaxSize(0);
    }

    public static final IndexOption getIndexOption() {
        return indexOptionEnum;
    }

}
