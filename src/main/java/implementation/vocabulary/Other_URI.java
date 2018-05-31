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
public class Other_URI {

    private static final HashMap<String, String> PREFIXES = new HashMap<>();

    public static final String DC_URI = "http://purl.org/dc/elements/1.1/";
    public static final String SKOS_URI = "http://www.w3.org/2004/02/skos/core#";
    public static final String OWL_URI = "http://www.w3.org/2002/07/owl#";

    public static final HashMap<String, String> getPrefixes() {
        if (PREFIXES.isEmpty()) {
            PREFIXES.put("dc", DC_URI);
            PREFIXES.put("skos", SKOS_URI);
            PREFIXES.put("owl", OWL_URI);
        }

        return PREFIXES;
    }

}
