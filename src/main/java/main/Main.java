/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.apache.jena.query.ARQ;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import org.apache.jena.vocabulary.ReasonerVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Prefixes;

/**
 *
 * @author haozhechen
 */
public class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static Model MODEL;
    private static InfModel INF_MODEL;

    /**
     * Initiate all the GeoSPARQL property, query rewrite, and filter functions
     *
     */
    public static void init() {

        final PropertyFunctionRegistry propertyRegistry = PropertyFunctionRegistry.chooseRegistry(ARQ.getContext());
        final FunctionRegistry filterRegistry = FunctionRegistry.get(ARQ.getContext());

        NonTopoFunctionsRegistry.loadFiltFunctions(filterRegistry);

        SFFunctionRegistry.loadPropFunctions(propertyRegistry);
        SFFunctionRegistry.loadFiltFunctions(filterRegistry);

        EHFunctionsRegistry.loadPropFunctions(propertyRegistry);
        EHFunctionsRegistry.loadFiltFunctions(filterRegistry);

        RCC8FunctionsRegistry.loadPropFunctions(propertyRegistry);
        RCC8FunctionsRegistry.loadFiltFunctions(filterRegistry);

    }

    public static void main(String[] args) {

        init();

        MODEL = makeInfModel(RDFDataLocation.SAMPLE_WKT);

        String Q1 = "SELECT ?feature WHERE{ "
                + "?feature rdf:type geo:Feature . "
                //+ "ntu:D geor:sfContains ?place ."
                + " }";

        evaluateQuery(Q1);

    }

    /**
     * This method returns default model without reasoner
     *
     * @return MODEL - the default model.
     */
    public static Model makeModel(String location) {
        MODEL = ModelFactory.createDefaultModel();
        return MODEL.read(location);
    }

    /**
     * This method enables the default RDFS reasoner
     *
     * @return INF_MODEL - the RDFS reasoner enabled model.
     */
    public static InfModel makeInfModel(String location) {
        MODEL = ModelFactory.createDefaultModel();
        //RDFS Reasoner
        Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
        reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_DEFAULT);
        INF_MODEL = ModelFactory.createInfModel(reasoner, MODEL);
        INF_MODEL.read(location);
        return INF_MODEL;
    }

    public static void evaluateQuery(String queryString) {

        long queryStartTime = System.nanoTime();

        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            ResultSet rs = qExec.execSelect();
            long endTime = System.nanoTime();
            long duration = endTime - queryStartTime;
            ResultSetFormatter.out(rs);
            LOGGER.info("Query Execution Time: {}", duration / 1000000);
        }
    }

}
