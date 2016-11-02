/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 * @author haozhechen
 */
public class Vocabulary {

    //URI
    public static final String GML_URI = "http://www.opengis.net/ont/gml#";
    public static final String GEOF_URI = "http://www.opengis.net/def/function/geosparql/";
    public static final String GEOR_URI = "http://www.opengis.net/def/rule/geosparql/";
    public static final String GEO_URI = "http://www.opengis.net/ont/geosparql#";
    public static final String NTU_URI = "http://ntu.ac.uk/ont/geo#";

    //Property
    public static final Property GML_PRO = ResourceFactory.createProperty(GML_URI + "asGML");
    public static final Property WKT_PRO = ResourceFactory.createProperty(GEO_URI + "asWKT");
    public static final Property SERIALIZATION_PRO = ResourceFactory.createProperty(GEO_URI + "hasSerialization");
    public static final Property GEOM_PRO = ResourceFactory.createProperty(GEO_URI + "hasGeometry");
    public static final Property GEOMEXACT_PRO = ResourceFactory.createProperty(NTU_URI + "hasExactGeometry");
    public static final Property GEOMPOINT_PRO = ResourceFactory.createProperty(NTU_URI + "hasPointGeometry");

    //Units Of Measurement URI
    public static final String METRE_URI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
    public static final String DEGREE_URI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
    public static final String GRIDSPACING_URI = "http://www.opengis.net/def/uom/OGC/1.0/GridSpacing";
    public static final String RADIAN_URI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
    public static final String UNITY_URI = "http://www.opengis.net/def/uom/OGC/1.0/unity";

    //Simple Feature Topological function symbols:
    public static final String SF_CONTAINS_SYMBOL = "geof:sfContains";
    public static final String SF_CROSSES_SYMBOL = "geof:sfCrosses";
    public static final String SF_DISJOINT_SYMBOL = "geof:sfDisjoint";
    public static final String SF_EQUALS_SYMBOL = "geof:sfEquals";
    public static final String SF_INTERSECTS_SYMBOL = "geof:sfIntersects";
    public static final String SF_OVERLAPS_SYMBOL = "geof:sfOverlaps";
    public static final String SF_TOUCHES_SYMBOL = "geof:sfTouches";
    public static final String SF_WITHIN_SYMBOL = "geof:sfWithin";

    //Egenhofer Topological function symbols:
    public static final String EH_CONTAINS_SYMBOL = "geof:ehContains";
    public static final String EH_COVERS_SYMBOL = "geof:ehCovers";
    public static final String EH_DISJOINT_SYMBOL = "geof:ehDisjoint";
    public static final String EH_EQUALS_SYMBOL = "geof:ehEquals";
    public static final String EH_INSIDE_SYMBOL = "geof:ehInside";
    public static final String EH_OVERLAP_SYMBOL = "geof:ehOverlap";
    public static final String EH_MEET_SYMBOL = "geof:ehMeet";
    public static final String EH_COVEREDBY_SYMBOL = "geof:ehCoveredBy";

    //RCC8 Topological function symbols:
    public static final String RCC_EQUALS_SYMBOL = "geof:rcc8eq";
    public static final String RCC_DISCONNECTED_SYMBOL = "geof:rcc8dc";
    public static final String RCC_EXTCONNECTED_SYMBOL = "geof:rcc8ec";
    public static final String RCC_PARTOVERLAP_SYMBOL = "geof:rcc8po";
    public static final String RCC_TANPROPERPARTINVERSE_SYMBOL = "geof:rcc8tppi";
    public static final String RCC_TANPROPERPART_SYMBOL = "geof:rcc8tpp";
    public static final String RCC_NTANPROPERPART_SYMBOL = "geof:rcc8ntpp";
    public static final String RCC_NTANPROPERPARTINVERSE_SYMBOL = "geof:rcc8ntppi";

    //Simple Feature Topological function names:
    public static final String SF_CONTAINS_NAME = "sfContains";
    public static final String SF_CROSSES_NAME = "sfCrosses";
    public static final String SF_DISJOINT_NAME = "sfDisjoint";
    public static final String SF_EQUALS_NAME = "sfEquals";
    public static final String SF_INTERSECTS_NAME = "sfIntersects";
    public static final String SF_OVERLAPS_NAME = "sfOverlaps";
    public static final String SF_TOUCHES_NAME = "sfTouches";
    public static final String SF_WITHIN_NAME = "sfWithin";

    //Egenhofer Topological function names:
    public static final String EH_CONTAINS_NAME = "ehContains";
    public static final String EH_COVERS_NAME = "ehCovers";
    public static final String EH_DISJOINT_NAME = "ehDisjoint";
    public static final String EH_EQUALS_NAME = "ehEquals";
    public static final String EH_INSIDE_NAME = "ehInside";
    public static final String EH_OVERLAP_NAME = "ehOverlap";
    public static final String EH_MEET_NAME = "ehMeet";
    public static final String EH_COVEREDBY_NAME = "ehCoveredBy";

    //RCC8 Topological function names:
    public static final String RCC_EQUALS_NAME = "rcc8eq";
    public static final String RCC_DISCONNECTED_NAME = "rcc8dc";
    public static final String RCC_EXTCONNECTED_NAME = "rcc8ec";
    public static final String RCC_PARTOVERLAP_NAME = "rcc8po";
    public static final String RCC_TANPROPERPARTINVERSE_NAME = "rcc8ppi";
    public static final String RCC_TANPROPERPART_NAME = "rcc8pp";
    public static final String RCC_NTANPROPERPART_NAME = "rcc8ntpp";
    public static final String RCC_NTANPROPERPARTINVERSE_NAME = "rcc8ntppi";

    //Topological DE-9IM function: relate
    public static final String RELATE_NAME = "relate";

    //Non-Topological function names:
    public static final String DISTANCE_NAME = "distance";
    public static final String BUFFER_NAME = "buffer";
    public static final String CONVEXHULL_NAME = "convexHull";
    public static final String INTERSECTION_NAME = "intersection";
    public static final String UNION_NAME = "union";
    public static final String DIFFERENCE_NAME = "difference";
    public static final String SYMDIFFERENCE_NAME = "symDifference";
    public static final String ENVELOPE_NAME = "envelope";
    public static final String BOUNDARY_NAME = "boundary";
    public static final String GETSRID_NAME = "getsrid";

    //Function URI
    public static String getFunctionURI(String baseURI, String name) {
        return baseURI + name;
    }
}
