/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension;

import conformance_test.ConformanceTestSuite;
import conformance_test.QueryLoader;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;

/**
 *
 *
 */
public class FilterTestMethods {

    private static final InfModel SPATIAL_RELATIONS_MODEL = ConformanceTestSuite.initSpatialRelationsModel();

    private static final String BOUND_QUERY_FILE = "sparql_query/BoundTargetFilterQuery.spl";
    private static final String BOUND_QUERY = QueryLoader.read(BOUND_QUERY_FILE);

    private static final String UNBOUND_QUERY_FILE = "sparql_query/UnboundTargetFilterQuery.spl";
    private static final String UNBOUND_QUERY = QueryLoader.read(UNBOUND_QUERY_FILE);

    private static final String SOURCE_REPLACEMENT = "#source#";
    private static final String FUNCTION_NAME_REPLACEMENT = "#function#";
    private static final String TARGET_REPLACEMENT = "#target#";

    public static final String runBoundQuery(String source, String functionName, String target) {
        String queryString = BOUND_QUERY.replace(SOURCE_REPLACEMENT, source).replace(FUNCTION_NAME_REPLACEMENT, functionName).replace(TARGET_REPLACEMENT, target);
        //System.out.println(queryString);
        return ConformanceTestSuite.resourceSingleQuery(queryString, SPATIAL_RELATIONS_MODEL);
    }

    public static final List<String> runUnboundQuery(String source, String functionName) {
        String queryString = UNBOUND_QUERY.replace(SOURCE_REPLACEMENT, source).replace(FUNCTION_NAME_REPLACEMENT, functionName);
        //System.out.println(queryString);
        return ConformanceTestSuite.resourceQuery(queryString, SPATIAL_RELATIONS_MODEL);
    }

}
