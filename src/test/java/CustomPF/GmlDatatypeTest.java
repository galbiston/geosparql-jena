/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomPF;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;
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

/**
 *
 * @author haozhechen
 */
public class GmlDatatypeTest {

    private static Model MODEL;
    private static final Logger LOGGER = LoggerFactory.getLogger(GmlDatatypeTest.class);

    public GmlDatatypeTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        MODEL = ModelFactory.createDefaultModel();
        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;
        TypeMapper.getInstance().registerDatatype(gmlDataType);
        LOGGER.info("Before Reading Data");
        MODEL.read("/Users/haozhechen/NetBeansProjects/GeoSPARQL/src/main/java/CustomPF/itn.rdf");
        LOGGER.info("After Reading Data");

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
     * Test of unparse method, of class GmlDatatype.
     */
    @Test
    public void testUnparse() {
        System.out.println("unparse");
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setSize(10);
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(0, 0));
        Geometry testGeometry = gsf.createRectangle();
        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;
        LOGGER.info("test unparse result: {}", gmlDataType.unparse(testGeometry));
    }

    /**
     * Test of parse method, of class GmlDatatype.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String queryString = "SELECT ?aGML ?bGML WHERE{ "
                + "ntu:A ntu:hasPointGeometry ?aGeom . ?aGeom gml:asGML ?aGML . "
                + "ntu:B ntu:hasPointGeometry ?bGeom . ?bGeom gml:asGML ?bGML . "
                + " }";

        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
        query.setNsPrefixes(Prefixes.get());

        QueryExecution qe = QueryExecutionFactory.create(query.asQuery(), MODEL);
        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            // Cast the object into geometry
            Geometry geometry = (Geometry) gmlDataType.parse(qs.getLiteral("aGML").getLexicalForm());
            if (geometry != null) {
                LOGGER.info("successfully parse gmlLiteral into geometry: {}", geometry);
            }
        }
    }

}
