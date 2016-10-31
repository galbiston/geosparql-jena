/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geof.topological.simplefeatures.filterfunction.*;
import static implementation.functionregistry.FunctionLoader.addFilterFunction;
import static implementation.functionregistry.FunctionLoader.addPropertyFunction;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import implementation.support.Vocabulary;

/**
 *
 * @author haozhechen
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
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, geof.topological.simplefeatures.propertyfunction.Contains.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, geof.topological.simplefeatures.propertyfunction.Crosses.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, geof.topological.simplefeatures.propertyfunction.Disjoint.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, geof.topological.simplefeatures.propertyfunction.Equals.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, geof.topological.simplefeatures.propertyfunction.Intersects.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, geof.topological.simplefeatures.propertyfunction.Overlaps.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, geof.topological.simplefeatures.propertyfunction.Touches.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, geof.topological.simplefeatures.propertyfunction.Within.class);
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
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, queryrewrite.pf.sf.wkt.featuretofeature.SFContainsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, queryrewrite.pf.sf.wkt.featuretofeature.SFCrossesQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, queryrewrite.pf.sf.wkt.featuretofeature.SFDisjointQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, queryrewrite.pf.sf.wkt.featuretofeature.SFEqualsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, queryrewrite.pf.sf.wkt.featuretofeature.SFIntersectsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, queryrewrite.pf.sf.wkt.featuretofeature.SFOverlapsQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, queryrewrite.pf.sf.wkt.featuretofeature.SFTouchesQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, queryrewrite.pf.sf.wkt.featuretofeature.SFWithinQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, queryrewrite.pf.sf.wkt.featuretogeometry.SFContainsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, queryrewrite.pf.sf.wkt.featuretogeometry.SFCrossesQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, queryrewrite.pf.sf.wkt.featuretogeometry.SFDisjointQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, queryrewrite.pf.sf.wkt.featuretogeometry.SFEqualsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, queryrewrite.pf.sf.wkt.featuretogeometry.SFIntersectsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, queryrewrite.pf.sf.wkt.featuretogeometry.SFOverlapsQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, queryrewrite.pf.sf.wkt.featuretogeometry.SFTouchesQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, queryrewrite.pf.sf.wkt.featuretogeometry.SFWithinQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, queryrewrite.pf.sf.wkt.geometrytofeature.SFContainsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, queryrewrite.pf.sf.wkt.geometrytofeature.SFCrossesQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, queryrewrite.pf.sf.wkt.geometrytofeature.SFDisjointQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, queryrewrite.pf.sf.wkt.geometrytofeature.SFEqualsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, queryrewrite.pf.sf.wkt.geometrytofeature.SFIntersectsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, queryrewrite.pf.sf.wkt.geometrytofeature.SFOverlapsQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, queryrewrite.pf.sf.wkt.geometrytofeature.SFTouchesQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, queryrewrite.pf.sf.wkt.geometrytofeature.SFWithinQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, queryrewrite.pf.sf.wkt.geometrytogeometry.SFContainsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, queryrewrite.pf.sf.wkt.geometrytogeometry.SFCrossesQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, queryrewrite.pf.sf.wkt.geometrytogeometry.SFDisjointQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, queryrewrite.pf.sf.wkt.geometrytogeometry.SFEqualsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, queryrewrite.pf.sf.wkt.geometrytogeometry.SFIntersectsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, queryrewrite.pf.sf.wkt.geometrytogeometry.SFOverlapsQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, queryrewrite.pf.sf.wkt.geometrytogeometry.SFTouchesQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, queryrewrite.pf.sf.wkt.geometrytogeometry.SFWithinQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, queryrewrite.pf.sf.gml.featuretofeature.SFContainsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, queryrewrite.pf.sf.gml.featuretofeature.SFCrossesQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, queryrewrite.pf.sf.gml.featuretofeature.SFDisjointQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, queryrewrite.pf.sf.gml.featuretofeature.SFEqualsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, queryrewrite.pf.sf.gml.featuretofeature.SFIntersectsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, queryrewrite.pf.sf.gml.featuretofeature.SFOverlapsQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, queryrewrite.pf.sf.gml.featuretofeature.SFTouchesQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, queryrewrite.pf.sf.gml.featuretofeature.SFWithinQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, queryrewrite.pf.sf.gml.featuretogeometry.SFContainsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, queryrewrite.pf.sf.gml.featuretogeometry.SFCrossesQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, queryrewrite.pf.sf.gml.featuretogeometry.SFDisjointQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, queryrewrite.pf.sf.gml.featuretogeometry.SFEqualsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, queryrewrite.pf.sf.gml.featuretogeometry.SFIntersectsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, queryrewrite.pf.sf.gml.featuretogeometry.SFOverlapsQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, queryrewrite.pf.sf.gml.featuretogeometry.SFTouchesQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, queryrewrite.pf.sf.gml.featuretogeometry.SFWithinQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, queryrewrite.pf.sf.gml.geometrytofeature.SFContainsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, queryrewrite.pf.sf.gml.geometrytofeature.SFCrossesQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, queryrewrite.pf.sf.gml.geometrytofeature.SFDisjointQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, queryrewrite.pf.sf.gml.geometrytofeature.SFEqualsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, queryrewrite.pf.sf.gml.geometrytofeature.SFIntersectsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, queryrewrite.pf.sf.gml.geometrytofeature.SFOverlapsQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, queryrewrite.pf.sf.gml.geometrytofeature.SFTouchesQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, queryrewrite.pf.sf.gml.geometrytofeature.SFWithinQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.SF_CONTAINS_NAME, queryrewrite.pf.sf.gml.geometrytogeometry.SFContainsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_CROSSES_NAME, queryrewrite.pf.sf.gml.geometrytogeometry.SFCrossesQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_DISJOINT_NAME, queryrewrite.pf.sf.gml.geometrytogeometry.SFDisjointQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_EQUALS_NAME, queryrewrite.pf.sf.gml.geometrytogeometry.SFEqualsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_INTERSECTS_NAME, queryrewrite.pf.sf.gml.geometrytogeometry.SFIntersectsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_OVERLAPS_NAME, queryrewrite.pf.sf.gml.geometrytogeometry.SFOverlapsQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_TOUCHES_NAME, queryrewrite.pf.sf.gml.geometrytogeometry.SFTouchesQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.SF_WITHIN_NAME, queryrewrite.pf.sf.gml.geometrytogeometry.SFWithinQRGmlGeometryToGeometry.class);
    }

    /**
     * This method loads all the Simple Feature Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFilterFunction(registry, Vocabulary.SF_CONTAINS_NAME, Contains.class);
        addFilterFunction(registry, Vocabulary.SF_CROSSES_NAME, Crosses.class);
        addFilterFunction(registry, Vocabulary.SF_DISJOINT_NAME, Disjoint.class);
        addFilterFunction(registry, Vocabulary.SF_EQUALS_NAME, Equals.class);
        addFilterFunction(registry, Vocabulary.SF_INTERSECTS_NAME, Intersects.class);
        addFilterFunction(registry, Vocabulary.SF_OVERLAPS_NAME, Overlaps.class);
        addFilterFunction(registry, Vocabulary.SF_TOUCHES_NAME, Touches.class);
        addFilterFunction(registry, Vocabulary.SF_WITHIN_NAME, Within.class);
    }

}
