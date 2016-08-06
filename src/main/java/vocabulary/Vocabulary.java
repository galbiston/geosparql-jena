/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 * @author haozhechen
 */
public class Vocabulary {

    //URI
    public static final String NTU_NAMESPACE = "http://ntu.ac.uk/";
    public static final String GML_URI = "http://www.opengis.net/ont/gml#";
    public static final String GEOF_URI = "http://www.opengis.net/def/function/geosparql/";
    public static final String GEO_URI = "http://www.opengis.net/ont/geosparql#";
    public static final String NTU_URI = "http://ntu.ac.uk/ont/geo#";

    //Resource
    public static final Resource BUILDING_TYPE_RES = ResourceFactory.createResource(NTU_NAMESPACE + "building");

    //Property
    public static final Property GML_PRO = ResourceFactory.createProperty(GML_URI + "asGML");
    public static final Property GEOM_PRO = ResourceFactory.createProperty(GEO_URI + "hasGeometry");

    //Units Of Measurement URI
    public static final String METRE_URI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
    public static final String DEGREE_URI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
    public static final String GRIDSPACING_URI = "http://www.opengis.net/def/uom/OGC/1.0/GridSpacing";
    public static final String RADIAN_URI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
    public static final String UNITY_URI = "http://www.opengis.net/def/uom/OGC/1.0/unity";

}
