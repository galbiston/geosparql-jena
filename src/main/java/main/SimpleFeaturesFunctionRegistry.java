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
public class SimpleFeaturesFunctionRegistry {

    /**
     * This method loads all the Simple Feature Topological Property Functions
     * as well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), geof.topopf.sf.SFContainsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), geof.topopf.sf.SFCrossesPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), geof.topopf.sf.SFDisjointPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), geof.topopf.sf.SFEqualsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), geof.topopf.sf.SFIntersectsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), geof.topopf.sf.SFOverlapsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), geof.topopf.sf.SFTouchesPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), geof.topopf.sf.SFWithinPropertyFunc.class);
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
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.wkt.featuretofeature.SFContainsQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.wkt.featuretofeature.SFCrossesQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.wkt.featuretofeature.SFDisjointQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.wkt.featuretofeature.SFEqualsQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.wkt.featuretofeature.SFIntersectsQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.wkt.featuretofeature.SFOverlapsQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.wkt.featuretofeature.SFTouchesQRWktFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.wkt.featuretofeature.SFWithinQRWktFeatureToFeature.class);

        // WKT feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.wkt.featuretogeometry.SFContainsQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.wkt.featuretogeometry.SFCrossesQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.wkt.featuretogeometry.SFDisjointQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.wkt.featuretogeometry.SFEqualsQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.wkt.featuretogeometry.SFIntersectsQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.wkt.featuretogeometry.SFOverlapsQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.wkt.featuretogeometry.SFTouchesQRWktFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.wkt.featuretogeometry.SFWithinQRWktFeatureToGeometry.class);

        // WKT geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.wkt.geometrytofeature.SFContainsQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.wkt.geometrytofeature.SFCrossesQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.wkt.geometrytofeature.SFDisjointQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.wkt.geometrytofeature.SFEqualsQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.wkt.geometrytofeature.SFIntersectsQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.wkt.geometrytofeature.SFOverlapsQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.wkt.geometrytofeature.SFTouchesQRWktGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.wkt.geometrytofeature.SFWithinQRWktGeometryToFeature.class);

        // WKT geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.wkt.geometrytogeometry.SFContainsQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.wkt.geometrytogeometry.SFCrossesQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.wkt.geometrytogeometry.SFDisjointQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.wkt.geometrytogeometry.SFEqualsQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.wkt.geometrytogeometry.SFIntersectsQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.wkt.geometrytogeometry.SFOverlapsQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.wkt.geometrytogeometry.SFTouchesQRWktGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.wkt.geometrytogeometry.SFWithinQRWktGeometryToGeometry.class);

        // GML feature to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.gml.featuretofeature.SFContainsQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.gml.featuretofeature.SFCrossesQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.gml.featuretofeature.SFDisjointQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.gml.featuretofeature.SFEqualsQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.gml.featuretofeature.SFIntersectsQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.gml.featuretofeature.SFOverlapsQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.gml.featuretofeature.SFTouchesQRGmlFeatureToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.gml.featuretofeature.SFWithinQRGmlFeatureToFeature.class);

        // GML feature to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.gml.featuretogeometry.SFContainsQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.gml.featuretogeometry.SFCrossesQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.gml.featuretogeometry.SFDisjointQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.gml.featuretogeometry.SFEqualsQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.gml.featuretogeometry.SFIntersectsQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.gml.featuretogeometry.SFOverlapsQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.gml.featuretogeometry.SFTouchesQRGmlFeatureToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.gml.featuretogeometry.SFWithinQRGmlFeatureToGeometry.class);

        // GML geometry to feature query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.gml.geometrytofeature.SFContainsQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.gml.geometrytofeature.SFCrossesQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.gml.geometrytofeature.SFDisjointQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.gml.geometrytofeature.SFEqualsQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.gml.geometrytofeature.SFIntersectsQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.gml.geometrytofeature.SFOverlapsQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.gml.geometrytofeature.SFTouchesQRGmlGeometryToFeature.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.gml.geometrytofeature.SFWithinQRGmlGeometryToFeature.class);

        // GML geometry to geometry query rewrite functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.gml.geometrytogeometry.SFContainsQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.gml.geometrytogeometry.SFCrossesQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.gml.geometrytogeometry.SFDisjointQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.gml.geometrytogeometry.SFEqualsQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.gml.geometrytogeometry.SFIntersectsQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.gml.geometrytogeometry.SFOverlapsQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.gml.geometrytogeometry.SFTouchesQRGmlGeometryToGeometry.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.gml.geometrytogeometry.SFWithinQRGmlGeometryToGeometry.class);
    }

    /**
     * This method loads all the Simple Feature Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SF_CONTAINS_NAME), geof.topo.sf.SFContainsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SF_CROSSES_NAME), geof.topo.sf.SFCrossesFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SF_DISJOINT_NAME), geof.topo.sf.SFDisjointFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SF_EQUALS_NAME), geof.topo.sf.SFEqualsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SF_INTERSECTS_NAME), geof.topo.sf.SFIntersectsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SF_OVERLAPS_NAME), geof.topo.sf.SFOverlapsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SF_TOUCHES_NAME), geof.topo.sf.SFTouchesFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SF_WITHIN_NAME), geof.topo.sf.SFWithinFilterFunc.class);
    }

    private static void addPropFunc(PropertyFunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
