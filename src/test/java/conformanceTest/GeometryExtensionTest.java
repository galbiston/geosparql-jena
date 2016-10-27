/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import static conformanceTest.ConformanceTestSuite.initModels;
import static main.Main.init;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A.3Conformance Class: Geometry Extension (serialization, version)
 * Conformance Class URI: /conf/geometry-extension.
 */
public class GeometryExtensionTest {

    public GeometryExtensionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the GeoSPARQL functionalities
         */
        init();

        /**
         * Initialize all the models
         */
        initModels();
    }

    /**
     * A.3.1 Tests for all Serializations
     * A.3.1.1 /conf/geometry-extension/geometry-class
     *
     * Requirement: /req/geometry-extension/geometry-class
     * Implementations shall allow the RDFS class geo:Geometry to be used in
     * SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving geo:Geometry return
     * the correct result on a test dataset.
     *
     * c.) Reference: Clause 8.2.1 Req 7 d.) Test Type: Capabilities
     */
    @Test
    public void geometryClassTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.geometryClassTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.1.2 /conf/geometry-extension/feature-properties
     *
     * Requirement: /req/geometry-extension/feature-properties
     * Implementations shall allow the properties geo:hasGeometry and
     * geo:hasDefaultGeometry to be used in SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that queries involving these properties
     * return the correct result for a test dataset.
     *
     * c.) Reference: Clause 8.3 Req 8
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void featurePropertiesTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.featurePropertiesTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.1.3 /conf/geometry-extension/geometry-properties
     *
     * Requirement: /req/geometry-extension/geometry-properties
     * Implementations shall allow the properties geo:dimension,
     * geo:coordinateDimension, geo:spatialDimension, geo:isEmpty,
     * geo:isSimple, geo:hasSerialization to be used in SPARQL graph
     * patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that queries involving these properties
     * return the correct result for a test dataset.
     *
     * c.) Reference: Clause 8.4 Req 9
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void geometryPropertiesTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.geometryPropertiesTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.1.4 /conf/geometry-extension/query-functions
     *
     * Requirement: /req/geometry-extension/query-functions
     * Implementations shall support geof:distance, geof:buffer,
     * geof:convexHull, geof:intersection, geof:union, geof:difference,
     * geof:symDifference, geof:envelope and geof:boundary as SPARQL
     * extension functions, consistent with the definitions of the
     * corresponding functions (distance, buffer, convexHull, intersection,
     * difference, symDifference, envelope and boundary respectively) in
     * Simple Features [ISO 19125-1].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Verify that a set of SPARQL queries involving each
     * of the following functions returns the correct result for a test
     * dataset when using the specified serialization and version:
     * geof:distance, geof:buffer, geof:convexHull, geof:intersection,
     * geof:union, geof:difference, geof:symDifference, geof:envelope and
     * geof:boundary.
     *
     * c.) Reference: Clause 8.7 Req 19
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void queryFunctionsTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.queryFunctionsTestQueries(), INF_WKT_MODEL);

    }

    /**
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
    @Test
    public void sridFunctionTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.sridFunctionTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.2 serialization = WKT
     * A.3.2.1 /conf/geometry-extension/wkt-literal
     *
     * Requirement: /req/geometry-extension/wkt-literal
     * All RDFS Literals of type geo:wktLiteral shall consist of an optional
     * URI identifying the coordinate reference system followed by Simple
     * Features Well Known Text (WKT) describing a geometric value, Valid
     * geo:wktLiterals are formed by concatenating a valid, absolute URI as
     * defined in [RFC 2396], one or more spaces (Unicode U+0020 character)
     * as a separator, and a WKT string as defined in Simple Features [ISO
     * 19125-1].
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving geo:wktLiteral values
     * return the correct result for a test dataset.
     *
     * c.) Reference: Clause 8.5.1 Req 10
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void wktLiteralTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.wktLiteralTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.2.2 /conf/geometry-extension/wkt-literal-default-srs
     *
     * Requirement: /req/geometry-extension/wkt-literal-default-srs
     * The URI <http://www.opengis.net/def/crs/OGC/1.3/CRS84> shall be
     * assumed as the spatial reference system for geo:wktLiterals that do
     * not specify an explicit spatial reference system URI.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving geo:wktLiteral values
     * without an explicit encoded spatial reference system URI return the
     * correct result for a test dataset.
     *
     * c.) Reference: Clause 8.5.1 Req 11
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void wktLiteralDefaultSrsTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.wktLiteralDefaultSrsTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.2.3 /conf/geometry-extension/wkt-axis-order
     *
     * Requirement: /req/geometry-extension/wkt-axis-order
     * Coordinate tuples within geo:wktLiterals shall be interpreted using
     * the axis order defined in the spatial reference system used.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving geo:wktLiteral values
     * return the correct result for a test dataset.
     *
     * c.) Reference: Clause 8.5.1 Req 12
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void wktAxisOrderTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.wktAxisOrderTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.2.4 /conf/geometry-extension/wkt-literal-empty
     *
     * Requirement: /req/geometry-extension/wkt-literal-empty
     * An empty RDFS Literal of type geo:wktLiteral shall be interpreted as
     * an empty geometry.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving empty geo:wktLiteral
     * values return the correct result for a test dataset.
     *
     * c.) Reference: Clause 8.5.1 Req 13
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void wktLiteralEmptyTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.wktLiteralEmptyTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.2.5 /conf/geometry-extension/geometry-as-wkt-literal
     *
     * Requirement: /req/geometry-extension/geometry-as-wkt-literal
     * Implementations shall allow the RDF property geo:asWKT to be used in
     * SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving the geo:asWKT property
     * return the correct result for a test dataset.
     *
     * c.) Reference: Clause 8.5.2 Req 14
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void geometryAsWktLiteralTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.geometryAsWktLiteralTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.3 serialization = GML
     * A.3.3.1 /conf/geometry-extension/gml-literal
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
    @Test
    public void gmlLiteralTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.gmlLiteralTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.3.2 /conf/geometry-extension/gml-literal-empty
     *
     * Requirement: /req/geometry-extension/gml-literal-empty
     * An empty geo:gmlLiteral shall be interpreted as an empty geometry.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving empty geo:gmlLiteral
     * values return the correct result for a test dataset.
     *
     * c.) Reference: Clause 8.6.1 Req 16
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void gmlLiteralEmptyTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.gmlLiteralEmptyTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.3.3 /conf/geometry-extension/gml-profile
     *
     * Requirement: /req/geometry-extension/gml-profile
     * Implementations shall document supported GML profiles.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: Examine the implementation’s documentation to verify
     * that the supported GML profiles are documented.
     *
     * c.) Reference: Clause 8.6.1 Req 17
     *
     * d.) Test Type: Documentation
     */
    @Test
    public void gmlProfileTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.gmlProfileTestQueries(), INF_WKT_MODEL);

    }

    /**
     * A.3.3.4 /conf/geometry-extension/geometry-as-gml-literal
     *
     * Requirement: /req/geometry-extension/geometry-as-gml-literal
     * Implementations shall allow the RDF property geo:asGML to be used in
     * SPARQL graph patterns.
     *
     * a.) Test purpose: check conformance with this requirement
     *
     * b.) Test method: verify that queries involving the geo:asGML property
     * return the correct result for a test dataset.
     *
     * c.) Reference: Clause 8.6.2 Req 18
     *
     * d.) Test Type: Capabilities
     */
    @Test
    public void geometryAsGmlLiteralTest() {

        ConformanceTestSuite.evaluateQuery(ConformanceTestSuite.geometryAsGmlLiteralTestQueries(), INF_WKT_MODEL);

    }

}
