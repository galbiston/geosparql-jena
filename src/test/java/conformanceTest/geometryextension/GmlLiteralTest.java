/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.INF_GML_MODEL;
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
 * * A.3.3.1 /conf/geometry-extension/gml-literal
 *
 * Requirement: /req/geometry-extension/gml-literal
 * All geo:gmlLiterals shall consist of a valid element from the GML
 * schema that implements a subtype of GM_Object as defined in [OGC
 * 07-036].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:gmlLiteral values
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.6.1 Req 15
 *
 * d.) Test Type: Capabilities
 */
public class GmlLiteralTest {

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

        this.expectedList.add("<gml:Point srsName='urn:ogc:def:crs:EPSG::27700' xmlns:gml='http://www.opengis.net/ont/gml'><gml:coordinates>-83.4,34.4</gml:coordinates></gml:Point>^^http://www.opengis.net/ont/geosparql#gmlLiteral");

        String Q1 = "SELECT ?aGML WHERE{"
                + " ex:A ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asGML ?aGML ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_GML_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Literal literal = solution.getLiteral("?aGML");
                this.actualList.add(literal.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
