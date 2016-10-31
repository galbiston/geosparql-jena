/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest;

import java.util.ArrayList;
import java.util.Iterator;
import implementation.support.RDFDataLocation;
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
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import implementation.support.Prefixes;

/**
 *
 * @author haozhechen
 */
public class ConformanceTestSuite {

    /**
     * Private Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConformanceTestSuite.class);

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * Inference WKT model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_WKT_MODEL;

    /**
     * This negative model facilitates the test for empty geometries and the WKT
     * literal without a specified SRID.
     */
    public static Model NEGATIVE_WKT_MODEL;

    /**
     * Default GML model - with no inference support.
     */
    public static Model DEFAULT_GML_MODEL;

    /**
     * Inference GML model enables the import with the GeoSPARQL ontology as an
     * OWL reasoner, use this model to get the fully compliance of GeoSPARQL.
     */
    public static InfModel INF_GML_MODEL;

    /**
     * This negative model facilitates the test for empty geometries and the GML
     * literal without a specified SRID.
     */
    public static Model NEGATIVE_GML_MODEL;

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A1.1.
     *
     * @return The test query list.
     */
    public static final ArrayList sparqlProtocolTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Given any SPARQL query, the query result should be
         * returned in specified format Test type: positive test Test Data Type:
         * point
         */
        list.add("SELECT * WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A1.2.
     *
     * @return The test query list.
     */
    public static final ArrayList spatialObjectClassTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Ask for all the Spatial Objects, all the Spatial
         * Objects and its sub-classes should be returned Test type: positive
         * test Test Data Type: not applicable
         */
        list.add("SELECT ?object WHERE{"
                + " ?object rdf:type geo:SpatialObject ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A1.3.
     *
     * @return The test query list.
     */
    public static final ArrayList featureClassTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Ask for all the Features, all the Features and its
         * sub-classes should be returned Test type: positive test Test Data
         * Type: not applicable
         */
        list.add("SELECT ?feature WHERE{"
                + " ?feature rdf:type geo:Feature ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.2.1.1.
     *
     * @return The test query list.
     */
    public static final ArrayList sfSpatialRelationsTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geo:sfEquals, the returned result should
         * be ntu:A Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfEquals ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:sfEquals, the returned result should
         * NOT be ntu:A Test type: negative test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfDisjoint ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:sfIntersects, the returned result
         * should be ntu:A, ntu:B, ntu:C, ntu:D, ntu:E, and ntu:F Test type:
         * positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfIntersects ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:sfTouches, the returned result should
         * be ntu:E Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfTouches ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:sfCrosses, the returned result should
         * be ntu:B Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfCrosses ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:sfWithin, the returned result should
         * be ntu:A and ntu:D Test type: positive test Test Data Type: polygon
         * to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfWithin ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:sfContains, the returned result should
         * be ntu:C Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfContains ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:sfOverlaps, the returned result should
         * be ntu:D and ntu:F Test type: positive test Test Data Type: polygon
         * to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfOverlaps ?aWKT ."
                + "}");
        return list;

    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.2.2.1.
     *
     * @return The test query list.
     */
    public static final ArrayList ehSpatialRelationsTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geo:ehEquals, the returned result should
         * be ntu:A Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?bGeom ."
                + " ?bGeom geo:asWKT ?bWKT ."
                + "?bWKT geo:ehEquals ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:ehDisjoint, the returned result should
         * NOT be ntu:A Test type: negative test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:ehDisjoint ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:ehMeet, the returned result should be
         * ntu:E Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:ehMeet ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:ehOverlaps, the returned result should
         * be ntu:D and ntu:F Test type: positive test Test Data Type: polygon
         * to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:ehOverlaps ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:ehCovers, the returned result should
         * be ntu:D Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?aWKT geo:ehCovers ?WKT ."
                + "}");
        /**
         * Test Description: Test for geo:ehCoveredBy, the returned result
         * should be ntu:D Test type: positive test Test Data Type: polygon to
         * all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:ehCoveredBy ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:ehInside, the returned result should
         * be ntu:A Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:ehInside ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:ehContains, the returned result should
         * be ntu:C Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:ehContains ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.2.3.1.
     *
     * @return The test query list.
     */
    public static final ArrayList rcc8SpatialRelationsTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geo:rcc8eq, the returned result should be
         * ntu:A Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:rcc8eq ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:rcc8dc, the returned result should NOT
         * be ntu:A Test type: negative test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:rcc8dc ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:rcc8ec, the returned result should be
         * ntu:E Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:rcc8ec ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:rcc8po, the returned result should be
         * ntu:F Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:rcc8po ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:rcc8tppi, the returned result should
         * be ntu:D Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?aWKT geo:rcc8tppi ?WKT ."
                + "}");
        /**
         * Test Description: Test for geo:rcc8tpp, the returned result should be
         * ntu:D Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:rcc8tpp ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:rcc8ntpp, the returned result should
         * be ntu:A Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:rcc8ntpp ?aWKT ."
                + "}");
        /**
         * Test Description: Test for geo:rcc8ntppi, the returned result should
         * be ntu:C Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:rcc8ntppi ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.1.1.
     *
     * @return The test query list.
     */
    public static final ArrayList geometryClassTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Ask for all the Geometries, all the Geometries and
         * its sub-classes such as Point, Line String, and Polygon should be
         * returned Test type: positive test Test Data Type: not applicable
         */
        list.add("SELECT ?geometry WHERE{"
                + " ?geometry rdf:type geo:Geometry ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.1.2.
     *
     * @return The test query list.
     */
    public static final ArrayList featurePropertiesTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geo:hasGeometry and
         * geo:hasDefaultGeometry, the returned result should be ntu:A Test
         * type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place geo:hasDefaultGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "?WKT geo:sfEquals ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.1.3.
     *
     * @return The test query list.
     */
    public static final ArrayList geometryPropertiesTestQueries() {
        ArrayList<String> list = new ArrayList<>();
        /**
         * Test Description: Test for geometry properties: geo:dimension,
         * geo:coordinateDimension, geo:spatialDimension, geo:isEmpty, and
         * geo:isSimple, and the returned result should be: 0, 0, 0, false, and
         * true Test type: positive test Test Data Type: not applicable
         */
        list.add("SELECT ?dimension ?coordinateDimension ?spatialDimension ?isEmpty ?isSimple WHERE{"
                + " ntu:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:dimension ?dimension ."
                + " ?aGeom geo:coordinateDimension ?coordinateDimension ."
                + " ?aGeom geo:spatialDimension ?spatialDimension ."
                + " ?aGeom geo:isEmpty ?isEmpty ."
                + " ?aGeom geo:isSimple ?isSimple ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.1.4.
     *
     * @return The test query list.
     */
    public static final ArrayList queryFunctionsTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geof:distance by asking the 3 closest
         * geometries to geometry ntu:E, the returned result order should be
         * ntu:C, ntu:F, and ntu:B Test type: positive test Test Data Type:
         * polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( ?WKT != ?aWKT )"
                + "}"
                + "ORDER BY ASC ( geof:distance( ?aWKT, ?WKT, uom:metre ) )"
                + "LIMIT 3");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.1.5.
     *
     * @return The test query list.
     */
    public static final ArrayList sridFunctionTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geof:getSRID by asking the SRID of ntu:C,
         * the returned result should be
         * "http://www.opengis.net/def/crs/OGC/1.3/CRS84" Test type: positive
         * test Test Data Type: polygon
         */
        list.add("SELECT ((geof:getsrid ( ?aWKT )) AS ?srid) WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.2.1.
     *
     * @return The test query list.
     */
    public static final ArrayList wktLiteralTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for WKT Literal format, the correct WKT
         * Literal format should contain a CRS URI, one or more spaces as
         * separator, and a valid WKT string defined by Simple Features Test
         * type: manual test Test Data Type: polygon
         */
        list.add("SELECT ?aWKT WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.2.2.
     *
     * @return The test query list.
     */
    public static final ArrayList wktLiteralDefaultSrsTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for default CRS URI return, for the WKT
         * Literals have NOT specified a CRS URI, the returned result should be
         * assumed as "http://www.opengis.net/def/crs/OGC/1.3/CRS84" Test type:
         * positive test Test Data Type: polygon
         */
        list.add("SELECT (geof:getSRID ( ?aWKT )) AS ?srid WHERE{"
                + " ntu:B ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.2.3.
     *
     * @return The test query list.
     */
    public static final ArrayList wktAxisOrderTestQueries() {
        ArrayList<String> list = new ArrayList<>();
        /**
         * Test Description: For the default CRS84 aka the WGS84, coordinate
         * tuples within the geo:wktLiteral is interpreted using the axis order
         * defined by this CRS Test type: manual test Test Data Type: polygon
         */
        list.add("SELECT ?aWKT WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.2.4.
     *
     * @return The test query list.
     */
    public static final ArrayList wktLiteralEmptyTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for empty WKT Literal return, for the empty
         * WKT Literals, an empty geometry should be returned Test type:
         * positive test Test Data Type: empty
         */
        list.add("SELECT ?aWKT WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.2.5.
     *
     * @return The test query list.
     */
    public static final ArrayList geometryAsWktLiteralTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geo:asWKT, queries using geo:asWKT should
         * return the corresponding WKT Literal, for this test, the returned WKT
         * should be: <http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4
         * 34.4) Test type: positive test Test Data Type: point
         */
        list.add("SELECT ?aWKT WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.3.1.
     *
     * @return The test query list.
     */
    public static final ArrayList gmlLiteralTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for GML Literal validation, the returned GML
         * Literal should consist of a valid element from the GML schema Test
         * type: manual test Test Data Type: point
         */
        list.add("SELECT ?aGML WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asGML ?aGML ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.3.2.
     *
     * @return The test query list.
     */
    public static final ArrayList gmlLiteralEmptyTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for empty GML Literal return, for the empty
         * GML Literals, an empty geometry should be returned Test type:
         * positive test Test Data Type: empty
         */
        list.add("SELECT ?aGML WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asGML ?aGML ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.3.3.
     *
     * @return The test query list.
     */
    public static final ArrayList gmlProfileTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Implementations shall document supported GML
         * profiles Test type: documentation Test Data Type: not applicable
         */
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.3.3.4.
     *
     * @return The test query list.
     */
    public static final ArrayList geometryAsGmlLiteralTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geo:asGML, queries using geo:asGML should
         * return the corresponding GML Literal, for this test, the returned GML
         * should be:
         * <gml:Point srsName='urn:ogc:def:crs:EPSG::27700' xmlns:gml='http://www.opengis.net/ont/gml'>
         * <gml:coordinates>-83.4,34.4</gml:coordinates>
         * </gml:Point>
         * Test type: positive test Test Data Type: point
         */
        list.add("SELECT ?aGML WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asGML ?aGML ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.4.1.1.
     *
     * @return The test query list.
     */
    public static final ArrayList relateQueryFunctionTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geof:relate function, for comparision we
         * use the intersection matrix of sfEquals "TFFFTFFFT", it should return
         * same result as using sfEquals, in this case: ntu:A Test type:
         * positive test Test Data Type: point
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:relate(?WKT, ?aWKT, TFFFTFFFT ) )"
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.4.2.1.
     *
     * @return The test query list.
     */
    public static final ArrayList sfQueryFunctionsTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geof:sfEquals, the returned result should
         * be ntu:A Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:sfEquals(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:sfDisjoint, the returned result
         * should NOT be ntu:A Test type: negative test Test Data Type: point to
         * all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:sfDisjoint(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:sfIntersects, the returned result
         * should be ntu:A, ntu:B, ntu:C, ntu:D, ntu:E, and ntu:F Test type:
         * positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:sfIntersects(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:sfTouches, the returned result should
         * be ntu:E Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:sfTouches(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:sfCrosses, the returned result should
         * be ntu:B Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:sfCrosses(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:sfWithin, the returned result should
         * be ntu:A and ntu:D Test type: positive test Test Data Type: polygon
         * to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:sfWithin(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:sfContains, the returned result
         * should be ntu:C Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:sfContains(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:sfOverlaps, the returned result
         * should be ntu:D and ntu:F Test type: positive test Test Data Type:
         * polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:sfOverlaps(?WKT, ?aWKT))"
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.4.3.1.
     *
     * @return The test query list.
     */
    public static final ArrayList ehQueryFunctionsTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geof:ehEquals, the returned result should
         * be ntu:A Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:ehEquals(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:ehDisjoint, the returned result
         * should NOT be ntu:A Test type: negative test Test Data Type: point to
         * all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:ehDisjoint(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:ehMeet, the returned result should be
         * ntu:E Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:ehMeet(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:ehOverlaps, the returned result
         * should be ntu:D and ntu:F Test type: positive test Test Data Type:
         * polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:ehOverlaps(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:ehCovers, the returned result should
         * be ntu:D Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:ehCovers(?aWKT, ?WKT))"
                + "}");
        /**
         * Test Description: Test for geo:ehCoveredBy, the returned result
         * should be ntu:D Test type: positive test Test Data Type: polygon to
         * all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:ehCoveredBy(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geo:ehInside, the returned result should
         * be ntu:A Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:ehInside(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geo:ehContains, the returned result should
         * be ntu:C Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:ehContains(?WKT, ?aWKT))"
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.4.4.1.
     *
     * @return The test query list.
     */
    public static final ArrayList rcc8QueryFunctionsTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geof:rcc8eq, the returned result should be
         * ntu:A Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:rcc8eq(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:rcc8dc, the returned result should
         * NOT be ntu:A Test type: negative test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:rcc8dc(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:rcc8ec, the returned result should be
         * ntu:E Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:rcc8ec(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:rcc8po, the returned result should be
         * ntu:F Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:rcc8po(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:rcc8tppi, the returned result should
         * be ntu:D Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:rcc8tppi(?aWKT, ?WKT))"
                + "}");
        /**
         * Test Description: Test for geof:rcc8tpp, the returned result should
         * be ntu:D Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:rcc8tpp(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:rcc8ntpp, the returned result should
         * be ntu:A Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:C ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:rcc8ntpp(?WKT, ?aWKT))"
                + "}");
        /**
         * Test Description: Test for geof:rcc8ntppi, the returned result should
         * be ntu:C Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "?place ntu:hasExactGeometry ?Geom ."
                + " ?Geom geo:asWKT ?WKT ."
                + "FILTER ( geof:rcc8ntppi(?WKT, ?aWKT))"
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.5.1.1.
     *
     * @return The test query list.
     */
    public static final ArrayList bgpRdfsEntTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         *
         */
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.5.2.1.
     *
     * @return The test query list.
     */
    public static final ArrayList wktGeometryTypesTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         *
         */
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.5.3.1.
     *
     * @return The test query list.
     */
    public static final ArrayList gmlGeometryTypesTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         *
         */
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.6.1.1.
     *
     * @return The test query list.
     */
    public static final ArrayList sfQueryRewriteTestQueries() {
        ArrayList<String> list = new ArrayList<>();

        /**
         * Test Description: Test for geor:sfEquals, the returned result should
         * be ntu:A Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?Geom ."
                + "?place geor:sfEquals ntu:A ."
                + "}");
        /**
         * Test Description: Test for geor:sfEquals, the returned result should
         * NOT be ntu:A Test type: negative test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?Geom ."
                + "?place geor:sfDisjoint ntu:A ."
                + "}");
        /**
         * Test Description: Test for geor:sfIntersects, the returned result
         * should be ntu:A, ntu:B, ntu:C, ntu:D, ntu:E, and ntu:F Test type:
         * positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?Geom ."
                + "?place geo:sfIntersects ntu:C ."
                + "}");
        /**
         * Test Description: Test for geor:sfTouches, the returned result should
         * be ntu:E Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?Geom ."
                + "?place geor:sfTouches ntu:C ."
                + "}");
        /**
         * Test Description: Test for geor:sfCrosses, the returned result should
         * be ntu:B Test type: positive test Test Data Type: polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?Geom ."
                + "?place geor:sfCrosses ntu:C ."
                + "}");
        /**
         * Test Description: Test for geor:sfWithin, the returned result should
         * be ntu:A and ntu:D Test type: positive test Test Data Type: polygon
         * to all
         */
        list.add("SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?Geom ."
                + "?place geor:sfWithin ntu:C ."
                + "}");
        /**
         * Test Description: Test for geor:sfContains, the returned result
         * should be ntu:C Test type: positive test Test Data Type: point to all
         */
        list.add("SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?Geom ."
                + "?place geor:sfContains ntu:A ."
                + "}");
        /**
         * Test Description: Test for geor:sfOverlaps, the returned result
         * should be ntu:D and ntu:F Test type: positive test Test Data Type:
         * polygon to all
         */
        list.add("SELECT ?place WHERE{"
                + "?place ntu:hasExactGeometry ?Geom ."
                + "?place geor:sfOverlaps ntu:C ."
                + "}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.6.2.1.
     *
     * @return The test query list.
     */
    public static final ArrayList ehQueryRewriteTestQueries() {
        ArrayList<String> list = new ArrayList<>();
        list.add("SELECT ?object WHERE{ ?object rdf:type geo:Feature .}");
        return list;
    }

    /**
     * This list contains all the queries that need to be queried against
     * GeoSPARQL conformance test A.6.3.1.
     *
     * @return The test query list.
     */
    public static final ArrayList rcc8QueryRewriteTestQueries() {
        ArrayList<String> list = new ArrayList<>();
        list.add("SELECT ?object WHERE{ ?object rdf:type geo:Feature .}");
        return list;
    }

    /**
     * This function iterate through the input query list and execute each query
     * inside the list.
     *
     * @param queryList - A list that contains all the test queries.
     * @param MODEL - The data model that need to be queried against.
     */
    public static void evaluateQuery(ArrayList queryList, Model MODEL) {

        Iterator itr = queryList.iterator();
        while (itr.hasNext()) {
            String queryString = (String) itr.next();
            QuerySolutionMap bindings = new QuerySolutionMap();
            ParameterizedSparqlString query = new ParameterizedSparqlString(queryString, bindings);
            query.setNsPrefixes(Prefixes.get());

            try (QueryExecution qExec = QueryExecutionFactory.create(query.asQuery(), MODEL)) {
                LOGGER.info("\n" + query.asQuery().toString());
                ResultSet rs = qExec.execSelect();
                ResultSetFormatter.out(rs);
            }
        }
    }

    /**
     * This method initialize all the test models, need to be called before
     * query execution.
     */
    public static void initModels() {

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
}
