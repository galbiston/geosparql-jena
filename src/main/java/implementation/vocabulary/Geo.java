/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.vocabulary;

import implementation.support.Prefixes;
import static implementation.support.Prefixes.GEO_URI;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 * @author Greg
 */
public class Geo {

    public static final Property HAS_SERIALISATION_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "hasSerialization");
    public static final Node HAS_SERIALISATION_NODE = HAS_SERIALISATION_PRO.asNode();

    public static final Property AS_WKT_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "asWKT");
    public static final Property AS_GML_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "asGML");

    public static final Property HAS_DEFAULT_GEOMETRY_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "hasDefaultGeometry");
    public static final Node HAS_DEFAULT_GEOMETRY_NODE = HAS_DEFAULT_GEOMETRY_PRO.asNode();

    public static final Property HAS_GEOMETRY_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "hasGeometry");
    public static final Node HAS_GEOMETRY_NODE = HAS_GEOMETRY_PRO.asNode();

    public static final Resource GEOMETRY_RES = ResourceFactory.createResource(Prefixes.GEO_URI + "Geometry");
    public static final Node GEOMETRY_NODE = GEOMETRY_RES.asNode();

    public static final Resource FEATURE_RES = ResourceFactory.createResource(Prefixes.GEO_URI + "Feature");
    public static final Node FEATURE_NODE = FEATURE_RES.asNode();

    //Simple Feature Topological function names:
    public static final String SF_CONTAINS_NAME = GEO_URI + "sfContains";
    public static final String SF_INTERSECTS_NAME = GEO_URI + "sfIntersects";
    public static final String SF_EQUALS_NAME = GEO_URI + "sfEquals";

    public static final String SF_TOUCHES_NAME = GEO_URI + "sfTouches";
    public static final String SF_DISJOINT_NAME = GEO_URI + "sfDisjoint";
    public static final String SF_OVERLAPS_NAME = GEO_URI + "sfOverlaps";
    public static final String SF_CROSSES_NAME = GEO_URI + "sfCrosses";
    public static final String SF_WITHIN_NAME = GEO_URI + "sfWithin";

    //Egenhofer Topological function names:
    public static final String EH_DISJOINT_NAME = GEO_URI + "ehDisjoint";
    public static final String EH_CONTAINS_NAME = GEO_URI + "ehContains";
    public static final String EH_COVERED_BY_NAME = GEO_URI + "ehCoveredBy";
    public static final String EH_EQUALS_NAME = GEO_URI + "ehEquals";
    public static final String EH_OVERLAP_NAME = GEO_URI + "ehOverlap";
    public static final String EH_COVERS_NAME = GEO_URI + "ehCovers";
    public static final String EH_INSIDE_NAME = GEO_URI + "ehInside";
    public static final String EH_MEET_NAME = GEO_URI + "ehMeet";

    //RCC8 Topological function names:
    public static final String RCC_DISCONNECTED_NAME = GEO_URI + "rcc8dc";
    public static final String RCC_NTANPROPERPART_NAME = GEO_URI + "rcc8ntpp";
    public static final String RCC_NTANPROPERPARTINVERSE_NAME = GEO_URI + "rcc8ntppi";
    public static final String RCC_TANPROPERPART_NAME = GEO_URI + "rcc8pp";
    public static final String RCC_TANPROPERPARTINVERSE_NAME = GEO_URI + "rcc8ppi";
    public static final String RCC_EQUALS_NAME = GEO_URI + "rcc8eq";
    public static final String RCC_PARTIALLY_OVERLAPPING_NAME = GEO_URI + "rcc8po";
    public static final String RCC_EXTERNALLY_CONNECTED_NAME = GEO_URI + "rcc8ec";

    //Topological DE-9IM function: relate
    public static final String RELATE_NAME = GEO_URI + "relate";
}
