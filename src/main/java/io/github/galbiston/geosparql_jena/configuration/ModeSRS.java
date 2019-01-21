/*
 * Copyright 2019 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.configuration;

import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;

/**
 *
 *
 */
public class ModeSRS {

    private final HashMap<String, Integer> srsMap = new HashMap<>();
    private List<Entry<String, Integer>> srsList = new ArrayList<>();

    public ModeSRS() {
    }

    public void search(Model model) {

        NodeIterator nodeIter = model.listObjectsOfProperty(Geo.HAS_SERIALIZATION_PROP);
        while (nodeIter.hasNext()) {
            RDFNode node = nodeIter.next();
            if (node.isLiteral()) {
                String geomLiteral = node.asLiteral().getLexicalForm();
                String srsURI;
                int start = geomLiteral.indexOf("<");
                if (start > -1) {
                    int end = geomLiteral.indexOf(">");
                    srsURI = geomLiteral.substring(start + 1, end);
                } else {
                    srsURI = SRS_URI.DEFAULT_WKT_CRS84;
                }

                //Put the SRS URI into the map.
                Integer count;
                if (srsMap.containsKey(srsURI)) {
                    count = srsMap.get(srsURI);
                    count++;
                } else {
                    count = 1;
                }
                srsMap.put(srsURI, count);
            }
        }

        srsList = srsMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toList());
    }

    public List<Entry<String, Integer>> getSrsList() {
        return srsList;
    }

    public HashMap<String, Integer> getSrsMap() {
        return srsMap;
    }

    public String getModeURI() {

        if (!srsList.isEmpty()) {
            return srsList.get(0).getKey();
        } else {
            return null;
        }
    }

}