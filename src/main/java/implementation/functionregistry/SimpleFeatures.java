/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geof.topological.simplefeatures.filterfunction.*;
import geof.topological.simplefeatures.propertyfunction.*;
import static implementation.functionregistry.FunctionLoader.addFilterFunction;
import static implementation.functionregistry.FunctionLoader.addPropertyFunction;
import implementation.vocabulary.General;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import queryrewrite.pf.sf.gml.featuretofeature.*;
import queryrewrite.pf.sf.gml.featuretogeometry.*;
import queryrewrite.pf.sf.gml.geometrytofeature.*;
import queryrewrite.pf.sf.gml.geometrytogeometry.*;
import queryrewrite.pf.sf.wkt.featuretofeature.*;
import queryrewrite.pf.sf.wkt.featuretogeometry.*;
import queryrewrite.pf.sf.wkt.geometrytofeature.*;
import queryrewrite.pf.sf.wkt.geometrytogeometry.*;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class SimpleFeatures {

    /**
     * This method loads all the Simple Feature Topological Property Functions
     * as well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, sfContainsPF.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, sfCrossesPF.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, sfDisjointPF.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, sfEqualsPF.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, sfIntersectsPF.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, sfOverlapsPF.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, sfTouchesPF.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, sfWithinPF.class);
    }

    /**
     * This method loads all the Simple Feature Query Rewrite Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFunctions(PropertyFunctionRegistry registry) {

        // WKT feature to feature query rewrite functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, SFContainsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, SFCrossesQRWktFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, SFDisjointQRWktFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, SFEqualsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, SFIntersectsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, SFOverlapsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, SFTouchesQRWktFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, SFWithinQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, SFContainsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, SFCrossesQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, SFDisjointQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, SFEqualsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, SFIntersectsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, SFOverlapsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, SFTouchesQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, SFWithinQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, SFContainsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, SFCrossesQRWktGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, SFDisjointQRWktGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, SFEqualsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, SFIntersectsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, SFOverlapsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, SFTouchesQRWktGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, SFWithinQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, SFContainsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, SFCrossesQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, SFDisjointQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, SFEqualsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, SFIntersectsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, SFOverlapsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, SFTouchesQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, SFWithinQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, SFContainsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, SFCrossesQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, SFDisjointQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, SFEqualsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, SFIntersectsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, SFOverlapsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, SFTouchesQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, SFWithinQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, SFContainsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, SFCrossesQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, SFDisjointQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, SFEqualsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, SFIntersectsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, SFOverlapsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, SFTouchesQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, SFWithinQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, SFContainsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, SFCrossesQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, SFDisjointQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, SFEqualsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, SFIntersectsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, SFOverlapsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, SFTouchesQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, SFWithinQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropertyFunction(registry, General.SF_CONTAINS_NAME, SFContainsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_CROSSES_NAME, SFCrossesQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_DISJOINT_NAME, SFDisjointQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_EQUALS_NAME, SFEqualsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_INTERSECTS_NAME, SFIntersectsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_OVERLAPS_NAME, SFOverlapsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_TOUCHES_NAME, SFTouchesQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, General.SF_WITHIN_NAME, SFWithinQRGmlGeometryToGeometry.class);
    }

    /**
     * This method loads all the Simple Feature Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFilterFunction(registry, General.SF_CONTAINS_NAME, sfContainsFF.class);
        addFilterFunction(registry, General.SF_CROSSES_NAME, sfCrossesFF.class);
        addFilterFunction(registry, General.SF_DISJOINT_NAME, sfDisjointFF.class);
        addFilterFunction(registry, General.SF_EQUALS_NAME, sfEqualsFF.class);
        addFilterFunction(registry, General.SF_INTERSECTS_NAME, sfIntersectsFF.class);
        addFilterFunction(registry, General.SF_OVERLAPS_NAME, sfOverlapsFF.class);
        addFilterFunction(registry, General.SF_TOUCHES_NAME, sfTouchesFF.class);
        addFilterFunction(registry, General.SF_WITHIN_NAME, sfWithinFF.class);
    }

}
