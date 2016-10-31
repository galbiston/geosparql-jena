/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import vocabulary.RDFDataLocation;
import functionregistry.NonTopological;
import functionregistry.RCC8;
import functionregistry.SimpleFeatures;
import functionregistry.Egenhofer;
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
import org.apache.jena.util.FileManager;
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
    private static int QUERYCOUNT = 1;

    /**
     * Initialize all the GeoSPARQL property and filter functions.
     * <br>Use this for standard GeoSPARQL setup
     */
    public static void init() {

        final PropertyFunctionRegistry propertyRegistry = PropertyFunctionRegistry.chooseRegistry(ARQ.getContext());
        final FunctionRegistry filterRegistry = FunctionRegistry.get(ARQ.getContext());

        NonTopological.loadFiltFunctions(filterRegistry);

        SimpleFeatures.loadPropFunctions(propertyRegistry);
        SimpleFeatures.loadFiltFunctions(filterRegistry);

        Egenhofer.loadPropFunctions(propertyRegistry);
        Egenhofer.loadFiltFunctions(filterRegistry);

        RCC8.loadPropFunctions(propertyRegistry);
        RCC8.loadFiltFunctions(filterRegistry);

    }

    /**
     * Initialize all the GeoSPARQL property and filter functions as well as the
     * query rewrite functions.
     * <br>Use this for fully GeoSPARQL functionality, need to be used with
     * inference model
     * <br>
     */
    public static void initWithQueryRewriteFunctions() {

        final PropertyFunctionRegistry propertyRegistry = PropertyFunctionRegistry.chooseRegistry(ARQ.getContext());
        final FunctionRegistry filterRegistry = FunctionRegistry.get(ARQ.getContext());

        NonTopological.loadFiltFunctions(filterRegistry);

        SimpleFeatures.loadPropFunctions(propertyRegistry);
        SimpleFeatures.loadFiltFunctions(filterRegistry);
        SimpleFeatures.loadQueryRewriteFunctions(propertyRegistry);

        Egenhofer.loadPropFunctions(propertyRegistry);
        Egenhofer.loadFiltFunctions(filterRegistry);
        Egenhofer.loadQueryRewriteFunctions(propertyRegistry);

        RCC8.loadPropFunctions(propertyRegistry);
        RCC8.loadFiltFunctions(filterRegistry);
        RCC8.loadQueryRewriteFunctions(propertyRegistry);

    }

    public static void main(String[] args) {

        LOGGER.info("GeoSPARQL Started");

        //realworldQuery();
        //sampleQuery();
        initWithQueryRewriteFunctions();
        //PropertyFunctionRegistry.get().put("http://www.opengis.net/ont/geosparql#sfEquals", prototype.SFEqualsQRPropertyFunc.class);

        MODEL = ModelFactory.createDefaultModel();
        Model schema = FileManager.get().loadModel("http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf");
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        INF_MODEL = ModelFactory.createInfModel(reasoner, MODEL);
        INF_MODEL.read(RDFDataLocation.SAMPLE_WKT);
        INF_MODEL.prepare();

        //Find a spatial location with a given name.
        String Q1 = "SELECT ?wkt WHERE{"
                + "ntu:A geo:hasGeometry ?geom ."
                + "?geom geo:asWKT ?wkt ."
                + "}";

        evaluateQueryWithInfModel(Q1);

        LOGGER.info("GeoSPARQL Ended");
    }

    public static void realworldQuery() {

        init();

        long startTime = System.nanoTime();
        MODEL = makeModel(RDFDataLocation.GEODATA);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        LOGGER.info("Data Model Read Time: {}ms", duration / 1000000);

        //Find a spatial location with a given name.
        String Q1 = "SELECT ?place WHERE{ "
                + "?place rdf:type geo:Feature . "
                + "?place rdfs:label \"Sofaditikos\"@en . "
                + " }";

        evaluateQuery(Q1);

        //Find a spatial name within a given location.
        String Q2 = "SELECT ?place WHERE{ "
                + "?place rdf:type lgd:Motorway . "
                + "?place lgd:hasGeometry ?geom . "
                + "?geom lgd:asWKT ?wkt . "
                + "FILTER (geof:sfContains(?wkt, \"Point(23 40)\"^^geo:wktLiteral ))"
                + " }";

        evaluateQuery(Q2);

        //Retrive all points within a given area.
        String Q3 = "SELECT ?place WHERE{ "
                + "?place rdf:type lgd:Motorway . "
                + "?place lgd:hasGeometry ?geom . "
                + "?geom lgd:asWKT ?wkt . "
                + "FILTER (geof:sfWithin(?wkt, \"Polygon((19 39, 21 39, 21 40, 19 40, 19 39))\"^^geo:wktLiteral ))"
                + " }";

        evaluateQuery(Q3);

        //Retrieve all the geometries which intersect a given area.
        String Q4 = "SELECT ?place WHERE{ "
                + "?place rdf:type lgd:Motorway . "
                + "?place lgd:hasGeometry ?geom . "
                + "?geom lgd:asWKT ?wkt . "
                + "FILTER (geof:sfIntersects(?wkt, \"Polygon((20 39, 21 39, 21 40, 20 40, 20 39))\"^^geo:wktLiteral ))"
                + " }";

        evaluateQuery(Q4);

        //Find all the line strings which cross a given area.
        String Q5 = "SELECT ?placeA WHERE{ "
                + "?placeA rdf:type lgd:Motorway . "
                + "?placeA lgd:hasGeometry ?geomA . "
                + "?geomA lgd:asWKT ?wktA . "
                + "lgdg:way15624241 lgd:asWKT ?wktC . "
                + "FILTER (geof:sfCrosses(?wktA, ?wktC))"
                + " }";

        evaluateQuery(Q5);

        //Retrieve all the geometry pairs within a given distance of each other.
        String Q6 = "SELECT ?placeA ?placeB WHERE{ "
                + "?placeA rdf:type lgd:Motorway . "
                + "?placeA lgd:hasGeometry ?geomA . "
                + "?geomA lgd:asWKT ?wktA . "
                + "?placeB rdf:type lgd:Motorway . "
                + "?placeB lgd:hasGeometry ?geomB . "
                + "?geomB lgd:asWKT ?wktB . "
                + "BIND (geof:distance(?wktA, ?wktB, uom:metre) AS ?dist ) . "
                + "FILTER( (?dist<5000 ) && !sameTerm(?geomA, ?geomB) )"
                + " }";

        evaluateQuery(Q6);

        //Find 2 nearest points to a given location.
        String Q7 = "SELECT ?place WHERE{ "
                + "?place rdf:type lgd:Motorway . "
                + "?place lgd:hasGeometry ?geom . "
                + "?geom lgd:asWKT ?wkt . "
                + "BIND (geof:distance(?wkt, \"POINT(20 40)\"^^geo:wktLiteral, uom:metre ) AS ?dist)"
                + " }"
                + "ORDER BY ASC (?dist)"
                + "LIMIT 2";

        evaluateQuery(Q7);

        //Find 2 nearest points to a given spatial object.
        String Q8 = "SELECT ?place WHERE{ "
                + "?place rdf:type lgd:Motorway . "
                + "?place lgd:hasGeometry ?geom . "
                + "?geom lgd:asWKT ?wkt . "
                + "lgdg:way15624241 lgd:asWKT ?wktF . "
                + "BIND (geof:distance(?wkt, ?wktF, uom:metre ) AS ?dist)"
                + " }"
                + "ORDER BY ASC (?dist)"
                + "LIMIT 2";

        evaluateQuery(Q8);

        //Return the portions of any line strings which overlap a given area.
        String Q9 = "SELECT ?overlap WHERE{ "
                + "?place rdf:type lgd:Motorway . "
                + "?place lgd:hasGeometry ?geom . "
                + "?geom lgd:asWKT ?wkt . "
                + "BIND (geof:intersection(?wkt, \"Polygon((20 39, 21 39, 21 40, 20 40, 20 39))\"^^geo:wktLiteral) AS ?overlap)"
                + "FILTER (geof:sfIntersects(?wkt, \"Polygon((20 39, 21 39, 21 40, 20 40, 20 39))\"^^geo:wktLiteral))"
                + " }";

        evaluateQuery(Q9);

        //Generate a buffer for a given line string.
        String Q10 = "SELECT (geof:buffer(?wkt, 10, uom:metre) AS ?buf) WHERE{ "
                + "lgdg:way15624241 lgd:asWKT ?wkt . "
                + " }";

        evaluateQuery(Q10);

        //Generate a union for given geometries.
        String Q11 = "SELECT (geof:union(?wktC, ?wktB) AS ?union) WHERE{ "
                + "lgdg:way15624241 lgd:asWKT ?wktB . "
                + "lgdg:way15624308 lgd:asWKT ?wktC . "
                + " }";

        evaluateQuery(Q11);

        //Count all spatial objects within a given area.
        String Q12 = "SELECT ?place (COUNT(?geom) AS ?no_objects) WHERE{ "
                + "?place lgd:hasGeometry ?geom . "
                + "?geom lgd:asWKT ?wkt . "
                + "FILTER (geof:sfWithin(?wkt, \"Polygon((20 39, 21 39, 21 40, 20 40, 20 39))\"^^geo:wktLiteral ))"
                + " }"
                + "GROUP BY ?place";

        evaluateQuery(Q12);
        //Locate points which are further than a certain distance from a given location.
        String Q13 = "SELECT ?place WHERE{ "
                + "?place rdf:type lgd:Motorway . "
                + "?place lgd:hasGeometry ?geom . "
                + "?geom lgd:asWKT ?wkt . "
                + "FILTER NOT EXISTS {"
                + "     lgdg:way15624241 lgd:asWKT ?wktA . "
                + "     FILTER( geof:distance(?wkt, ?wktA, uom:metre) < 100000)"
                + "   }"
                + "}";

        evaluateQuery(Q13);
    }

    public static void sampleQuery() {

        init();

        long startTime = System.nanoTime();
        MODEL = makeModel(RDFDataLocation.SAMPLE_WKT);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        LOGGER.info("Data Model Read Time: {}ms", duration / 1000000);

        //Find a spatial location with a given name.
        String Q1 = "SELECT ?place WHERE{ "
                + "?place rdf:type ntu:PlaceOfInterest . "
                + "?place ntu:name \"place1\" . "
                + " }";

        evaluateQuery(Q1);

        //Find a spatial name within a given location.
        String Q2 = "SELECT ?place WHERE{ "
                + "?place rdf:type ntu:PlaceOfInterest . "
                + "?place ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + "FILTER (geof:sfContains(?wkt, \"Point(-83.4 34.4)\"^^sf:wktLiteral ))"
                + " }";

        evaluateQuery(Q2);

        //Retrive all points within a given area.
        String Q3 = "SELECT ?place WHERE{ "
                + "?place ntu:hasPointGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + "FILTER (geof:sfWithin(?wkt, \"Polygon((-83.6 34.1, -83.0 34.1, -83.0 34.5, -83.6 34.5, -83.6 34.1))\"^^sf:wktLiteral ))"
                + " }";

        evaluateQuery(Q3);

        //Retrieve all the geometries which intersect a given area.
        String Q4 = "SELECT ?place WHERE{ "
                + "?place ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + "FILTER (geof:sfIntersects(?wkt, \"Polygon((-83.6 34.1, -83.0 34.1, -83.0 34.5, -83.6 34.5, -83.6 34.1))\"^^sf:wktLiteral ))"
                + " }";

        evaluateQuery(Q4);

        //Find all the line strings which cross a given area.
        String Q5 = "SELECT ?placeA WHERE{ "
                + "?placeA ntu:hasExactGeometry ?geomA . "
                + "?geomA geo:asWKT ?wktA . "
                + "ntu:C ntu:hasExactGeometry ?geomC . "
                + "?geomC geo:asWKT ?wktC . "
                + "FILTER (geof:sfCrosses(?wktA, ?wktC))"
                + " }";

        evaluateQuery(Q5);

        //Retrieve all the geometry pairs within a given distance of each other.
        String Q6 = "SELECT ?placeA ?placeB WHERE{ "
                + "?placeA ntu:hasExactGeometry ?geomA . "
                + "?geomA geo:asWKT ?wktA . "
                + "?placeB ntu:hasExactGeometry ?geomB . "
                + "?geomB geo:asWKT ?wktB . "
                + "BIND (geof:distance(?wktA, ?wktB, uom:metre) AS ?dist ) . "
                + "FILTER( (?dist<500000) && !sameTerm(?geomA, ?geomB) )"
                + " }";

        evaluateQuery(Q6);

        //Find 2 nearest points to a given location.
        String Q7 = "SELECT ?place WHERE{ "
                + "?place ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + "BIND (geof:distance(?wkt, \"POINT(-83.0 34.0)\"^^sf:wktLiteral, uom:metre ) AS ?dist)"
                + " }"
                + "ORDER BY ASC (?dist)"
                + "LIMIT 2";

        evaluateQuery(Q7);

        //Find 2 nearest points to a given spatial object.
        String Q8 = "SELECT ?place WHERE{ "
                + "?place ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + "ntu:F ntu:hasExactGeometry ?geomF . "
                + "?geomF geo:asWKT ?wktF . "
                + "BIND (geof:distance(?wkt, ?wktF, uom:metre ) AS ?dist)"
                + " }"
                + "ORDER BY ASC (?dist)"
                + "LIMIT 2";

        evaluateQuery(Q8);

        //Return the portions of any line strings which overlap a given area.
        String Q9 = "SELECT ?overlap WHERE{ "
                + "?place ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + "BIND (geof:intersection(?wkt, \"Polygon((-83.6 34.1, -83.0 34.1, -83.0 34.5, -83.6 34.5, -83.6 34.1))\"^^sf:wktLiteral) AS ?overlap)"
                + "FILTER (geof:sfIntersects(?wkt, \"Polygon((-83.6 34.1, -83.0 34.1, -83.0 34.5, -83.6 34.5, -83.6 34.1))\"^^sf:wktLiteral))"
                + " }";

        evaluateQuery(Q9);

        //Generate a buffer for a given line string.
        String Q10 = "SELECT (geof:buffer(?wkt, 10, uom:metre) AS ?buf) WHERE{ "
                + "ntu:B ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + " }";

        evaluateQuery(Q10);

        //Generate a union for given geometries.
        String Q11 = "SELECT (geof:union(?wktC, ?wktB) AS ?union) WHERE{ "
                + "ntu:B ntu:hasExactGeometry ?geomB . "
                + "?geomB geo:asWKT ?wktB . "
                + "ntu:C ntu:hasExactGeometry ?geomC . "
                + "?geomC geo:asWKT ?wktC . "
                + " }";

        evaluateQuery(Q11);

        //Count all spatial objects within a given area.
        String Q12 = "SELECT ?place (COUNT(?geom) AS ?no_objects) WHERE{ "
                + "?place ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + "FILTER (geof:sfWithin(?wkt, \"Polygon((-83.6 34.1, -83.0 34.1, -83.0 34.5, -83.6 34.5, -83.6 34.1))\"^^sf:wktLiteral ))"
                + " }"
                + "GROUP BY ?place";

        evaluateQuery(Q12);

        //Locate points which are further than a certain distance from a given location.
        String Q13 = "SELECT ?place WHERE{ "
                + "?place ntu:hasExactGeometry ?geom . "
                + "?geom geo:asWKT ?wkt . "
                + "FILTER NOT EXISTS {"
                + "     ntu:A ntu:hasExactGeometry ?geomA . "
                + "     ?geomA geo:asWKT ?wktA . "
                + "     FILTER( geof:distance(?wkt, ?wktA, uom:metre) < 100000)"
                + "   }"
                + "}";

        evaluateQuery(Q13);

    }

    /**
     * This method returns default model without reasoner
     *
     * @param location - the location of the file.
     * @return MODEL - the default model.
     */
    public static Model makeModel(String location) {
        MODEL = ModelFactory.createDefaultModel();
        return MODEL.read(location);
    }

    /**
     * This method enables the default RDFS reasoner
     *
     * @param location - the location of the file.
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
        LOGGER.info("Executing the {}th query... ...", QUERYCOUNT++);
        long queryStartTime = System.nanoTime();

        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
            LOGGER.info("\n" + query.asQuery().toString());
            ResultSet rs = qExec.execSelect();

            ResultSetFormatter.out(rs);

        }

        long endTime = System.nanoTime();
        long duration = endTime - queryStartTime;
        LOGGER.info("Query Execution Time: {} ms", duration / 1000000);
    }

    public static void evaluateQueryWithInfModel(String queryString) {
        LOGGER.info("Executing the {}th query... ...", QUERYCOUNT++);
        long queryStartTime = System.nanoTime();

        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), INF_MODEL)) {
            LOGGER.info("\n" + query.asQuery().toString());
            ResultSet rs = qExec.execSelect();

            ResultSetFormatter.out(rs);

        }

        long endTime = System.nanoTime();
        long duration = endTime - queryStartTime;
        LOGGER.info("Query Execution Time: {} ms", duration / 1000000);
    }

}
