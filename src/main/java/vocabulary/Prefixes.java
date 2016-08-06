/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author haozhechen
 */
public class Prefixes {

    public static final Map<String, String> get() {
        Map<String, String> map = new HashMap<>();
        map.put("osgb", "http://www.ordnancesurvey.co.uk/xml/namespaces/osgb");
        map.put("gml", "http://www.opengis.net/ont/gml#");
        map.put("ntu", "http://ntu.ac.uk/ont/geo#");
        map.put("geo", "http://www.opengis.net/ont/geosparql#");
        map.put("geof", "http://www.opengis.net/def/function/geosparql/");
        map.put("ext", "http://example/f#");
        map.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        map.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        map.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        map.put("uom", "http://www.opengis.net/def/uom/OGC/1.0/");

        return map;
    }

}
