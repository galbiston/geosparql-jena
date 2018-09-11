/*
 * Copyright 2018 the original author or authors.
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
package conformance_test;

import static implementation.vocabulary.GeoSPARQL_URI.GEOF_URI;
import static implementation.vocabulary.GeoSPARQL_URI.GEOR_URI;
import static implementation.vocabulary.GeoSPARQL_URI.GEO_URI;
import static implementation.vocabulary.GeoSPARQL_URI.RDFS_URI;
import static implementation.vocabulary.GeoSPARQL_URI.RDF_URI;
import static implementation.vocabulary.GeoSPARQL_URI.SF_URI;
import static implementation.vocabulary.GeoSPARQL_URI.UOM_URI;
import static implementation.vocabulary.GeoSPARQL_URI.XSD_URI;
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
