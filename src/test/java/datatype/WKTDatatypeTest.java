/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import main.RDFDataLocation;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Prefixes;

/**
 *
 * @author haozhechen
 */
public class WKTDatatypeTest {

    private static Model MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(WKTDatatypeTest.class);

    public WKTDatatypeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        MODEL = ModelFactory.createDefaultModel();
        RDFDatatype wktDataType = WKTDatatype.theWKTDatatype;
        TypeMapper.getInstance().registerDatatype(wktDataType);
        MODEL.read(RDFDataLocation.SAMPLE_WKT);

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of unparse method, of class WKTDatatype.
     */
    @Test
    public void testUnparse() {
        System.out.println("unparse");
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setSize(10);
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(0, 0));
        Geometry testGeometry = gsf.createRectangle();
        RDFDatatype wktDataType = WKTDatatype.theWKTDatatype;
        LOGGER.info("test unparse result: \n{}", wktDataType.unparse(testGeometry));

    }

    /**
     * Test of parse method, of class WKTDatatype.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String queryString = "SELECT ?dWKT WHERE{ "
                + "ntu:D ntu:hasExactGeometry ?dGeom . ?dGeom geo:asWKT ?dWKT . "
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        QueryExecution qe = QueryExecutionFactory.create(query.asQuery(), MODEL);
        WKTDatatype wktDataType = WKTDatatype.theWKTDatatype;
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            // Cast the object into geometry
            LOGGER.info("WKT Literal: {}", qs.getLiteral("dWKT").getLexicalForm());
            Geometry geometry = wktDataType.parse(qs.getLiteral("dWKT").getLexicalForm());
            if (geometry != null) {
                LOGGER.info("successfully parse wktLiteral into geometry: \n{}", wktDataType.unparse(geometry));
                LOGGER.info("User Data: {}", geometry.getUserData().toString());
            }
        }

    }

}
