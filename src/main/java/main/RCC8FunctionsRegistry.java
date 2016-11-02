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
public class RCC8FunctionsRegistry {

    /**
     * This method loads all the RCC8 Topological Property Functions without the
     * query rewrite support
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadTopologyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), geof.topopf.rcc8.RCC8DCPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EQUALS_NAME), geof.topopf.rcc8.RCC8EQPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), geof.topopf.rcc8.RCC8ECPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), geof.topopf.rcc8.RCC8NTPPIPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), geof.topopf.rcc8.RCC8NTPPPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), geof.topopf.rcc8.RCC8POPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), geof.topopf.rcc8.RCC8TPPIPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), geof.topopf.rcc8.RCC8TPPPropertyFunc.class);
    }

    /**
     * This method loads all the RCC8 Query Rewrite Feature To Feature Property
     * Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFeatureToFeatureFuncs(PropertyFunctionRegistry registry) {
        // feature to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.featuretofeature.RCC8DCQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.featuretofeature.RCC8EQQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.featuretofeature.RCC8ECQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.featuretofeature.RCC8NTPPIQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.featuretofeature.RCC8NTPPQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.featuretofeature.RCC8POQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.featuretofeature.RCC8TPPIQueryRewriteFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.featuretofeature.RCC8TPPQueryRewriteFeatureToFeature.class);
    }

    /**
     * This method loads all the RCC8 Query Rewrite Feature To Geometry Property
     * Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFeatureToGeometryFuncs(PropertyFunctionRegistry registry) {
        // feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.featuretogeometry.RCC8DCQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.featuretogeometry.RCC8EQQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.featuretogeometry.RCC8ECQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.featuretogeometry.RCC8NTPPIQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.featuretogeometry.RCC8NTPPQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.featuretogeometry.RCC8POQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.featuretogeometry.RCC8TPPIQueryRewriteFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.featuretogeometry.RCC8TPPQueryRewriteFeatureToGeometry.class);
    }

    /**
     * This method loads all the RCC8 Query Rewrite Geometry To Feature Property
     * Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteGeometryToFeatureFuncs(PropertyFunctionRegistry registry) {
        // geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.geometrytofeature.RCC8DCQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.geometrytofeature.RCC8EQQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.geometrytofeature.RCC8ECQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.geometrytofeature.RCC8NTPPIQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.geometrytofeature.RCC8NTPPQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.geometrytofeature.RCC8POQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.geometrytofeature.RCC8TPPIQueryRewriteGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.geometrytofeature.RCC8TPPQueryRewriteGeometryToFeature.class);
    }

    /**
     * This method loads all the RCC8 Query Rewrite Geometry To Geometry
     * Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteGeometryToGeometryFuncs(PropertyFunctionRegistry registry) {
        // geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.geometrytogeometry.RCC8DCQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.geometrytogeometry.RCC8EQQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.geometrytogeometry.RCC8ECQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.geometrytogeometry.RCC8NTPPIQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.geometrytogeometry.RCC8NTPPQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.geometrytogeometry.RCC8POQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.geometrytogeometry.RCC8TPPIQueryRewriteGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.geometrytogeometry.RCC8TPPQueryRewriteGeometryToGeometry.class);
    }

    /**
     * This method loads all the RCC8 Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), geof.topo.rcc8.RCC8DCFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EQUALS_NAME), geof.topo.rcc8.RCC8EQFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), geof.topo.rcc8.RCC8ECFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), geof.topo.rcc8.RCC8NTPPIFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), geof.topo.rcc8.RCC8NTPPFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), geof.topo.rcc8.RCC8POFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), geof.topo.rcc8.RCC8TPPIFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), geof.topo.rcc8.RCC8TPPFilterFunc.class);
    }

    private static void addPropFunc(PropertyFunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

}
