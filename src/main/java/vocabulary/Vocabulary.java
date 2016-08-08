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
    public static final String GEO_URI = "http://www.opengis.net/ont/geosparql#";
    public static final String NTU_URI = "http://ntu.ac.uk/ont/geo#";

    //Property
    public static final Property GML_PRO = ResourceFactory.createProperty(GML_URI + "asGML");
    public static final Property GEOM_PRO = ResourceFactory.createProperty(GEO_URI + "hasGeometry");
    public static final Property GEOMPOINT_PRO = ResourceFactory.createProperty(GEO_URI + "hasPointGeometry");

    //Units Of Measurement URI
    public static final String METRE_URI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
    public static final String DEGREE_URI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
    public static final String GRIDSPACING_URI = "http://www.opengis.net/def/uom/OGC/1.0/GridSpacing";
    public static final String RADIAN_URI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
    public static final String UNITY_URI = "http://www.opengis.net/def/uom/OGC/1.0/unity";

    //Topological function symbols:
    public static final String CONTAINS_SYMBOL = "geof:sfContains";
    public static final String CROSSES_SYMBOL = "geof:sfCrosses";
    public static final String DISJOINT_SYMBOL = "geof:sfDisjoint";
    public static final String EQUALS_SYMBOL = "geof:sfEquals";
    public static final String INTERSECTS_SYMBOL = "geof:sfIntersects";
    public static final String OVERLAPS_SYMBOL = "geof:sfOverlaps";
    public static final String TOUCHES_SYMBOL = "geof:sfTouches";
    public static final String WITHIN_SYMBOL = "geof:sfWithin";

    //Topological function names:
    public static final String CONTAINS_NAME = "sfContains";
    public static final String CROSSES_NAME = "sfCrosses";
    public static final String DISJOINT_NAME = "sfDisjoint";
    public static final String EQUALS_NAME = "sfEquals";
    public static final String INTERSECTS_NAME = "sfIntersects";
    public static final String OVERLAPS_NAME = "sfOverlaps";
    public static final String TOUCHES_NAME = "sfTouches";
    public static final String WITHIN_NAME = "sfWithin";

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
