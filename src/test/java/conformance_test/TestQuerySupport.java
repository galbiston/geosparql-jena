/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package conformance_test;

import implementation.GeoSPARQLSupport;
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

/**
 *
 *
 */
public class TestQuerySupport {

    /**
     * Sample Data in WKT
     */
    public static final String SAMPLE_DATA_WKT = "dataset/SampleData_WKT.rdf";

    /**
     * Method to load WKT test data for test queries.
     *
     * @return
     */
    public static InfModel getSampleData_WKT() {
        InputStream inputStream = getInputStream(SAMPLE_DATA_WKT);
        return GeoSPARQLSupport.prepareRDFS(inputStream);
    }

    /**
     * Sample Data in GML.
     */
    public static final String SAMPLE_DATA_GML = "dataset/SampleData_GML.rdf";

    /**
     * Method to load GML test data for test queries.
     *
     * @return
     */
    public static InfModel getSampleData_GML() {
        InputStream inputStream = getInputStream(SAMPLE_DATA_GML);
        return GeoSPARQLSupport.prepareRDFS(inputStream);
    }

    public static InputStream getInputStream(String filepath) {
        return TestQuerySupport.class.getClassLoader().getResourceAsStream(filepath);
    }

    public static List<String> queryMany(String queryString, InfModel queryModel) {
        ArrayList<String> resultList = new ArrayList<>();
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(TestPrefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), queryModel)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Iterator varIterator = solution.varNames();
                List<String> varList = new ArrayList<>();
                while (varIterator.hasNext()) {
                    String varName = (String) varIterator.next();
                    varList.add(solution.get(varName).toString());

                }
                String result = String.join(" ", varList);
                resultList.add(result);
            }
        }
        return resultList;
    }

    public static String querySingle(String queryString, InfModel queryModel) {
        List<String> resources = queryMany(queryString, queryModel);
        if (resources.size() == 1) {
            return resources.get(0);
        }
        return null;
    }

}
