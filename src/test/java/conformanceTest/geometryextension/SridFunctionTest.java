/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import static implementation.functionregistry.RegistryLoader.load;
import implementation.support.Prefixes;
import java.util.ArrayList;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 *
 * A.3.1.5 /conf/geometry-extension/srid-function
 *
 * Requirement: /req/geometry-extension/srid-function
 * Implementations shall support geof:getSRID as a SPARQL extension
 * function.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a SPARQL query involving the
 * geof:getSRID function returns the correct result for a test dataset
 * when using the specified serialization and version.
 *
 * c.) Reference: Clause 8.7 Req 20
 *
 * d.) Test Type: Capabilities
 */
public class SridFunctionTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private ArrayList expectedList;
    private ArrayList actualList;

    @Before
    public void setUp() {
        this.expectedList = new ArrayList<>();
        this.actualList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        this.actualList.clear();
        this.expectedList.clear();
    }

    @Test
    public void positiveTest() {

        this.expectedList.add("http://www.opengis.net/def/crs/OGC/1.3/CRS84");

        String Q1 = "SELECT ((geof:getsrid ( ?aWKT )) AS ?srid) WHERE{"
                + " ex:C ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Literal literal = solution.getLiteral("?srid");
                this.actualList.add(literal.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
