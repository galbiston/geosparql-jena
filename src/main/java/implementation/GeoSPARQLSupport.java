/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import geof.topological.RelateFF;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.function_registration.Egenhofer;
import implementation.function_registration.GeometryProperty;
import implementation.function_registration.NonTopological;
import implementation.function_registration.RCC8;
import implementation.function_registration.Relate;
import implementation.function_registration.SimpleFeatures;
import implementation.index.IndexConfiguration;
import implementation.index.IndexOption;
import implementation.registry.CRSRegistry;
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
     * GeoSPARQL schema
     */
    private static final String GEOSPARQL_SCHEMA = "schema/geosparql_vocab_all.rdf";
    private static Boolean IS_FUNCTIONS_REGISTERED = false;

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
        IndexConfiguration.defaultMemoryIndexMaxSize();
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
     * @param geometryLiteralIndexMaxSize - default max size: 50,000
     * @param geometryWrapperCRSTransformationsMaxSize - default max size: 8 (3
     * CRS used in certain functions)
     * @param queryRewriteIndexMaxSize - default max size: 50,000
     */
    public static void loadFunctionsMemoryIndex(Integer geometryLiteralIndexMaxSize, Integer geometryWrapperCRSTransformationsMaxSize, Integer queryRewriteIndexMaxSize) {
        loadFunctions(IndexOption.MEMORY, null);
        IndexConfiguration.setIndexMaxSize(geometryLiteralIndexMaxSize, geometryWrapperCRSTransformationsMaxSize, queryRewriteIndexMaxSize);
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
     * Provide an IndexOption enum for configuring index. If TDB option is
     * selected then folder created in current working directory.
     *
     * @param indexOption
     */
    public static void loadFunctions(IndexOption indexOption) {
        File indexFolder;
        if (indexOption.equals(IndexOption.TDB)) {
            indexFolder = new File("geosparql_indexes");
        } else {
            indexFolder = null;
        }
        loadFunctions(indexOption, indexFolder);
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

        //Set the configuration for indexing.
        IndexConfiguration.setConfig(indexOption, indexFolder);

        //Only register functions once.
        if (!IS_FUNCTIONS_REGISTERED) {

            //Setup Default Cordinate Reference Systems
            CRSRegistry.setupDefaultCRS();

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
            TypeMapper.getInstance().registerDatatype(WKTDatatype.INSTANCE);
            TypeMapper.getInstance().registerDatatype(GMLDatatype.INSTANCE);
            IS_FUNCTIONS_REGISTERED = true;
        }
    }

    /**
     * Empty all indexes and registries currently in use.
     */
    public static final void clearAllIndexesAndRegistries() {
        //Convenience method so that setup and clearing in one class.
        IndexConfiguration.clearAllIndexesAndRegistries();
    }

    /**
     * Writes the current index and registry to the provided folder.
     *
     * @param indexFolder
     */
    public static final void writeIndexRegistryToFile(File indexFolder) {
        IndexConfiguration.writeIndexRegistryToFile(indexFolder);
    }

}
