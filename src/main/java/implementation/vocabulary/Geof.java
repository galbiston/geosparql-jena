/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.vocabulary;

import static implementation.support.Prefixes.GEOF_URI;

/**
 *
 * @author Greg
 * @author haozhechen
 */
public class Geof {

    //Relate function symbol:
    public static final String RELATE = GEOF_URI + "relate";

    //Simple Feature Topological function symbols:
    public static final String SF_CONTAINS = GEOF_URI + "sfContains";
    public static final String SF_CROSSES = GEOF_URI + "sfCrosses";
    public static final String SF_DISJOINT = GEOF_URI + "sfDisjoint";
    public static final String SF_EQUALS = GEOF_URI + "sfEquals";
    public static final String SF_INTERSECTS = GEOF_URI + "sfIntersects";
    public static final String SF_OVERLAPS = GEOF_URI + "sfOverlaps";
    public static final String SF_TOUCHES = GEOF_URI + "sfTouches";
    public static final String SF_WITHIN = GEOF_URI + "sfWithin";

    //Egenhofer Topological function symbols:
    public static final String EH_CONTAINS = GEOF_URI + "ehContains";
    public static final String EH_COVERS = GEOF_URI + "ehCovers";
    public static final String EH_DISJOINT = GEOF_URI + "ehDisjoint";
    public static final String EH_EQUALS = GEOF_URI + "ehEquals";
    public static final String EH_INSIDE = GEOF_URI + "ehInside";
    public static final String EH_OVERLAP = GEOF_URI + "ehOverlap";
    public static final String EH_MEET = GEOF_URI + "ehMeet";
    public static final String EH_COVERED_BY = GEOF_URI + "ehCoveredBy";

    //RCC8 Topological function symbols:
    public static final String RCC_EQUALS = GEOF_URI + "rcc8eq";
    public static final String RCC_DISCONNECTED = GEOF_URI + "rcc8dc";
    public static final String RCC_EXTERNALLY_CONNECTED = GEOF_URI + "rcc8ec";
    public static final String RCC_PARTIALLY_OVERLAPPING = GEOF_URI + "rcc8po";
    public static final String RCC_TANGENTIAL_PROPER_PART_INVERSE = GEOF_URI + "rcc8tppi";
    public static final String RCC_TANGENTIAL_PROPER_PART = GEOF_URI + "rcc8tpp";
    public static final String RCC_NON_TANGENTIAL_PROPER_PART = GEOF_URI + "rcc8ntpp";
    public static final String RCC_NON_TANGENTIAL_PROPER_PART_INVERSE = GEOF_URI + "rcc8ntppi";

    //Non-Topological function names:
    public static final String DISTANCE_NAME = GEOF_URI + "distance";
    public static final String SYMDIFFERENCE_NAME = GEOF_URI + "symDifference";
    public static final String UNION_NAME = GEOF_URI + "union";
    public static final String INTERSECTION_NAME = GEOF_URI + "intersection";
    public static final String ENVELOPE_NAME = GEOF_URI + "envelope";
    public static final String BUFFER_NAME = GEOF_URI + "buffer";
    public static final String DIFFERENCE_NAME = GEOF_URI + "difference";
    public static final String BOUNDARY_NAME = GEOF_URI + "boundary";
    public static final String CONVEXHULL_NAME = GEOF_URI + "convexHull";
    public static final String GETSRID_NAME = GEOF_URI + "getSRID";

}
