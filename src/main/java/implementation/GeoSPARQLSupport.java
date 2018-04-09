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
import implementation.vocabulary.Geo;
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
    public static final Integer GEOMETRY_TRANSFORM_INDEX_MAX_SIZE = 50000;
    public static final Integer CRS_REGISTRY_MAX_SIZE = 20;
    public static final Integer GEOMETRY_LITERAL_INDEX_MAX_SIZE = 50000;
    /**
     * GeoSPARQL schema
     */
    private static final String GEOSPARQL_SCHEMA = "schema/geosparql_vocab_all.rdf";
    private static Boolean isFunctionsRegistered = false;

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
     * Initialize all the GeoSPARQL property and filter functions.
     * <br>Use this for standard GeoSPARQL setup
     */
    public static void loadFunctions() {
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

}
