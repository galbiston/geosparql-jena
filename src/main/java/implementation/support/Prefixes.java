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
 */
public class Prefixes {

    //URI
    public static final String SF_URI = "http://www.opengis.net/ont/sf#";
    public static final String GML_URI = "http://www.opengis.net/ont/gml#";
    public static final String GEOF_URI = "http://www.opengis.net/def/function/geosparql/";
    public static final String GEOR_URI = "http://www.opengis.net/def/rule/geosparql/";
    public static final String GEO_URI = "http://www.opengis.net/ont/geosparql#";
    public static final String NTU_URI = "http://ntu.ac.uk/ont/geo#";
    public static final String XSD_URI = "http://www.w3.org/2001/XMLSchema#";
    public static final String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String RDFS_URI = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String UOM_URI = "http://www.opengis.net/def/uom/OGC/1.0/";
    public static final String LGD_URI = "http://linkedgeodata.org/ontology/";
    public static final String LGDG_URI = "http://linkedgeodata.org/geometry/";

    public static final Map<String, String> get() {
        Map<String, String> map = new HashMap<>();
        map.put("sf", SF_URI);
        map.put("ntu", NTU_URI);
        map.put("geo", GEO_URI);
        map.put("geof", GEOF_URI);
        map.put("geor", GEOR_URI);
        map.put("xsd", XSD_URI);
        map.put("rdf", RDF_URI);
        map.put("rdfs", RDFS_URI);
        map.put("uom", UOM_URI);
        map.put("lgd", LGD_URI);
        map.put("lgdg", LGDG_URI);

        return map;
    }

}
