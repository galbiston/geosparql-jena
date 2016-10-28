/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import vocabulary.Prefixes;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class RCC8FunctionsRegistry {

    /**
     * This method loads all the RCC8 Topological Property Functions as well as
     * the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), geof.topopf.rcc8.RCC8DCPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), geof.topopf.rcc8.RCC8EQPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), geof.topopf.rcc8.RCC8ECPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), geof.topopf.rcc8.RCC8NTPPIPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), geof.topopf.rcc8.RCC8NTPPPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), geof.topopf.rcc8.RCC8POPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), geof.topopf.rcc8.RCC8TPPIPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), geof.topopf.rcc8.RCC8TPPPropertyFunc.class);
    }

    /**
     * This method loads all the RCC8 Query Rewrite Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFunctions(PropertyFunctionRegistry registry) {

        // WKT feature to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8DCQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8EQQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8ECQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8NTPPIQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8NTPPQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8POQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8TPPIQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8TPPQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8DCQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8EQQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8ECQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8NTPPIQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8NTPPQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8POQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8TPPIQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8TPPQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8DCQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8EQQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8ECQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8NTPPIQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8NTPPQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8POQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8TPPIQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8TPPQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8DCQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8EQQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8ECQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8NTPPIQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8NTPPQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8POQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8TPPIQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8TPPQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.gml.featuretofeature.RCC8DCQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.gml.featuretofeature.RCC8EQQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.gml.featuretofeature.RCC8ECQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.gml.featuretofeature.RCC8NTPPIQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.gml.featuretofeature.RCC8NTPPQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.gml.featuretofeature.RCC8POQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.gml.featuretofeature.RCC8TPPIQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.gml.featuretofeature.RCC8TPPQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8DCQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8EQQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8ECQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8NTPPIQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8NTPPQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8POQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8TPPIQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8TPPQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8DCQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8EQQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8ECQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8NTPPIQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8NTPPQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8POQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8TPPIQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8TPPQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8DCQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8EQQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8ECQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8NTPPIQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8NTPPQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8POQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8TPPIQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8TPPQRGmlGeometryToGeometry.class);

    }

    /**
     * This method loads all the RCC8 Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), geof.topo.rcc8.RCC8DCFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EQUALS_NAME), geof.topo.rcc8.RCC8EQFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), geof.topo.rcc8.RCC8ECFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), geof.topo.rcc8.RCC8NTPPIFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), geof.topo.rcc8.RCC8NTPPFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), geof.topo.rcc8.RCC8POFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), geof.topo.rcc8.RCC8TPPIFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), geof.topo.rcc8.RCC8TPPFilterFunc.class);
    }

    private static void addPropFunc(PropertyFunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

}
