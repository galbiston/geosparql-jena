/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.query_rewrite_extension;

import conformance_test.ConformanceTestSuite;
import conformance_test.QueryLoader;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;

/**
 *
 *
 */
public class QueryRewriteTestMethods {

    private static final InfModel SPATIAL_RELATIONS_MODEL = ConformanceTestSuite.initSpatialRelationsModel();

    private static final String BOTH_BOUND_QUERY_FILE = "sparql_query/BothBoundPropertyQuery.spl";
    private static final String BOTH_BOUND_QUERY = QueryLoader.read(BOTH_BOUND_QUERY_FILE);

    private static final String UNBOUND_OBJECT_QUERY_FILE = "sparql_query/UnboundObjectPropertyQuery.spl";
    private static final String UNBOUND_OBJECT_QUERY = QueryLoader.read(UNBOUND_OBJECT_QUERY_FILE);

    private static final String UNBOUND_SUBJECT_QUERY_FILE = "sparql_query/UnboundSubjectPropertyQuery.spl";
    private static final String UNBOUND_SUBJECT_QUERY = QueryLoader.read(UNBOUND_SUBJECT_QUERY_FILE);

    private static final String BOTH_UNBOUND_QUERY_FILE = "sparql_query/BothUnboundPropertyQuery.spl";
    private static final String BOTH_UNBOUND_QUERY = QueryLoader.read(BOTH_UNBOUND_QUERY_FILE);

    private static final String SUBJECT_REPLACEMENT = "#subject#";
    private static final String PROPERTY_NAME_REPLACEMENT = "#property#";
    private static final String OBJECT_REPLACEMENT = "#object#";

    public static final String runBothBoundQuery(String subject, String propertyName, String object) {
        String queryString = BOTH_BOUND_QUERY.replace(SUBJECT_REPLACEMENT, subject).replace(PROPERTY_NAME_REPLACEMENT, propertyName).replace(OBJECT_REPLACEMENT, object);
        //System.out.println(queryString);
        return ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);
    }

    public static final List<String> runUnboundObjectQuery(String subject, String propertyName) {
        String queryString = UNBOUND_OBJECT_QUERY.replace(SUBJECT_REPLACEMENT, subject).replace(PROPERTY_NAME_REPLACEMENT, propertyName);
        //System.out.println(queryString);
        return ConformanceTestSuite.queryMany(queryString, SPATIAL_RELATIONS_MODEL);
    }

    public static final List<String> runUnboundSubjectQuery(String propertyName, String object) {
        String queryString = UNBOUND_SUBJECT_QUERY.replace(PROPERTY_NAME_REPLACEMENT, propertyName).replace(OBJECT_REPLACEMENT, object);
        //System.out.println(queryString);
        return ConformanceTestSuite.queryMany(queryString, SPATIAL_RELATIONS_MODEL);
    }

    public static final List<String> runBothUnboundQuery(String propertyName) {
        String queryString = BOTH_UNBOUND_QUERY.replace(PROPERTY_NAME_REPLACEMENT, propertyName);
        //System.out.println(queryString);
        return ConformanceTestSuite.queryMany(queryString, SPATIAL_RELATIONS_MODEL);
    }

    public static final Boolean runAssertQuery(String propertyName) {
        String queryString = BOTH_BOUND_QUERY.replace(SUBJECT_REPLACEMENT, "http://example.org/Feature#X").replace(PROPERTY_NAME_REPLACEMENT, propertyName).replace(OBJECT_REPLACEMENT, "http://example.org/Feature#Y");
        //System.out.println(queryString);
        return ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL) != null;
    }

}
