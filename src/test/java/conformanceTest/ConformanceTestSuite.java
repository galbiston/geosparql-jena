/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest;

import implementation.support.Prefixes;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;

/**
 *
 * @author haozhechen
 */
public class ConformanceTestSuite {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * Inference WKT model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_WKT_MODEL;

    /**
     * Default GML model - with no inference support.
     */
    public static Model DEFAULT_GML_MODEL;

    /**
     * Inference GML model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_GML_MODEL;

    /**
     * This negative model facilitates the test for empty geometries and the GML
     * literal without a specified SRID.
     */
    public static Model TEST_GML_MODEL;

    /**
     * This method initialize all the WKT test models, need to be called before
     * query execution.
     */
    public static void initWktModel() {
        /**
         * Setup inference model.
         */
        DEFAULT_WKT_MODEL = ModelFactory.createDefaultModel();
        Model schema = FileManager.get().loadModel("http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf");
        /**
         * The use of OWL reasoner can bind schema with existing test data.
         */
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        INF_WKT_MODEL = ModelFactory.createInfModel(reasoner, DEFAULT_WKT_MODEL);
        INF_WKT_MODEL.read(RDFDataLocation.SAMPLE_WKT);

    }

    /**
     * This method initialize all the GML test models, need to be called before
     * query execution.
     */
    public static void initGmlModel() {
        /**
         * Setup inference model.
         */
        DEFAULT_GML_MODEL = ModelFactory.createDefaultModel();
        Model schema = FileManager.get().loadModel("http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf");
        /**
         * The use of OWL reasoner can bind schema with existing test data.
         */
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        INF_GML_MODEL = ModelFactory.createInfModel(reasoner, DEFAULT_GML_MODEL);
        INF_GML_MODEL.read(RDFDataLocation.SAMPLE_GML);

    }

    public static ArrayList resourceQuery(String queryString, InfModel queryModel) {
        ArrayList resultList = new ArrayList();
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
                    resultList.add(resource.toString());
                }
            }
        }
        return resultList;
    }

    public static ArrayList literalQuery(String queryString, InfModel queryModel) {
        ArrayList resultList = new ArrayList();
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
                    resultList.add(literal.toString());
                }
            }
        }

        return resultList;
    }

    /**
     * This method test for the empty result return.
     *
     * @param queryString
     * @param queryModel
     * @return true - if the test has any result<br>false - if there is no
     * result.
     */
    public static boolean emptyQuery(String queryString, InfModel queryModel) {
        boolean hasResult = true;
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), queryModel)) {
            ResultSet results = qexec.execSelect();
            if (!results.hasNext()) {
                hasResult = false;
            }
        }

        return hasResult;
    }

    public static String geometryTopologyQuery(String functionName, String geometry) {
        String query = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER " + functionName + "(?aWKT, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> " + geometry + "\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>) ."
                + "}";
        return query;
    }

    public static String topologyVocabluary(String functionName, String instance) {
        String query = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + instance + " ex:hasExactGeometry ?bGeom ."
                + " ?aGeom " + functionName + " ?bGeom ."
                + "}";
        return query;
    }
}
