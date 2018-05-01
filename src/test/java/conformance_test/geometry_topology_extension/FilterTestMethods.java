/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension;

import conformance_test.ConformanceTestSuite;
import implementation.QueryLoader;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;

/**
 *
 *
 */
public class FilterTestMethods {

    private static final InfModel SPATIAL_RELATIONS_MODEL = ConformanceTestSuite.initSpatialRelationsModel();

    private static final String BOTH_BOUND_QUERY_FILE = "sparql_query/BothBoundFilterQuery.spl";
    private static final String BOTH_BOUND_QUERY = QueryLoader.readResource(BOTH_BOUND_QUERY_FILE);

    private static final String UNBOUND_QUERY_FILE = "sparql_query/UnboundObjectFilterQuery.spl";
    private static final String UNBOUND_QUERY = QueryLoader.readResource(UNBOUND_QUERY_FILE);

    private static final String SUBJECT_REPLACEMENT = "#subject#";
    private static final String FUNCTION_NAME_REPLACEMENT = "#function#";
    private static final String OBJECT_REPLACEMENT = "#object#";

    public static final String runBoundQuery(String subject, String functionName, String object) {
        String queryString = BOTH_BOUND_QUERY.replace(SUBJECT_REPLACEMENT, subject).replace(FUNCTION_NAME_REPLACEMENT, functionName).replace(OBJECT_REPLACEMENT, object);
        //System.out.println(queryString);
        return ConformanceTestSuite.querySingle(queryString, SPATIAL_RELATIONS_MODEL);
    }

    public static final List<String> runUnboundQuery(String subject, String functionName) {
        String queryString = UNBOUND_QUERY.replace(SUBJECT_REPLACEMENT, subject).replace(FUNCTION_NAME_REPLACEMENT, functionName);
        //System.out.println(queryString);
        return ConformanceTestSuite.queryMany(queryString, SPATIAL_RELATIONS_MODEL);
    }

}
