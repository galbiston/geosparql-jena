/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.simplefeatures.*;
import geof.topological.simplefeatures.*;
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
public class SimpleFeatures {

    /**
     * This method loads all the Simple Feature Topological Property Functions
     * as well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        registry.put(Geo.SF_CONTAINS_NAME, sfContainsPF.class);
        registry.put(Geo.SF_CROSSES_NAME, sfCrossesPF.class);
        registry.put(Geo.SF_DISJOINT_NAME, sfDisjointPF.class);
        registry.put(Geo.SF_EQUALS_NAME, sfEqualsPF.class);
        registry.put(Geo.SF_INTERSECTS_NAME, sfIntersectsPF.class);
        registry.put(Geo.SF_OVERLAPS_NAME, sfOverlapsPF.class);
        registry.put(Geo.SF_TOUCHES_NAME, sfTouchesPF.class);
        registry.put(Geo.SF_WITHIN_NAME, sfWithinPF.class);
    }

    /**
     * This method loads all the Simple Feature Topological Expression Functions
     *
     */
    @SuppressWarnings("deprecation")
    public static void loadExpressionFunctions() {

        // Manually create expression function variables
        List<Var> args = new ArrayList<>();
        args.add(Var.alloc("left"));
        args.add(Var.alloc("right"));

        UserDefinedFunctionFactory.getFactory().add(Geof.SF_CONTAINS, new sfContainsEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.SF_CROSSES, new sfCrossesEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.SF_DISJOINT, new sfDisjointEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.SF_EQUALS, new sfEqualsEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.SF_INTERSECTS, new sfIntersectsEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.SF_OVERLAPS, new sfOverlapsEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.SF_TOUCHES, new sfTouchesEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);
        UserDefinedFunctionFactory.getFactory().add(Geof.SF_WITHIN, new sfWithinEF(new ExprVar(args.get(0).getName()), new ExprVar(args.get(1).getName())), args);

    }
}
