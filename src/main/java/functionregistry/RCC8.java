/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionregistry;

import geof.topological.rcc8.filterfunction.*;
import static functionregistry.FunctionLoader.addFilterFunction;
import static functionregistry.FunctionLoader.addPropertyFunction;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class RCC8 {

    /**
     * This method loads all the RCC8 Topological Property Functions as well as
     * the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, geof.topopf.rcc8.RCC8DCPropertyFunc.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, geof.topopf.rcc8.RCC8EQPropertyFunc.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, geof.topopf.rcc8.RCC8ECPropertyFunc.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, geof.topopf.rcc8.RCC8NTPPIPropertyFunc.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, geof.topopf.rcc8.RCC8NTPPPropertyFunc.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, geof.topopf.rcc8.RCC8POPropertyFunc.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, geof.topopf.rcc8.RCC8TPPIPropertyFunc.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, geof.topopf.rcc8.RCC8TPPPropertyFunc.class);
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
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8DCQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8EQQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8ECQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8NTPPIQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8NTPPQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8POQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8TPPIQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, queryrewrite.pf.rcc8.wkt.featuretofeature.RCC8TPPQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8DCQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8EQQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8ECQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8NTPPIQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8NTPPQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8POQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8TPPIQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, queryrewrite.pf.rcc8.wkt.featuretogeometry.RCC8TPPQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8DCQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8EQQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8ECQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8NTPPIQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8NTPPQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8POQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8TPPIQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, queryrewrite.pf.rcc8.wkt.geometrytofeature.RCC8TPPQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8DCQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8EQQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8ECQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8NTPPIQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8NTPPQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8POQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8TPPIQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, queryrewrite.pf.rcc8.wkt.geometrytogeometry.RCC8TPPQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, queryrewrite.pf.rcc8.gml.featuretofeature.RCC8DCQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, queryrewrite.pf.rcc8.gml.featuretofeature.RCC8EQQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, queryrewrite.pf.rcc8.gml.featuretofeature.RCC8ECQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.gml.featuretofeature.RCC8NTPPIQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, queryrewrite.pf.rcc8.gml.featuretofeature.RCC8NTPPQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, queryrewrite.pf.rcc8.gml.featuretofeature.RCC8POQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.gml.featuretofeature.RCC8TPPIQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, queryrewrite.pf.rcc8.gml.featuretofeature.RCC8TPPQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8DCQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8EQQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8ECQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8NTPPIQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8NTPPQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8POQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8TPPIQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, queryrewrite.pf.rcc8.gml.featuretogeometry.RCC8TPPQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8DCQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8EQQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8ECQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8NTPPIQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8NTPPQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8POQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8TPPIQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, queryrewrite.pf.rcc8.gml.geometrytofeature.RCC8TPPQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8DCQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8EQQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8ECQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8NTPPIQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8NTPPQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8POQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8TPPIQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, queryrewrite.pf.rcc8.gml.geometrytogeometry.RCC8TPPQRGmlGeometryToGeometry.class);

    }

    /**
     * This method loads all the RCC8 Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFilterFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, Disconnected.class);
        addFilterFunction(registry, Vocabulary.RCC_EQUALS_NAME, Equals.class);
        addFilterFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, ExternallyConnected.class);
        addFilterFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, NonTangentalProperPartInverse.class);
        addFilterFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, NonTangentalProperPart.class);
        addFilterFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, PartiallyOverlapping.class);
        addFilterFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, TangentalProperPartInverse.class);
        addFilterFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, TangentalProperPart.class);
    }

}
