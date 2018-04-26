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
import implementation.vocabulary.Geo;
import java.io.File;
import java.io.InputStream;
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

    /**
     * Prepare an empty model for GeoSPARQL.
     *
     * @return
     */
    public static InfModel prepare() {
        return GeoSPARQLSupport.prepare(ModelFactory.createDefaultModel());
    }

    /**
     * Prepare a model for GeoSPARQL usage from existing model.
     *
     * @param model
     * @return
     */
    public static InfModel prepare(Model model) {

        //Register GeoSPARQL functions if required.
        loadFunctions();


        /*
         * The use of OWL reasoner can bind schema with existing test data.
         */
        InputStream inputStream = GeoSPARQLSupport.class.getClassLoader().getResourceAsStream(GEOSPARQL_SCHEMA);
        Model schema = ModelFactory.createDefaultModel();
        schema.read(inputStream, null);

        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);

        /*
         * Setup inference model.
         */
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);
        return infModel;
    }

    /**
     * Prepare a model for GeoSPARQL usage from file.
     *
     * @param inputStream
     * @return
     */
    public static InfModel prepare(InputStream inputStream) {

        /**
         * Load data
         */
        Model model = ModelFactory.createDefaultModel();
        model.read(inputStream, null);

        return GeoSPARQLSupport.prepare(model);

    }

    /**
     * Initialise all GeoSPARQL property and filter functions.
     * <br>Use this for in memory GeoSPARQL setup.
     */
    public static void loadFunctions() {
        loadFunctions(null);
    }

    /**
     * Initialise all GeoSPARQL property and filter functions. Store any indexes
     * in the specified folder.
     * <br>Use this for persisting indexes such as a TDB setup.
     *
     * @param indexFolder
     */
    public static void loadFunctions(File indexFolder) {

        if (indexFolder != null) {
            //Only load and setup storage once per filename.
            if (indexStorageFolder == null | !indexFolder.equals(indexStorageFolder)) {
                loadIndexes(indexFolder);
                storeIndexesAtShutdown(indexFolder);
                indexStorageFolder = indexFolder;
            }
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

    private static void loadIndexes(File indexFolder) {

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
            GeometryTransformIndex.readMathTransformRegistry(mathTransformRegistryFile);
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

    private static void storeIndexesAtShutdown(File indexFolder) {

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
                GeometryTransformIndex.writeMathTransformRegistry(mathTransformRegistryFile);

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
        if (shutdownStorageThread != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownStorageThread);
        }

        //Retain the thread in case called again.
        shutdownStorageThread = thread;
    }

    public static final void clearAllIndexesAndRegistries() {
        CRSRegistry.clearAll();
        GeometryLiteralIndex.clearAll();
        GeometryTransformIndex.clearAll();
    }

}
