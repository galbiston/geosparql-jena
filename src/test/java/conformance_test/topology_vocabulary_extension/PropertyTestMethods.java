/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.topology_vocabulary_extension;

import conformance_test.TestQuerySupport;
import implementation.support.QueryLoader;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;

/**
 *
 *
 */
public class PropertyTestMethods {

    private static final InfModel SAMPLE_DATA_MODEL = TestQuerySupport.getSampleData_WKT();

    private static final String BOTH_BOUND_QUERY_FILE = "sparql_query/BothBoundPropertyQuery.spl";
    private static final String BOTH_BOUND_QUERY = QueryLoader.readResource(BOTH_BOUND_QUERY_FILE);

    private static final String UNBOUND_QUERY_FILE = "sparql_query/UnboundObjectPropertyQuery.spl";
    private static final String UNBOUND_QUERY = QueryLoader.readResource(UNBOUND_QUERY_FILE);

    private static final String SUBJECT_REPLACEMENT = "#subject#";
    private static final String PROPERTY_NAME_REPLACEMENT = "#property#";
    private static final String OBJECT_REPLACEMENT = "#object#";

    public static final String runBoundQuery(String subject, String propertyName, String object) {
        String queryString = BOTH_BOUND_QUERY.replace(SUBJECT_REPLACEMENT, subject).replace(PROPERTY_NAME_REPLACEMENT, propertyName).replace(OBJECT_REPLACEMENT, object);
        //System.out.println(queryString);
        return TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);
    }

    public static final List<String> runUnboundQuery(String source, String propertyName) {
        String queryString = UNBOUND_QUERY.replace(SUBJECT_REPLACEMENT, source).replace(PROPERTY_NAME_REPLACEMENT, propertyName);
        //System.out.println(queryString);
        return TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);
    }

}
