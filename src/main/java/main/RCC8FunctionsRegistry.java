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
     * This method loads all the RCC8 Topological Property Functions as well as
     * the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_DISCONNECTED_NAME), geof.topopf.rcc8.RCC8DCPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EQUALS_NAME), geof.topopf.rcc8.RCC8EQPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_EXTCONNECTED_NAME), geof.topopf.rcc8.RCC8ECPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), geof.topopf.rcc8.RCC8NTPPIPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_NTANPROPERPART_NAME), geof.topopf.rcc8.RCC8NTPPPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_PARTOVERLAP_NAME), geof.topopf.rcc8.RCC8POPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), geof.topopf.rcc8.RCC8TPPIPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.RCC_TANPROPERPART_NAME), geof.topopf.rcc8.RCC8TPPPropertyFunc.class);

        // Simple Feature Query Rewrite Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.RCC_DISCONNECTED_NAME), queryrewrite.pf.rcc8.RCC8DCQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.RCC_EQUALS_NAME), queryrewrite.pf.rcc8.RCC8EQQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.RCC_EXTCONNECTED_NAME), queryrewrite.pf.rcc8.RCC8ECQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.RCC_NTANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.RCC8NTPPIQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.RCC_NTANPROPERPART_NAME), queryrewrite.pf.rcc8.RCC8NTPPQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.RCC_PARTOVERLAP_NAME), queryrewrite.pf.rcc8.RCC8POQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.RCC_TANPROPERPARTINVERSE_NAME), queryrewrite.pf.rcc8.RCC8TPPIQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.RCC_TANPROPERPART_NAME), queryrewrite.pf.rcc8.RCC8TPPQRPropertyFunc.class);
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
