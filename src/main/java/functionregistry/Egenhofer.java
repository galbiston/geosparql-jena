/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionregistry;

import geof.topological.egenhofer.filterfunction.*;
import static functionregistry.FunctionLoader.addFilterFunction;
import static functionregistry.FunctionLoader.addPropertyFunction;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import support.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class Egenhofer {

    /**
     * This method loads all the Egenhofer Topological Property Functions as
     * well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, geof.topological.egenhofer.propertyfunction.Contains.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, geof.topological.egenhofer.propertyfunction.CoveredBy.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, geof.topological.egenhofer.propertyfunction.Covers.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, geof.topological.egenhofer.propertyfunction.Disjoint.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, geof.topological.egenhofer.propertyfunction.Equals.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, geof.topological.egenhofer.propertyfunction.Inside.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, geof.topological.egenhofer.propertyfunction.Meet.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, geof.topological.egenhofer.propertyfunction.Overlap.class);
    }

    /**
     * This method loads all the Egenhofer Query Rewrite Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFunctions(PropertyFunctionRegistry registry) {

        // WKT feature to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, queryrewrite.pf.eh.wkt.featuretofeature.EHContainsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, queryrewrite.pf.eh.wkt.featuretofeature.EHCoveredByQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, queryrewrite.pf.eh.wkt.featuretofeature.EHCoversQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, queryrewrite.pf.eh.wkt.featuretofeature.EHDisjointQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, queryrewrite.pf.eh.wkt.featuretofeature.EHEqualQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, queryrewrite.pf.eh.wkt.featuretofeature.EHInsideQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, queryrewrite.pf.eh.wkt.featuretofeature.EHMeetQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, queryrewrite.pf.eh.wkt.featuretofeature.EHOverlapQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, queryrewrite.pf.eh.wkt.featuretogeometry.EHContainsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, queryrewrite.pf.eh.wkt.featuretogeometry.EHCoveredByQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, queryrewrite.pf.eh.wkt.featuretogeometry.EHCoversQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, queryrewrite.pf.eh.wkt.featuretogeometry.EHDisjointQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, queryrewrite.pf.eh.wkt.featuretogeometry.EHEqualQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, queryrewrite.pf.eh.wkt.featuretogeometry.EHInsideQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, queryrewrite.pf.eh.wkt.featuretogeometry.EHMeetQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, queryrewrite.pf.eh.wkt.featuretogeometry.EHOverlapQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, queryrewrite.pf.eh.wkt.geometrytofeature.EHContainsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, queryrewrite.pf.eh.wkt.geometrytofeature.EHCoveredByQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, queryrewrite.pf.eh.wkt.geometrytofeature.EHCoversQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, queryrewrite.pf.eh.wkt.geometrytofeature.EHDisjointQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, queryrewrite.pf.eh.wkt.geometrytofeature.EHEqualQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, queryrewrite.pf.eh.wkt.geometrytofeature.EHInsideQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, queryrewrite.pf.eh.wkt.geometrytofeature.EHMeetQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, queryrewrite.pf.eh.wkt.geometrytofeature.EHOverlapQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, queryrewrite.pf.eh.wkt.geometrytogeometry.EHContainsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, queryrewrite.pf.eh.wkt.geometrytogeometry.EHCoveredByQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, queryrewrite.pf.eh.wkt.geometrytogeometry.EHCoversQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, queryrewrite.pf.eh.wkt.geometrytogeometry.EHDisjointQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, queryrewrite.pf.eh.wkt.geometrytogeometry.EHEqualQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, queryrewrite.pf.eh.wkt.geometrytogeometry.EHInsideQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, queryrewrite.pf.eh.wkt.geometrytogeometry.EHMeetQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, queryrewrite.pf.eh.wkt.geometrytogeometry.EHOverlapQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, queryrewrite.pf.eh.gml.featuretofeature.EHContainsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, queryrewrite.pf.eh.gml.featuretofeature.EHCoveredByQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, queryrewrite.pf.eh.gml.featuretofeature.EHCoversQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, queryrewrite.pf.eh.gml.featuretofeature.EHDisjointQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, queryrewrite.pf.eh.gml.featuretofeature.EHEqualQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, queryrewrite.pf.eh.gml.featuretofeature.EHInsideQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, queryrewrite.pf.eh.gml.featuretofeature.EHMeetQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, queryrewrite.pf.eh.gml.featuretofeature.EHOverlapQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, queryrewrite.pf.eh.gml.featuretogeometry.EHContainsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, queryrewrite.pf.eh.gml.featuretogeometry.EHCoveredByQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, queryrewrite.pf.eh.gml.featuretogeometry.EHCoversQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, queryrewrite.pf.eh.gml.featuretogeometry.EHDisjointQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, queryrewrite.pf.eh.gml.featuretogeometry.EHEqualQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, queryrewrite.pf.eh.gml.featuretogeometry.EHInsideQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, queryrewrite.pf.eh.gml.featuretogeometry.EHMeetQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, queryrewrite.pf.eh.gml.featuretogeometry.EHOverlapQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, queryrewrite.pf.eh.gml.geometrytofeature.EHContainsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, queryrewrite.pf.eh.gml.geometrytofeature.EHCoveredByQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, queryrewrite.pf.eh.gml.geometrytofeature.EHCoversQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, queryrewrite.pf.eh.gml.geometrytofeature.EHDisjointQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, queryrewrite.pf.eh.gml.geometrytofeature.EHEqualQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, queryrewrite.pf.eh.gml.geometrytofeature.EHInsideQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, queryrewrite.pf.eh.gml.geometrytofeature.EHMeetQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, queryrewrite.pf.eh.gml.geometrytofeature.EHOverlapQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, queryrewrite.pf.eh.gml.geometrytogeometry.EHContainsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, queryrewrite.pf.eh.gml.geometrytogeometry.EHCoveredByQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, queryrewrite.pf.eh.gml.geometrytogeometry.EHCoversQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, queryrewrite.pf.eh.gml.geometrytogeometry.EHDisjointQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, queryrewrite.pf.eh.gml.geometrytogeometry.EHEqualQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, queryrewrite.pf.eh.gml.geometrytogeometry.EHInsideQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, queryrewrite.pf.eh.gml.geometrytogeometry.EHMeetQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, queryrewrite.pf.eh.gml.geometrytogeometry.EHOverlapQRGmlGeometryToGeometry.class);
    }

    /**
     * This method loads all the Egenhofer Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFilterFunction(registry, Vocabulary.EH_CONTAINS_NAME, Contains.class);
        addFilterFunction(registry, Vocabulary.EH_COVERED_BY_NAME, CoveredBy.class);
        addFilterFunction(registry, Vocabulary.EH_COVERS_NAME, Covers.class);
        addFilterFunction(registry, Vocabulary.EH_DISJOINT_NAME, Disjoint.class);
        addFilterFunction(registry, Vocabulary.EH_EQUALS_NAME, Equals.class);
        addFilterFunction(registry, Vocabulary.EH_INSIDE_NAME, Inside.class);
        addFilterFunction(registry, Vocabulary.EH_MEET_NAME, Meet.class);
        addFilterFunction(registry, Vocabulary.EH_OVERLAP_NAME, Overlap.class);
    }

}
