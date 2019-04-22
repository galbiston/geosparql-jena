/*
 * Copyright 2019 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.implementation.vocabulary;

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
