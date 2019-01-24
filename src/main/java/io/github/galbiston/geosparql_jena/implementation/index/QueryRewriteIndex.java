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
package io.github.galbiston.geosparql_jena.implementation.index;

import io.github.galbiston.expiring_map.ExpiringMap;
import static io.github.galbiston.expiring_map.MapDefaultValues.MAP_EXPIRY_INTERVAL;
import static io.github.galbiston.expiring_map.MapDefaultValues.UNLIMITED_MAP;
import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.geo.topological.GenericPropertyFunction;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;

/**
 *
 */
public class QueryRewriteIndex {

    private boolean indexActive;
    private final String queryRewriteLabel;
    private ExpiringMap<String, Boolean> index;
    private static String LABEL_DEFAULT = "Query Rewrite";
    private static int MAP_SIZE_DEFAULT = UNLIMITED_MAP;
    private static long MAP_EXPIRY_INTERVAL_DEFAULT = MAP_EXPIRY_INTERVAL;

    public QueryRewriteIndex() {
        this.queryRewriteLabel = LABEL_DEFAULT;
        this.indexActive = false;
        this.index = new ExpiringMap<>(queryRewriteLabel, 1, MAP_EXPIRY_INTERVAL);
    }

    public QueryRewriteIndex(String queryRewriteLabel, int maxSize, long expiryInterval) {
        this.queryRewriteLabel = queryRewriteLabel;
        this.indexActive = true;
        this.index = new ExpiringMap<>(queryRewriteLabel, maxSize, expiryInterval);
    }

    /**
     *
     * @param subjectGeometryLiteral
     * @param predicate
     * @param objectGeometryLiteral
     * @param propertyFunction
     * @return Result of relation between subject and object.
     */
    public final Boolean test(Node subjectGeometryLiteral, Property predicate, Node objectGeometryLiteral, GenericPropertyFunction propertyFunction) {

        if (!subjectGeometryLiteral.isLiteral() || !objectGeometryLiteral.isLiteral()) {
            return false;
        }

        if (indexActive) {
            String key = subjectGeometryLiteral.getLiteralLexicalForm() + "@" + predicate.getURI() + "@" + objectGeometryLiteral.getLiteralLexicalForm();
            try {
                Boolean result;
                if (index.containsKey(key)) {
                    result = index.get(key);
                } else {
                    result = propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
                    index.put(key, result);
                }
                return result;
            } catch (NullPointerException ex) {
                //Catch NullPointerException and fall through to default action.
            }
        }

        return propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
    }

    /**
     * Empty the index.
     */
    public final void clear() {
        index.clear();
    }

    /**
     * Sets whether the Query Rewrite Index is active.
     * <br> The index will be empty after this process.
     *
     * @param maxSize : use -1 for unlimited size
     */
    public final void setMapSize(int maxSize) {
        index.setMaxSize(maxSize);
    }

    /**
     * Sets the expiry time in milliseconds of the Query Rewrite Index, if
     * active.
     *
     * @param expiryInterval : use 0 or negative for unlimited timeout
     */
    public final void setMapExpiry(long expiryInterval) {
        index.setExpiryInterval(expiryInterval);
    }

    /**
     *
     * @return True if index is active.
     */
    public boolean isIndexActive() {
        return indexActive;
    }

    /**
     * Sets whether the index is active.
     *
     * @param indexActive
     */
    public final void setActive(boolean indexActive) {
        this.indexActive = indexActive;

        if (indexActive) {
            index.startExpiry();
        } else {
            index.stopExpiry();
        }
    }

    /**
     *
     * @return Number of items in the index.
     */
    public final long getIndexSize() {
        return index.mappingCount();
    }

    /**
     * Reset the index to the provided max size and expiry interval.<br>
     * All contents will be lost.
     *
     * @param maxSize
     * @param expiryInterval
     */
    public void reset(int maxSize, long expiryInterval) {
        index = new ExpiringMap<>(queryRewriteLabel, maxSize, expiryInterval);
    }

    public static void setMaxSize(int mapSizeDefault) {
        QueryRewriteIndex.MAP_SIZE_DEFAULT = mapSizeDefault;
    }

    public static void setExpiry(long mapExpiryIntervalDefault) {
        QueryRewriteIndex.MAP_EXPIRY_INTERVAL_DEFAULT = mapExpiryIntervalDefault;
    }

    public static QueryRewriteIndex createDefault() {

        if (GeoSPARQLConfig.isQueryRewriteEnabled()) {
            return new QueryRewriteIndex(LABEL_DEFAULT, MAP_SIZE_DEFAULT, MAP_EXPIRY_INTERVAL_DEFAULT);
        } else {
            return new QueryRewriteIndex();
        }
    }

}
