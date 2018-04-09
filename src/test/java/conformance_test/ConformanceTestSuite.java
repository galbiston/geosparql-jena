/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test;

import implementation.GeoSPARQLSupport;
import implementation.support.Prefixes;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class ConformanceTestSuite {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConformanceTestSuite.class);

    /**
     * Sample Data in WKT - small and quick
     */
    public static final String SAMPLE_WKT = "dataset/sampleWKT.rdf";

    /**
     * Empty WKT Data - test for empty geometry and default SRID return
     */
    public static final String SAMPLE_WKT_EMPTY = "dataset/sampleWKTEmpty.rdf";

    /**
     * Sample Data in GML - small and quick
     */
    public static final String SAMPLE_GML = "dataset/sampleGML.rdf";

    /**
     * Empty GML Data - test for empty geometry and default SRID return
     */
    public static final String SAMPLE_GML_EMPTY = "dataset/sampleGMLEmpty.rdf";

    /**
     * This method initialize all the WKT test models, need to be called before
     * query execution.
     *
     * @return
     */
    public static InfModel initWktModel() {
        InputStream inputStream = getInputStream(SAMPLE_WKT);
        return GeoSPARQLSupport.prepare(inputStream);
    }

    /**
     * This method initialize all the empty WKT test models, need to be called
     * before query execution.
     *
     * @return
     */
    public static InfModel initWktEmptyModel() {
        InputStream inputStream = getInputStream(SAMPLE_WKT_EMPTY);
        return GeoSPARQLSupport.prepare(inputStream);
    }

    /**
     * This method initialize all the GML test models, need to be called before
     * query execution.
     *
     * @return
     */
    public static InfModel initGmlModel() {
        InputStream inputStream = getInputStream(SAMPLE_GML);
        return GeoSPARQLSupport.prepare(inputStream);
    }

    /**
     * This method initialize all the empty GML test models, need to be called
     * before query execution.
     *
     * @return
     */
    public static InfModel initGmlEmptyModel() {
        InputStream inputStream = getInputStream(SAMPLE_GML_EMPTY);
        return GeoSPARQLSupport.prepare(inputStream);
    }

    private static InputStream getInputStream(String filepath) {
        return ConformanceTestSuite.class.getClassLoader().getResourceAsStream(filepath);
    }

    /**
     * Use query iterator to query resources, i.e: http://this/is/resource#A.
     *
     * @param queryString - query
     * @param queryModel - Use InfModel to get the full reasoner support
     * @return - the returned result list
     */
    public static ArrayList<String> resourceQuery(String queryString, InfModel queryModel) {
        ArrayList<String> resultList = new ArrayList<>();
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), queryModel)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Iterator varIterator = solution.varNames();
                while (varIterator.hasNext()) {
                    String varName = (String) varIterator.next();
                    Resource resource = solution.getResource(varName);
                    //System.out.println("this.expResult.add(\"" + resource.toString() + "\");");
                    resultList.add(resource.toString());
                }
            }
        }
        return resultList;
    }

    /**
     * True or false query result.
     *
     * @param queryString
     * @param queryModel
     * @return
     */
    public static Boolean askQuery(String queryString, InfModel queryModel) {

        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString);
        query.setNsPrefixes(Prefixes.get());

        LOGGER.info("{}", query.toString());
        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), queryModel)) {
            return qexec.execAsk();
        } catch (RuntimeException ex) {
            LOGGER.error("Exception: {} {}", ex.getMessage(), query.toString());
            return null;
        }

    }

    /**
     * Use query iterator to query literals, i.e: WKT or GML.
     *
     * @param queryString - query
     * @param queryModel - Use InfModel to get the full reasoner support
     * @return - the returned result list
     */
    public static List<Literal> literalQuery(String queryString, InfModel queryModel) {
        List<Literal> resultList = new ArrayList<>();
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), queryModel)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Iterator varIterator = solution.varNames();
                while (varIterator.hasNext()) {
                    String varName = (String) varIterator.next();
                    Literal literal = solution.getLiteral(varName);
                    resultList.add(literal);
                }
            }
        }

        return resultList;
    }

    public static Literal literalSingleQuery(String queryString, InfModel queryModel) {
        List<Literal> literals = literalQuery(queryString, queryModel);
        if (literals.size() == 1) {
            return literals.get(0);
        }
        return null;
    }

    /**
     *
     * A helper class for reducing the effort when writing test queries.
     *
     * @param functionName - for example: geof:sfEquals.
     * @param geometry - for example: POINT(-83.4 34.4)
     * @return - the final query string
     */
    public static String geometryTopologyQuery(String functionName, String geometry) {
        String query = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER " + functionName + "(?aWKT, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> " + geometry + "\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>) ."
                + "}";
        return query;
    }

    /**
     * A helper class for reducing the effort when writing test queries.
     *
     * @param instance - the spatial object to be compared.
     * @param functionName - for example: geo:sfEquals.
     * @param filter - an extra line for adding SPARQL filters
     * @return - the final query string
     */
    public static String topologyVocabluaryQuery(String instance, String functionName, String filter) {
        String query = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom . "
                + instance + " ex:hasExactGeometry ?bGeom ."
                + " ?aGeom " + functionName + " ?bGeom ."
                + filter
                + "}";
        return query;
    }

    public static String featureFeatureQuery(String instance, String functionName) {
        String query = "SELECT ?place WHERE{"
                + "?place " + functionName + " " + instance + " . "
                + "}";
        return query;
    }

    public static String geometryFeatureQuery(String instance, String functionName) {
        String query = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom . "
                + "?aGeom " + functionName + " " + instance + " . "
                + "}";
        return query;
    }

    public static String featureGeometryQuery(String instance, String functionName) {
        String query = "SELECT ?place WHERE{"
                + instance + " ex:hasExactGeometry ?bGeom . "
                + "?place " + functionName + " ?bGeom . "
                + "}";
        return query;
    }
}
