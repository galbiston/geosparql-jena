/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geof.topological.egenhofer.filterfunction.*;
import geof.topological.egenhofer.propertyfunction.*;
import static implementation.functionregistry.FunctionLoader.addFilterFunction;
import static implementation.functionregistry.FunctionLoader.addPropertyFunction;
import implementation.support.Vocabulary;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import queryrewrite.pf.eh.gml.featuretofeature.*;
import queryrewrite.pf.eh.gml.featuretogeometry.*;
import queryrewrite.pf.eh.gml.geometrytofeature.*;
import queryrewrite.pf.eh.gml.geometrytogeometry.*;
import queryrewrite.pf.eh.wkt.featuretofeature.*;
import queryrewrite.pf.eh.wkt.featuretogeometry.*;
import queryrewrite.pf.eh.wkt.geometrytofeature.*;
import queryrewrite.pf.eh.wkt.geometrytogeometry.*;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
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
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, ehContainsPF.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, ehCoveredByPF.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, ehCoversPF.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, ehDisjointPF.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, ehEqualsPF.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, ehInsidePF.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, ehMeetPF.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, ehOverlapPF.class);
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
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, EHContainsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, EHCoveredByQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, EHCoversQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, EHDisjointQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, EHEqualQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, EHInsideQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, EHMeetQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, EHOverlapQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, EHContainsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, EHCoveredByQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, EHCoversQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, EHDisjointQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, EHEqualQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, EHInsideQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, EHMeetQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, EHOverlapQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, EHContainsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, EHCoveredByQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, EHCoversQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, EHDisjointQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, EHEqualQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, EHInsideQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, EHMeetQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, EHOverlapQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, EHContainsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, EHCoveredByQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, EHCoversQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, EHDisjointQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, EHEqualQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, EHInsideQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, EHMeetQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, EHOverlapQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, EHContainsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, EHCoveredByQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, EHCoversQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, EHDisjointQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, EHEqualQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, EHInsideQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, EHMeetQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, EHOverlapQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, EHContainsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, EHCoveredByQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, EHCoversQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, EHDisjointQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, EHEqualQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, EHInsideQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, EHMeetQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, EHOverlapQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, EHContainsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, EHCoveredByQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, EHCoversQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, EHDisjointQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, EHEqualQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, EHInsideQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, EHMeetQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, EHOverlapQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.EH_CONTAINS_NAME, EHContainsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERED_BY_NAME, EHCoveredByQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_COVERS_NAME, EHCoversQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_DISJOINT_NAME, EHDisjointQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_EQUALS_NAME, EHEqualQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_INSIDE_NAME, EHInsideQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_MEET_NAME, EHMeetQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.EH_OVERLAP_NAME, EHOverlapQRGmlGeometryToGeometry.class);
    }

    /**
     * This method loads all the Egenhofer Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFilterFunction(registry, Vocabulary.EH_CONTAINS_NAME, ehContainsFF.class);
        addFilterFunction(registry, Vocabulary.EH_COVERED_BY_NAME, ehCoveredByFF.class);
        addFilterFunction(registry, Vocabulary.EH_COVERS_NAME, ehCoversFF.class);
        addFilterFunction(registry, Vocabulary.EH_DISJOINT_NAME, ehDisjointFF.class);
        addFilterFunction(registry, Vocabulary.EH_EQUALS_NAME, ehEqualsFF.class);
        addFilterFunction(registry, Vocabulary.EH_INSIDE_NAME, ehInsideFF.class);
        addFilterFunction(registry, Vocabulary.EH_MEET_NAME, ehMeetFF.class);
        addFilterFunction(registry, Vocabulary.EH_OVERLAP_NAME, ehOverlapFF.class);
    }

}
