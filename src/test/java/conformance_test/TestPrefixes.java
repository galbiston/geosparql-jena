/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test;

import static implementation.vocabulary.Prefixes.GEOF_URI;
import static implementation.vocabulary.Prefixes.GEOR_URI;
import static implementation.vocabulary.Prefixes.GEO_URI;
import static implementation.vocabulary.Prefixes.RDFS_URI;
import static implementation.vocabulary.Prefixes.RDF_URI;
import static implementation.vocabulary.Prefixes.SF_URI;
import static implementation.vocabulary.Prefixes.UOM_URI;
import static implementation.vocabulary.Prefixes.XSD_URI;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 */
public class TestPrefixes {

    //Test Data URIs
    public static final String LGD_URI = "http://linkedgeodata.org/ontology/";
    public static final String LGDG_URI = "http://linkedgeodata.org/geometry/";
    public static final String EXAMPLE_URI = "http://example.org/Schema#";
    public static final String FEATURE_EXAMPLE_URI = "http://example.org/Feature#";
    public static final String GEOMETRY_EXAMPLE_URI = "http://example.org/Geometry#";

    public static final Map<String, String> get() {
        Map<String, String> map = new HashMap<>();
        map.put("sf", SF_URI);
        map.put("geo", GEO_URI);
        map.put("geof", GEOF_URI);
        map.put("geor", GEOR_URI);
        map.put("xsd", XSD_URI);
        map.put("rdf", RDF_URI);
        map.put("rdfs", RDFS_URI);
        map.put("uom", UOM_URI);
        map.put("lgd", LGD_URI);
        map.put("lgdg", LGDG_URI);
        map.put("ex", EXAMPLE_URI);
        map.put("feat", FEATURE_EXAMPLE_URI);
        map.put("geom", GEOMETRY_EXAMPLE_URI);

        return map;
    }

}
