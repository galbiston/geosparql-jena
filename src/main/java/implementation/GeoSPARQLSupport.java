/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import geof.topological.RelateFF;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.function_registry.Egenhofer;
import implementation.function_registry.GeometryProperty;
import implementation.function_registry.NonTopological;
import implementation.function_registry.RCC8;
import implementation.function_registry.Relate;
import implementation.function_registry.SimpleFeatures;
import implementation.index.CRSRegistry;
import implementation.index.GeometryLiteralIndex;
import implementation.index.GeometryTransformIndex;
import implementation.index.IndexOption;
import implementation.index.MathTransformRegistry;
import implementation.vocabulary.Geo;
import java.io.File;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 *
 */
public class GeoSPARQLSupport {

    /**
     * Indexing and Registry Sizes
     */
    public static final Integer CRS_REGISTRY_MAX_SIZE = 20;
    public static final Integer UNITS_REGISTRY_MAX_SIZE = CRS_REGISTRY_MAX_SIZE;
    public static final Integer MATH_TRANSFORM_REGISTRY_MAX_SIZE = CRS_REGISTRY_MAX_SIZE;
    public static final Integer GEOMETRY_TRANSFORM_INDEX_MAX_SIZE = 100000;
    public static final Integer GEOMETRY_LITERAL_INDEX_MAX_SIZE = 100000;

    public static final String CRS_REGISTRY_FILENAME = "geosparql-CRS.registry";
    public static final String UNITS_REGISTRY_FILENAME = "geosparql-Units.registry";
    public static final String MATH_TRANSFORM_REGISTRY_FILENAME = "geosparql-MathTransform.registry";
    public static final String GEOMETRY_TRANSFORM_INDEX_FILENAME = "geosparql-GeometryTransform.index";
    public static final String GEOMETRY_LITERAL_INDEX_FILENAME = "geosparql-GeometryLiteral.index";

    /**
     * GeoSPARQL schema
     */
    private static final String GEOSPARQL_SCHEMA = "schema/geosparql_vocab_all.rdf";
    private static Boolean isFunctionsRegistered = false;
    private static File indexStorageFolder = null;
    private static Thread shutdownStorageThread = null;
    private static IndexOption indexOptionEnum = IndexOption.MEMORY;

    /**
     * Prepare an empty GeoSPARQL model with RDFS reasoning.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling loadFunctions methods.
     *
     * @return
     */
    public static InfModel prepare() {
        return GeoSPARQLSupport.prepareRDFS(ModelFactory.createDefaultModel());
    }

    /**
     * Prepare a GeoSPARQL model from an existing model with RDFS reasoning.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling loadFunctions methods.
     *
     * @param model
     * @return
     */
    public static InfModel prepareRDFS(Model model) {
        return prepare(model, ReasonerRegistry.getRDFSReasoner());
    }

    /**
     * Prepare a GeoSPARQL model from an existing model with alternative
     * Reasoner, e.g. OWL.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling loadFunctions methods.
     *
     * @param model
     * @param reasoner
     * @return
     */
    public static InfModel prepare(Model model, Reasoner reasoner) {
        InputStream geosparqlSchemaInputStream = GeoSPARQLSupport.class.getClassLoader().getResourceAsStream(GEOSPARQL_SCHEMA);
        return prepare(model, geosparqlSchemaInputStream, reasoner);
    }

    /**
     * Prepare a GeoSPARQL model from file with RDFS reasoning.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling loadFunctions methods.
     *
     * @param inputStream
     * @return
     */
    public static InfModel prepareRDFS(InputStream inputStream) {
        return GeoSPARQLSupport.prepare(inputStream, ReasonerRegistry.getRDFSReasoner());
    }

    /**
     * Prepare a GeoSPARQL model from file with alternative Reasoner, e.g. OWL.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling loadFunctions methods.
     *
     * @param inputStream
     * @param reasoner
     * @return
     */
    public static InfModel prepare(InputStream inputStream, Reasoner reasoner) {
        Model model = ModelFactory.createDefaultModel();
        model.read(inputStream, null);

        return GeoSPARQLSupport.prepare(model, reasoner);
    }

    /**
     * Prepare a model from an existing model with alternative GeoSPARQL schema
     * and Reasoner, e.g. OWL.
     * <br> In-memory indexing with no storing of indexes applied by default.
     * This can be changed by calling loadFunctions methods.
     *
     * @param model
     * @param geosparqlSchemaInputStream
     * @param reasoner
     * @return
     */
    public static InfModel prepare(Model model, InputStream geosparqlSchemaInputStream, Reasoner reasoner) {

        //Register GeoSPARQL functions if required.
        loadFunctionsMemoryIndex();

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
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup.
     */
    public static void loadFunctionsMemoryIndex() {
        loadFunctions(IndexOption.MEMORY, null);
        defaultMemoryIndexMaxSize();
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup but override the
     * default maximum sizes of indexes and registries.
     * <br>Registries are small but contain frequently re-used or generally
     * useful data.
     * <br>Indexes are large and contain data that may have re-use in specific
     * contexts.
     * <br>Any existing in-memory indexes and registries will be emptied.
     *
     * @param geometryLiteralIndexMaxSize - default max size: 100,000
     * @param geometryTransformIndexMaxSize - default max size: 100,000
     * @param crsRegistryMaxSize - default max size: 20
     * @param unitsRegistryMaxSize - default max size: 20
     * @param mathTransformRegistryMaxSize - default max size: 20
     */
    public static void loadFunctionsMemoryIndex(Integer geometryLiteralIndexMaxSize, Integer geometryTransformIndexMaxSize, Integer crsRegistryMaxSize, Integer unitsRegistryMaxSize, Integer mathTransformRegistryMaxSize) {
        loadFunctions(IndexOption.MEMORY, null);
        GeometryLiteralIndex.setIndexMaxSize(geometryLiteralIndexMaxSize);
        GeometryTransformIndex.setIndexMaxSize(geometryTransformIndexMaxSize);
        setRegistryMaxSize(crsRegistryMaxSize, unitsRegistryMaxSize, mathTransformRegistryMaxSize);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with memory
     * indexing.
     * <br>Use this for in-memory indexing GeoSPARQL setup but override the
     * default maximum sizes of indexes.
     * <br>Indexes are large and contain data that may have re-use in specific
     * contexts.
     * <br>Any existing in-memory indexes will be emptied.
     *
     * @param geometryLiteralIndexMaxSize - default max size: 100,000
     * @param geometryTransformIndexMaxSize - default max size: 100,000
     */
    public static void loadFunctionsMemoryIndex(Integer geometryLiteralIndexMaxSize, Integer geometryTransformIndexMaxSize) {
        loadFunctions(IndexOption.MEMORY, null);
        GeometryLiteralIndex.setIndexMaxSize(geometryLiteralIndexMaxSize);
        GeometryTransformIndex.setIndexMaxSize(geometryTransformIndexMaxSize);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with TDB indexing.
     * <br>Use this for TDB indexing GeoSPARQL setup.
     */
    public static void loadFunctionsTDBIndex() {
        loadFunctions(IndexOption.TDB, null);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions with no indexing.
     * <br>Use this for no indexing GeoSPARQL setup.
     * <br>Warning: Any previously setup index folders will be deleted.
     */
    public static void loadFunctionsNoIndex() {
        loadFunctions(IndexOption.NONE, null);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions. Store any indexes
     * in the specified folder.
     * <br>Use this for persisting indexes such as a TDB setup or storing memory
     * indexes to file at shutdown.
     * <br>Warning: When set to NONE, any previously setup index folders will be
     * deleted.
     *
     * @param indexOption
     * @param indexFolder
     */
    public static void loadFunctions(IndexOption indexOption, File indexFolder) {

        indexOptionEnum = indexOption;

        switch (indexOptionEnum) {
            case MEMORY:
                setupMemoryIndex(indexFolder);
                break;
            case TDB:
                setupTDBIndex(indexFolder);
                break;
            default:
                setupNoIndex();
        }

        //Only register functions once.
        if (!isFunctionsRegistered) {
            PropertyFunctionRegistry propertyRegistry = PropertyFunctionRegistry.get();
            FunctionRegistry functionRegistry = FunctionRegistry.get();
            NonTopological.loadFilterFunctions(functionRegistry);
            functionRegistry.put(Geo.RELATE_NAME, RelateFF.class);
            SimpleFeatures.loadPropertyFunctions(propertyRegistry);
            SimpleFeatures.loadFilterFunctions(functionRegistry);
            Egenhofer.loadPropertyFunctions(propertyRegistry);
            Egenhofer.loadFilterFunctions(functionRegistry);
            RCC8.loadPropertyFunctions(propertyRegistry);
            RCC8.loadFilterFunctions(functionRegistry);
            Relate.loadRelateFunction(functionRegistry);
            GeometryProperty.loadPropertyFunctions(propertyRegistry);
            TypeMapper.getInstance().registerDatatype(WKTDatatype.THE_WKT_DATATYPE);
            TypeMapper.getInstance().registerDatatype(GMLDatatype.THE_GML_DATATYPE);
            isFunctionsRegistered = true;
        }
    }

    private static void setupMemoryIndex(File indexFolder) {
        if (indexFolder != null) {
            //Only load and setup storage once per filename.
            if (indexStorageFolder == null | !indexFolder.equals(indexStorageFolder)) {
                loadMemoryIndexes(indexFolder);
                storeMemoryIndexesAtShutdown(indexFolder);
                indexStorageFolder = indexFolder;
            }
        }
    }

    private static void setupTDBIndex(File indexFolder) {
        removeMemoryIndexStorageThread();
        zeroMemoryIndexMaxSize();
    }

    private static void setupNoIndex() {
        removeMemoryIndexStorageThread();
        zeroMemoryIndexMaxSize();
        if (indexStorageFolder != null) {
            FileUtils.deleteQuietly(indexStorageFolder);
            indexStorageFolder = null;
        }
    }

    private static void zeroMemoryIndexMaxSize() {
        GeometryLiteralIndex.setIndexMaxSize(0);
        GeometryTransformIndex.setIndexMaxSize(0);
    }

    private static void defaultMemoryIndexMaxSize() {
        GeometryLiteralIndex.setIndexMaxSize(GEOMETRY_LITERAL_INDEX_MAX_SIZE);
        GeometryTransformIndex.setIndexMaxSize(GEOMETRY_TRANSFORM_INDEX_MAX_SIZE);
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
            MathTransformRegistry.readMathTransformRegistry(mathTransformRegistryFile);
        }

        //Geometry Transform Index
        File geometryTransformIndexFile = new File(indexFolder, GEOMETRY_TRANSFORM_INDEX_FILENAME);
        if (geometryTransformIndexFile.exists()) {
            GeometryTransformIndex.readGeometryTransformIndex(geometryTransformIndexFile);
        }

        //Geometry Literal Index
        File geometryLiteralIndexFile = new File(indexFolder, GEOMETRY_LITERAL_INDEX_FILENAME);
        if (geometryLiteralIndexFile.exists()) {
            GeometryLiteralIndex.readGeometryLiteralIndex(geometryLiteralIndexFile);
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
                MathTransformRegistry.writeMathTransformRegistry(mathTransformRegistryFile);

                //Geometry Transform Index
                File geometryTransformIndexFile = new File(indexFolder, GEOMETRY_TRANSFORM_INDEX_FILENAME);
                GeometryTransformIndex.writeGeometryTransformIndex(geometryTransformIndexFile);

                //Geometry Literal Index
                File geometryLiteralIndexFile = new File(indexFolder, GEOMETRY_LITERAL_INDEX_FILENAME);
                GeometryLiteralIndex.writeGeometryLiteralIndex(geometryLiteralIndexFile);
            }
        };
        Runtime.getRuntime().addShutdownHook(thread);

        //Remove any previous shutdown storage thread.
        removeMemoryIndexStorageThread();

        //Retain the thread in case called again.
        shutdownStorageThread = thread;
    }

    private static void removeMemoryIndexStorageThread() {
        if (shutdownStorageThread != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownStorageThread);
        }
    }

    public static final void clearAllIndexesAndRegistries() {
        GeometryLiteralIndex.clearAll();
        GeometryTransformIndex.clearAll();
        CRSRegistry.clearAll();
        MathTransformRegistry.clearAll();
    }

    public static final IndexOption getIndexOption() {
        return indexOptionEnum;
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

}
