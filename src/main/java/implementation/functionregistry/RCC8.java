/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geof.topological.rcc8.filterfunction.*;
import geof.topological.rcc8.propertyfunction.*;
import static implementation.functionregistry.FunctionLoader.addFilterFunction;
import static implementation.functionregistry.FunctionLoader.addPropertyFunction;
import implementation.support.Vocabulary;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import queryrewrite.pf.rcc8.gml.featuretofeature.*;
import queryrewrite.pf.rcc8.gml.featuretogeometry.*;
import queryrewrite.pf.rcc8.gml.geometrytofeature.*;
import queryrewrite.pf.rcc8.gml.geometrytogeometry.*;
import queryrewrite.pf.rcc8.wkt.featuretofeature.*;
import queryrewrite.pf.rcc8.wkt.featuretogeometry.*;
import queryrewrite.pf.rcc8.wkt.geometrytofeature.*;
import queryrewrite.pf.rcc8.wkt.geometrytogeometry.*;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
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
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, rccDisconnectedPF.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, rccEqualsPF.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, rccExternallyConnectedPF.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, rccNonTangentalProperPartInversePF.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, rccNonTangentalProperPartPF.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, rccPartiallyOverlappingPF.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, rccTangentalProperPartInversePF.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, rccTangentalProperPartPF.class);
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
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, RCC8DCQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, RCC8EQQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, RCC8ECQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, RCC8NTPPIQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, RCC8NTPPQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, RCC8POQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, RCC8TPPIQRWktFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, RCC8TPPQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, RCC8DCQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, RCC8EQQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, RCC8ECQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, RCC8NTPPIQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, RCC8NTPPQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, RCC8POQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, RCC8TPPIQRWktFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, RCC8TPPQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, RCC8DCQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, RCC8EQQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, RCC8ECQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, RCC8NTPPIQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, RCC8NTPPQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, RCC8POQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, RCC8TPPIQRWktGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, RCC8TPPQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, RCC8DCQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, RCC8EQQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, RCC8ECQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, RCC8NTPPIQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, RCC8NTPPQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, RCC8POQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, RCC8TPPIQRWktGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, RCC8TPPQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, RCC8DCQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, RCC8EQQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, RCC8ECQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, RCC8NTPPIQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, RCC8NTPPQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, RCC8POQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, RCC8TPPIQRGmlFeatureToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, RCC8TPPQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, RCC8DCQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, RCC8EQQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, RCC8ECQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, RCC8NTPPIQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, RCC8NTPPQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, RCC8POQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, RCC8TPPIQRGmlFeatureToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, RCC8TPPQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, RCC8DCQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, RCC8EQQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, RCC8ECQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, RCC8NTPPIQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, RCC8NTPPQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, RCC8POQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, RCC8TPPIQRGmlGeometryToFeature.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, RCC8TPPQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropertyFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, RCC8DCQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EQUALS_NAME, RCC8EQQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, RCC8ECQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, RCC8NTPPIQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, RCC8NTPPQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, RCC8POQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, RCC8TPPIQRGmlGeometryToGeometry.class);
        addPropertyFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, RCC8TPPQRGmlGeometryToGeometry.class);

    }

    /**
     * This method loads all the RCC8 Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFilterFunction(registry, Vocabulary.RCC_DISCONNECTED_NAME, rccDisconnectedFF.class);
        addFilterFunction(registry, Vocabulary.RCC_EQUALS_NAME, rccEqualsFF.class);
        addFilterFunction(registry, Vocabulary.RCC_EXTERNALLY_CONNECTED_NAME, rccExternallyConnectedFF.class);
        addFilterFunction(registry, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME, rccNonTangentialProperPartInverseFF.class);
        addFilterFunction(registry, Vocabulary.RCC_NTANPROPERPART_NAME, rccNonTangentialProperPartFF.class);
        addFilterFunction(registry, Vocabulary.RCC_PARTIALLY_OVERLAPPING_NAME, rccPartiallyOverlappingFF.class);
        addFilterFunction(registry, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME, rccTangentialProperPartInverseFF.class);
        addFilterFunction(registry, Vocabulary.RCC_TANPROPERPART_NAME, rccTangentialProperPartFF.class);
    }

}
