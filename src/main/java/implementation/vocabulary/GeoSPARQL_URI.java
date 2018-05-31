/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.vocabulary;

import java.util.HashMap;

/**
 *
 *
 */
public class GeoSPARQL_URI {

    //URI
    public static final String SF_URI = "http://www.opengis.net/ont/sf#";
    public static final String GML_URI = "http://www.opengis.net/ont/gml#";
    public static final String GEOF_URI = "http://www.opengis.net/def/function/geosparql/";
    public static final String GEOR_URI = "http://www.opengis.net/def/rule/geosparql/";
    public static final String GEO_URI = "http://www.opengis.net/ont/geosparql#";
    public static final String XSD_URI = "http://www.w3.org/2001/XMLSchema#";
    public static final String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String RDFS_URI = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String UOM_URI = "http://www.opengis.net/def/uom/OGC/1.0/";

    private static final HashMap<String, String> PREFIXES = new HashMap<>();

    public static final HashMap<String, String> getPrefixes() {

        if (PREFIXES.isEmpty()) {
            PREFIXES.put("sf", SF_URI);
            PREFIXES.put("gml", GML_URI);
            PREFIXES.put("geof", GEOF_URI);
            PREFIXES.put("geo", GEO_URI);
            PREFIXES.put("xsd", XSD_URI);
            PREFIXES.put("rdf", RDF_URI);
            PREFIXES.put("rdfs", RDFS_URI);
            PREFIXES.put("uom", UOM_URI);
        }
        return PREFIXES;
    }

}
