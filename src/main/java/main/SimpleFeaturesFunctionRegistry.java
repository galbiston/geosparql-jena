/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class SimpleFeaturesFunctionRegistry {

    /**
     * This method loads all the Simple Feature Topological Property Functions
     * without the query rewrite support
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadTopologyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CONTAINS_NAME), geof.topopf.sf.SFContainsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CROSSES_NAME), geof.topopf.sf.SFCrossesPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_DISJOINT_NAME), geof.topopf.sf.SFDisjointPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_EQUALS_NAME), geof.topopf.sf.SFEqualsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), geof.topopf.sf.SFIntersectsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), geof.topopf.sf.SFOverlapsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_TOUCHES_NAME), geof.topopf.sf.SFTouchesPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_WITHIN_NAME), geof.topopf.sf.SFWithinPropertyFunc.class);
    }

    /**
     * This method loads all the Simple Feature Query Rewrite Feature To Feature
     * Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFeatureToFeatureFuncs(PropertyFunctionRegistry registry) {
        // feature to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.featuretofeature.SFContainsQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.featuretofeature.SFCrossesQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.featuretofeature.SFDisjointQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.featuretofeature.SFEqualsQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.featuretofeature.SFIntersectsQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.featuretofeature.SFOverlapsQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.featuretofeature.SFTouchesQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.featuretofeature.SFWithinQueryRewriteFeatureToFeature.class);
    }

    /**
     * This method loads all the Simple Feature Query Rewrite Feature To
     * Geometry Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFeatureToGeometryFuncs(PropertyFunctionRegistry registry) {
        // feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.featuretogeometry.SFContainsQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.featuretogeometry.SFCrossesQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.featuretogeometry.SFDisjointQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.featuretogeometry.SFEqualsQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.featuretogeometry.SFIntersectsQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.featuretogeometry.SFOverlapsQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.featuretogeometry.SFTouchesQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.featuretogeometry.SFWithinQueryRewriteFeatureToGeometry.class);
    }

    /**
     * This method loads all the Simple Feature Query Rewrite Geometry To
     * Feature Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteGeometryToFeatureFuncs(PropertyFunctionRegistry registry) {
        // geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.geometrytofeature.SFContainsQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.geometrytofeature.SFCrossesQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.geometrytofeature.SFDisjointQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.geometrytofeature.SFEqualsQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.geometrytofeature.SFIntersectsQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.geometrytofeature.SFOverlapsQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.geometrytofeature.SFTouchesQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.geometrytofeature.SFWithinQueryRewriteGeometryToFeature.class);
    }

    /**
     * This method loads all the Simple Feature Query Rewrite Geometry To
     * Geometry Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteGeometryToGeometryFuncs(PropertyFunctionRegistry registry) {
        // geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.geometrytogeometry.SFContainsQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.geometrytogeometry.SFCrossesQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.geometrytogeometry.SFDisjointQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.geometrytogeometry.SFEqualsQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.geometrytogeometry.SFIntersectsQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.geometrytogeometry.SFOverlapsQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.geometrytogeometry.SFTouchesQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.geometrytogeometry.SFWithinQueryRewriteGeometryToGeometry.class);
    }

    /**
     * This method loads all the Simple Feature Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_CONTAINS_NAME), geof.topo.sf.SFContainsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_CROSSES_NAME), geof.topo.sf.SFCrossesFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_DISJOINT_NAME), geof.topo.sf.SFDisjointFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_EQUALS_NAME), geof.topo.sf.SFEqualsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_INTERSECTS_NAME), geof.topo.sf.SFIntersectsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_OVERLAPS_NAME), geof.topo.sf.SFOverlapsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_TOUCHES_NAME), geof.topo.sf.SFTouchesFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_WITHIN_NAME), geof.topo.sf.SFWithinFilterFunc.class);
    }

    private static void addPropFunc(PropertyFunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
