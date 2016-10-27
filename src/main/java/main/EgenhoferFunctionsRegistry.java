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
     * This method loads all the Egenhofer Topological Property Functions as
     * well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

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
     * This method loads all the Egenhofer Query Rewrite Property Functions
     * <br> Notice These functions must be used with the inference model
     *
     * @param registry - the Query Rewrite PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadQueryRewriteFunctions(PropertyFunctionRegistry registry) {

        // WKT feature to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.wkt.featuretofeature.EHContainsQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.wkt.featuretofeature.EHCoveredByQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.wkt.featuretofeature.EHCoversQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.wkt.featuretofeature.EHDisjointQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.wkt.featuretofeature.EHEqualQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.wkt.featuretofeature.EHInsideQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.wkt.featuretofeature.EHMeetQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.wkt.featuretofeature.EHOverlapQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.wkt.featuretogeometry.EHContainsQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.wkt.featuretogeometry.EHCoveredByQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.wkt.featuretogeometry.EHCoversQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.wkt.featuretogeometry.EHDisjointQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.wkt.featuretogeometry.EHEqualQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.wkt.featuretogeometry.EHInsideQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.wkt.featuretogeometry.EHMeetQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.wkt.featuretogeometry.EHOverlapQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.wkt.geometrytofeature.EHContainsQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.wkt.geometrytofeature.EHCoveredByQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.wkt.geometrytofeature.EHCoversQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.wkt.geometrytofeature.EHDisjointQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.wkt.geometrytofeature.EHEqualQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.wkt.geometrytofeature.EHInsideQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.wkt.geometrytofeature.EHMeetQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.wkt.geometrytofeature.EHOverlapQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.wkt.geometrytogeometry.EHContainsQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.wkt.geometrytogeometry.EHCoveredByQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.wkt.geometrytogeometry.EHCoversQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.wkt.geometrytogeometry.EHDisjointQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.wkt.geometrytogeometry.EHEqualQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.wkt.geometrytogeometry.EHInsideQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.wkt.geometrytogeometry.EHMeetQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.wkt.geometrytogeometry.EHOverlapQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.gml.featuretofeature.EHContainsQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.gml.featuretofeature.EHCoveredByQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.gml.featuretofeature.EHCoversQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.gml.featuretofeature.EHDisjointQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.gml.featuretofeature.EHEqualQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.gml.featuretofeature.EHInsideQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.gml.featuretofeature.EHMeetQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.gml.featuretofeature.EHOverlapQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.gml.featuretogeometry.EHContainsQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.gml.featuretogeometry.EHCoveredByQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.gml.featuretogeometry.EHCoversQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.gml.featuretogeometry.EHDisjointQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.gml.featuretogeometry.EHEqualQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.gml.featuretogeometry.EHInsideQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.gml.featuretogeometry.EHMeetQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.gml.featuretogeometry.EHOverlapQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.gml.geometrytofeature.EHContainsQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.gml.geometrytofeature.EHCoveredByQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.gml.geometrytofeature.EHCoversQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.gml.geometrytofeature.EHDisjointQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.gml.geometrytofeature.EHEqualQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.gml.geometrytofeature.EHInsideQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.gml.geometrytofeature.EHMeetQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.gml.geometrytofeature.EHOverlapQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.gml.geometrytogeometry.EHContainsQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.gml.geometrytogeometry.EHCoveredByQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.gml.geometrytogeometry.EHCoversQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.gml.geometrytogeometry.EHDisjointQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.gml.geometrytogeometry.EHEqualQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.gml.geometrytogeometry.EHInsideQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.gml.geometrytogeometry.EHMeetQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.gml.geometrytogeometry.EHOverlapQRGmlGeometryToGeometry.class);
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
