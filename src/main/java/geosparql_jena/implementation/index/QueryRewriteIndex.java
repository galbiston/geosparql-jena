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
package geosparql_jena.implementation.index;

import geosparql_jena.geo.topological.GenericPropertyFunction;
import io.github.galbiston.expiring_index.ExpiringIndex;
import static io.github.galbiston.expiring_index.IndexDefaultValues.INDEX_EXPIRY_INTERVAL;
import static io.github.galbiston.expiring_index.IndexDefaultValues.NO_INDEX;
import static io.github.galbiston.expiring_index.IndexDefaultValues.UNLIMITED_EXPIRY;
import static io.github.galbiston.expiring_index.IndexDefaultValues.UNLIMITED_INDEX;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;

/**
 *
 */
public class QueryRewriteIndex {

    private static Boolean IS_INDEX_ACTIVE = true;
    private static final String QUERY_REWRITE_LABEL = "Query Rewrite";
    private static ExpiringIndex<String, Boolean> QUERY_REWRITE_INDEX = new ExpiringIndex<>(QUERY_REWRITE_LABEL, UNLIMITED_INDEX, INDEX_EXPIRY_INTERVAL);
    public static Long RETRIEVAL_COUNT = 0L;

    /**
     *
     * @param subjectGeometryLiteral
     * @param predicate
     * @param objectGeometryLiteral
     * @param propertyFunction
     * @return
     */
    public static final Boolean test(Node subjectGeometryLiteral, Property predicate, Node objectGeometryLiteral, GenericPropertyFunction propertyFunction) {

        if (!subjectGeometryLiteral.isLiteral() || !objectGeometryLiteral.isLiteral()) {
            return false;
        }

        Boolean result;
        String key = subjectGeometryLiteral.getLiteralLexicalForm() + "@" + predicate.getURI() + "@" + objectGeometryLiteral.getLiteralLexicalForm();
        RETRIEVAL_COUNT++;
        if (IS_INDEX_ACTIVE) {
            try {
                if (QUERY_REWRITE_INDEX.containsKey(key)) {
                    result = QUERY_REWRITE_INDEX.get(key);
                } else {
                    result = propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
                    QUERY_REWRITE_INDEX.put(key, result);
                }
                return result;
            } catch (NullPointerException ex) {
                //Catch NullPointerException and fall through to default action.
            }
        }

        return propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
    }

    public static final void clear() {
        if (QUERY_REWRITE_INDEX != null) {
            QUERY_REWRITE_INDEX.clear();
        }
        RETRIEVAL_COUNT = 0L;
    }

    /**
     * Sets whether the Query Rewrite Index is active.
     * <br> The index will be empty after this process.
     *
     * @param maxSize : use -1 for unlimited size
     */
    public static final void setMaxSize(int maxSize) {
        IS_INDEX_ACTIVE = NO_INDEX != maxSize;

        if (IS_INDEX_ACTIVE) {
            if (QUERY_REWRITE_INDEX != null) {
                QUERY_REWRITE_INDEX.stopExpiry();
            }
            QUERY_REWRITE_INDEX = new ExpiringIndex<>(QUERY_REWRITE_LABEL, maxSize, INDEX_EXPIRY_INTERVAL);
            QUERY_REWRITE_INDEX.startExpiry();
        } else {
            if (QUERY_REWRITE_INDEX != null) {
                QUERY_REWRITE_INDEX.stopExpiry();
            }
            QUERY_REWRITE_INDEX = null;
        }
    }

    /**
     * Sets the expiry time in milliseconds of the Query Rewrite Index, if
     * active.
     *
     * @param expiryInterval : use 0 or negative for unlimited timeout
     */
    public static final void setExpiry(long expiryInterval) {

        if (IS_INDEX_ACTIVE) {
            if (expiryInterval > UNLIMITED_EXPIRY) {
                QUERY_REWRITE_INDEX.stopExpiry();
                QUERY_REWRITE_INDEX.setExpiryInterval(expiryInterval);
                QUERY_REWRITE_INDEX.startExpiry();
            } else {
                QUERY_REWRITE_INDEX.stopExpiry();
            }
        }
    }

    public static final Integer getQueryRewriteIndexSize() {
        if (QUERY_REWRITE_INDEX != null) {
            return QUERY_REWRITE_INDEX.size();
        } else {
            return 0;
        }
    }

    public static final Long getRetrievalCount() {
        return RETRIEVAL_COUNT;
    }
}
