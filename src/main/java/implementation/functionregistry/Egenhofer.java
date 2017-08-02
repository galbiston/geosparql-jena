/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.egenhofer.*;
import geof.topological.egenhofer.*;
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
public class Egenhofer {

    /**
     * This method loads all the Egenhofer Topological Property Functions as
     * well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        registry.put(Geo.EH_CONTAINS_NAME, ehContainsPF.class);
        registry.put(Geo.EH_COVERED_BY_NAME, ehCoveredByPF.class);
        registry.put(Geo.EH_COVERS_NAME, ehCoversPF.class);
        registry.put(Geo.EH_DISJOINT_NAME, ehDisjointPF.class);
        registry.put(Geo.EH_EQUALS_NAME, ehEqualsPF.class);
        registry.put(Geo.EH_INSIDE_NAME, ehInsidePF.class);
        registry.put(Geo.EH_MEET_NAME, ehMeetPF.class);
        registry.put(Geo.EH_OVERLAP_NAME, ehOverlapPF.class);
    }

    /**
     * This method loads all the Egenhofer Topological Expression Functions
     *
     */
    public static void loadExpressionFunctions() {

        // Manually create expression function variables
        List<Var> args = new ArrayList<>();
        args.add(Var.alloc("left"));
        args.add(Var.alloc("right"));

        UserDefinedFunctionFactory.getFactory().add(Geof.EH_CONTAINS, new ehContainsEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.EH_COVERED_BY, new ehCoveredByEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.EH_COVERS, new ehDisjointEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.EH_DISJOINT, new ehDisjointEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.EH_EQUALS, new ehEqualsEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.EH_INSIDE, new ehInsideEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.EH_MEET, new ehMeetEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.EH_OVERLAP, new ehOverlapEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);

    }
}
