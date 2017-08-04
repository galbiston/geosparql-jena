/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.core;

import static conformance_test.ConformanceTestSuite.*;
import implementation.functionregistry.RegistryLoader;
import implementation.support.Prefixes;
import java.util.ArrayList;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 *
 * A.1.1 /conf/core/sparql-protocol Requirement: /req/core/sparql-protocol
 *
 * Implementations shall support the SPARQL Query Language for RDF [W3C SPARQL],
 * the SPARQL Protocol for RDF [W3CSPARQL Protocol] and the SPARQL Query Results
 * XML Format [W3C SPARQL Result Format].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that the implementation accepts SPARQL queries and
 * returns the correct results in the correct format, according to the SPARQL
 * Query Language for RDF, the SPARQL Protocol for RDF and SPARQL Query Results
 * XML Format W3C specifications.
 *
 * c.) Reference: Clause 6.1 Req 1 d.) Test Type: Capabilities.
 */
public class SparqlProtocolTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();
        infModel = initWktModel();
    }

    private static InfModel infModel;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void positiveTest() {

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)^^http://www.opengis.net/ont/geosparql#wktLiteral");

        String Q1 = "SELECT ?aWKT WHERE{"
                + " ex:A ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        ArrayList<String> actualList = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), infModel)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Literal literal = solution.getLiteral("?aWKT");
                actualList.add(literal.toString());
            }
        }
        assertEquals(expectedList, actualList);
    }
}
