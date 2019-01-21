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
import static io.github.galbiston.expiring_map.MapDefaultValues.NO_MAP;
import static io.github.galbiston.expiring_map.MapDefaultValues.UNLIMITED_EXPIRY;
import static io.github.galbiston.expiring_map.MapDefaultValues.UNLIMITED_MAP;
import io.github.galbiston.geosparql_jena.geo.topological.GenericPropertyFunction;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;

/**
 *
 */
public class QueryRewriteIndex {

    private Boolean indexActive = true;
    private final String queryRewriteLabel;
    private ExpiringMap<String, Boolean> queryRewriteIndex;
    public Long retrievalCount = 0L;
    private static int MAP_SIZE_DEFAULT = UNLIMITED_MAP;
    private static long MAP_EXPIRY_INTERVAL_DEFAULT = MAP_EXPIRY_INTERVAL;

    public QueryRewriteIndex() {
        this.queryRewriteLabel = "Query Rewrite";
        this.queryRewriteIndex = new ExpiringMap<>(queryRewriteLabel, MAP_SIZE_DEFAULT, MAP_EXPIRY_INTERVAL_DEFAULT);
    }

    public QueryRewriteIndex(String queryRewriteLabel, int maxSize, long expiryInterval) {
        this.queryRewriteLabel = queryRewriteLabel;
        this.queryRewriteIndex = new ExpiringMap<>(queryRewriteLabel, maxSize, expiryInterval);
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

        Boolean result;
        String key = subjectGeometryLiteral.getLiteralLexicalForm() + "@" + predicate.getURI() + "@" + objectGeometryLiteral.getLiteralLexicalForm();
        retrievalCount++;
        if (indexActive) {
            try {
                if (queryRewriteIndex.containsKey(key)) {
                    result = queryRewriteIndex.get(key);
                } else {
                    result = propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
                    queryRewriteIndex.put(key, result);
                }
                return result;
            } catch (NullPointerException ex) {
                //Catch NullPointerException and fall through to default action.
            }
        }

        return propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
    }

    public final void clear() {
        if (queryRewriteIndex != null) {
            queryRewriteIndex.clear();
        }
        retrievalCount = 0L;
    }

    /**
     * Sets whether the Query Rewrite Index is active.
     * <br> The index will be empty after this process.
     *
     * @param maxSize : use -1 for unlimited size
     */
    public final void setMapSize(int maxSize) {
        indexActive = NO_MAP != maxSize;

        if (indexActive) {
            if (queryRewriteIndex != null) {
                queryRewriteIndex.stopExpiry();
            }
            queryRewriteIndex = new ExpiringMap<>(queryRewriteLabel, maxSize, MAP_EXPIRY_INTERVAL_DEFAULT);
            queryRewriteIndex.startExpiry();
        } else {
            if (queryRewriteIndex != null) {
                queryRewriteIndex.stopExpiry();
            }
            queryRewriteIndex = null;
        }
    }

    /**
     * Sets the expiry time in milliseconds of the Query Rewrite Index, if
     * active.
     *
     * @param expiryInterval : use 0 or negative for unlimited timeout
     */
    public final void setMapExpiry(long expiryInterval) {

        if (indexActive) {
            if (expiryInterval > UNLIMITED_EXPIRY) {
                queryRewriteIndex.stopExpiry();
                queryRewriteIndex.setExpiryInterval(expiryInterval);
                queryRewriteIndex.startExpiry();
            } else {
                queryRewriteIndex.stopExpiry();
            }
        }
    }

    public final void setActive(boolean isActive) {
        indexActive = isActive;
    }

    public final Integer getQueryRewriteIndexSize() {
        if (queryRewriteIndex != null) {
            return queryRewriteIndex.size();
        } else {
            return 0;
        }
    }

    public static void setMaxSize(int mapSizeDefault) {
        QueryRewriteIndex.MAP_SIZE_DEFAULT = mapSizeDefault;
    }

    public static void setExpiry(long mapExpiryIntervalDefault) {
        QueryRewriteIndex.MAP_EXPIRY_INTERVAL_DEFAULT = mapExpiryIntervalDefault;
    }

    public final Long getRetrievalCount() {
        return retrievalCount;
    }
}
