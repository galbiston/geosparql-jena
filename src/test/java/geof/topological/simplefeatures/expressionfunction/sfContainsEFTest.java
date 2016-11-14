/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.simplefeatures.expressionfunction;

import conformanceTest.RDFDataLocation;
import implementation.support.Prefixes;
import implementation.vocabulary.Geof;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.function.user.UserDefinedFunctionFactory;
import org.apache.jena.sparql.lang.sparql_11.ParseException;
import org.apache.jena.util.FileManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 */
public class sfContainsEFTest {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * Inference WKT model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_WKT_MODEL;

    public sfContainsEFTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ParseException {
        List<Var> args = new ArrayList<>();
        args.add(Var.alloc("left"));
        args.add(Var.alloc("right"));

        UserDefinedFunctionFactory.getFactory().add(Geof.SF_CONTAINS, new sfContainsEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFunction() {

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:sfContains(?aWKT, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral> ) ."
                + "}";

        QuerySolutionMap bindings = new QuerySolutionMap();

        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet rs = qExec.execSelect();
            ResultSetFormatter.out(rs);
        }

    }
}
