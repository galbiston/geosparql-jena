/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension;

import conformance_test.TestQuerySupport;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;

/**
 *
 *
 */
public class FilterTestMethods {

    private static final InfModel SAMPLE_DATA_MODEL = TestQuerySupport.getSampleData_WKT();

    private static final String BOTH_BOUND_QUERY_STRING = "SELECT ?geom\n"
            + "WHERE{\n"
            + "    <#subject#> geo:asWKT ?subjectLit .\n"
            + "    <#object#> geo:asWKT ?objectLit .\n"
            + "    BIND(<#object#> AS ?geom) .\n"
            + "    FILTER(#function#(?subjectLit, ?objectLit)) .\n"
            + "}ORDER by ?geom";

    private static final String UNBOUND_OBJECT_QUERY_STRING = "SELECT ?geom\n"
            + "WHERE{\n"
            + "    <#subject#> geo:asWKT ?subjectLit .\n"
            + "    ?geom geo:asWKT ?objectLit .\n"
            + "    FILTER(#function#(?subjectLit, ?objectLit)) .\n"
            + "}ORDER BY ?geom";

    private static final String SUBJECT_REPLACEMENT = "#subject#";
    private static final String FUNCTION_NAME_REPLACEMENT = "#function#";
    private static final String OBJECT_REPLACEMENT = "#object#";

    public static final String runBoundQuery(String subject, String functionName, String object) {
        String queryString = BOTH_BOUND_QUERY_STRING.replace(SUBJECT_REPLACEMENT, subject).replace(FUNCTION_NAME_REPLACEMENT, functionName).replace(OBJECT_REPLACEMENT, object);
        //System.out.println(queryString);
        return TestQuerySupport.querySingle(queryString, SAMPLE_DATA_MODEL);
    }

    public static final List<String> runUnboundQuery(String subject, String functionName) {
        String queryString = UNBOUND_OBJECT_QUERY_STRING.replace(SUBJECT_REPLACEMENT, subject).replace(FUNCTION_NAME_REPLACEMENT, functionName);
        //System.out.println(queryString);
        return TestQuerySupport.queryMany(queryString, SAMPLE_DATA_MODEL);
    }

}
