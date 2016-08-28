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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
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

        MODEL = makeModel(RDFDataLocation.SAMPLE);

        String queryString = "SELECT ?place WHERE{ "
                + "ntu:D ntu:hasExactGeometry ?dGeom . "
                + "?dGeom gml:asGML ?dGML . "
                + "?place ntu:hasExactGeometry ?Geom . "
                + "?Geom gml:asGML ?GML . "
                //+ "ntu:D geor:sfContains ?place ."
                + " }"
                + "ORDER BY ASC ( geof:distance( ?dGML, ?GML, uom:metre ) )";

        makeQuery(queryString);

    }

    public static Model makeModel(String location) {
        MODEL = ModelFactory.createDefaultModel();
        return MODEL.read(location);
    }

    public static void makeQuery(String queryString) {

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
