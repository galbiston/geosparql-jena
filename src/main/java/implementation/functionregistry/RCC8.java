/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.rcc8.*;
import geof.topological.rcc8.expressionfunction.*;
import implementation.vocabulary.Geo;
import implementation.vocabulary.Geof;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.function.user.UserDefinedFunctionFactory;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

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
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        registry.put(Geo.RCC_DISCONNECTED_NAME, rccDisconnectedPF.class);
        registry.put(Geo.RCC_EQUALS_NAME, rccEqualsPF.class);
        registry.put(Geo.RCC_EXTERNALLY_CONNECTED_NAME, rccExternallyConnectedPF.class);
        registry.put(Geo.RCC_NTANPROPERPARTINVERSE_NAME, rccNonTangentalProperPartInversePF.class);
        registry.put(Geo.RCC_NTANPROPERPART_NAME, rccNonTangentalProperPartPF.class);
        registry.put(Geo.RCC_PARTIALLY_OVERLAPPING_NAME, rccPartiallyOverlappingPF.class);
        registry.put(Geo.RCC_TANPROPERPARTINVERSE_NAME, rccTangentalProperPartInversePF.class);
        registry.put(Geo.RCC_TANPROPERPART_NAME, rccTangentalProperPartPF.class);
    }

    /**
     * This method loads all the RCC8 Topological Expression Functions
     *
     */
    @SuppressWarnings("deprecation")
    public static void loadExpressionFunctions() {

        // Manually create expression function variables
        List<Var> args = new ArrayList<>();
        args.add(Var.alloc("left"));
        args.add(Var.alloc("right"));

        UserDefinedFunctionFactory.getFactory().add(Geof.RCC_DISCONNECTED, new rccDisconnectedEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.RCC_EQUALS, new rccEqualsEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.RCC_EXTERNALLY_CONNECTED, new rccExternallyConnectedEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.RCC_NON_TANGENTIAL_PROPER_PART_INVERSE, new rccNonTangentialProperPartInverseEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.RCC_NON_TANGENTIAL_PROPER_PART, new rccNonTangentialProperPartEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.RCC_PARTIALLY_OVERLAPPING, new rccPartiallyOverlappingEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.RCC_TANGENTIAL_PROPER_PART_INVERSE, new rccTangentialProperPartInverseEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.RCC_TANGENTIAL_PROPER_PART, new rccTangentialProperPartEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);

    }
}
