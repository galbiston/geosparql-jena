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
public class EgenhoferFunctionsRegistry {

    /**
     * This method loads all the Egenhofer Topological Property Functions
     * without the query rewrite support
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadTopologyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), geof.topopf.eh.EHContainsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), geof.topopf.eh.EHCoveredByPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), geof.topopf.eh.EHCoversPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), geof.topopf.eh.EHDisjointPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), geof.topopf.eh.EHEqualPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), geof.topopf.eh.EHInsidePropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), geof.topopf.eh.EHMeetPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), geof.topopf.eh.EHOverlapPropertyFunc.class);
    }

    /**
     * This method loads all the Egenhofer Query Rewrite Feature to Feature
     * Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFeatureToFeatureFuncs(PropertyFunctionRegistry registry) {

        // feature to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.featuretofeature.EHContainsQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.featuretofeature.EHCoveredByQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.featuretofeature.EHCoversQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.featuretofeature.EHDisjointQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.featuretofeature.EHEqualQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.featuretofeature.EHInsideQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.featuretofeature.EHMeetQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.featuretofeature.EHOverlapQueryRewriteFeatureToFeature.class);
    }

    /**
     * This method loads all the Egenhofer Query Rewrite Feature to Geometry
     * Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFeatureToGeometryFuncs(PropertyFunctionRegistry registry) {

        // feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.featuretogeometry.EHContainsQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.featuretogeometry.EHCoveredByQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.featuretogeometry.EHCoversQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.featuretogeometry.EHDisjointQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.featuretogeometry.EHEqualQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.featuretogeometry.EHInsideQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.featuretogeometry.EHMeetQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.featuretogeometry.EHOverlapQueryRewriteFeatureToGeometry.class);
    }

    /**
     * This method loads all the Egenhofer Query Rewrite Geometry to Feature
     * Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteGeometryToFeatureFuncs(PropertyFunctionRegistry registry) {

        // geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.geometrytofeature.EHContainsQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.geometrytofeature.EHCoveredByQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.geometrytofeature.EHCoversQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.geometrytofeature.EHDisjointQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.geometrytofeature.EHEqualQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.geometrytofeature.EHInsideQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.geometrytofeature.EHMeetQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.geometrytofeature.EHOverlapQueryRewriteGeometryToFeature.class);
    }

    /**
     * This method loads all the Egenhofer Query Rewrite Geometry to Geometry
     * Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteGeometryToGeometryFuncs(PropertyFunctionRegistry registry) {
        // geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.geometrytogeometry.EHContainsQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.geometrytogeometry.EHCoveredByQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.geometrytogeometry.EHCoversQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.geometrytogeometry.EHDisjointQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.geometrytogeometry.EHEqualQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.geometrytogeometry.EHInsideQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.geometrytogeometry.EHMeetQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.geometrytogeometry.EHOverlapQueryRewriteGeometryToGeometry.class);
    }

    /**
     * This method loads all the Egenhofer Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_CONTAINS_NAME), geof.topo.eh.EHContainsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_COVEREDBY_NAME), geof.topo.eh.EHCoveredByFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_COVERS_NAME), geof.topo.eh.EHCoversFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_DISJOINT_NAME), geof.topo.eh.EHDisjointFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_EQUALS_NAME), geof.topo.eh.EHEqualFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_INSIDE_NAME), geof.topo.eh.EHInsideFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_MEET_NAME), geof.topo.eh.EHMeetFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_OVERLAP_NAME), geof.topo.eh.EHOverlapFilterFunc.class);
    }

    private static void addPropFunc(PropertyFunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
