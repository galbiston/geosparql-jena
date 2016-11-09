/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.support;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class Prefixes {

    //Predefined Prefix URIs
    public static final String SF_URI = "http://www.opengis.net/ont/sf#";
    public static final String GEOF_URI = "http://www.opengis.net/def/function/geosparql/";
    public static final String GEO_URI = "http://www.opengis.net/ont/geosparql#";
    public static final String NTU_URI = "http://ntu.ac.uk/ont/geo#";
    public static final String XSD_URI = "http://www.w3.org/2001/XMLSchema#";
    public static final String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String RDFS_URI = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String UOM_URI = "http://www.opengis.net/def/uom/OGC/1.0/";
//    public static final String LGD_URI = "http://linkedgeodata.org/ontology/";
//    public static final String LGDG_URI = "http://linkedgeodata.org/geometry/";

    public static final Map<String, String> get() {
        Map<String, String> map = new HashMap<>();

        map.put("sf", SF_URI);
        map.put("ntu", NTU_URI);
        map.put("geo", GEO_URI);
        map.put("geof", GEOF_URI);
        map.put("xsd", XSD_URI);
        map.put("rdf", RDF_URI);
        map.put("rdfs", RDFS_URI);
        map.put("uom", UOM_URI);

        return map;
    }

    /**
     * This method will add all the additional prefixes in the map if required
     *
     * @param prefixMap - the additional prefix map
     * @return the prefix map which combined the additional prefixes
     */
    public static final Map<String, String> get(Map<String, String> prefixMap) {
        Map<String, String> map = new HashMap<>();

        map.put("sf", SF_URI);
        map.put("geo", GEO_URI);
        map.put("geof", GEOF_URI);
        map.put("xsd", XSD_URI);
        map.put("rdf", RDF_URI);
        map.put("rdfs", RDFS_URI);
        map.put("uom", UOM_URI);
        map.putAll(prefixMap);

        return map;
    }

}
