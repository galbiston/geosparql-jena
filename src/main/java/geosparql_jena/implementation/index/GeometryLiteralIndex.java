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

import geosparql_jena.implementation.GeometryWrapper;
import geosparql_jena.implementation.datatype.DatatypeReader;
import static geosparql_jena.implementation.index.IndexDefaultValues.INDEX_EXPIRY_INTERVAL;
import static geosparql_jena.implementation.index.IndexDefaultValues.NO_INDEX;
import static geosparql_jena.implementation.index.IndexDefaultValues.UNLIMITED_EXPIRY;
import static geosparql_jena.implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import geosparql_jena.implementation.index.expiring.ExpiringIndex;
import java.util.Map;

/**
 *
 *
 */
public class GeometryLiteralIndex {

    private static Boolean IS_INDEX_ACTIVE = true;
    private static final String PRIMARY_INDEX_LABEL = "Primary Geometry Literal Index";
    private static final String SECONDARY_INDEX_LABEL = "Secondary Geometry Literal Index";
    private static ExpiringIndex<String, GeometryWrapper> PRIMARY_INDEX = new ExpiringIndex<>(UNLIMITED_INDEX, INDEX_EXPIRY_INTERVAL, PRIMARY_INDEX_LABEL);
    private static ExpiringIndex<String, GeometryWrapper> SECONDARY_INDEX = new ExpiringIndex<>(UNLIMITED_INDEX, INDEX_EXPIRY_INTERVAL, SECONDARY_INDEX_LABEL);
    private static Long RETRIEVAL_COUNT = 0L;

    public enum GeometryIndex {
        PRIMARY, SECONDARY
    }

    public static final GeometryWrapper retrieve(String geometryLiteral, DatatypeReader datatypeReader, GeometryIndex targetIndex) {
        GeometryWrapper geometryWrapper;

        switch (targetIndex) {
            case SECONDARY:
                geometryWrapper = retrieveMemoryIndex(geometryLiteral, datatypeReader, SECONDARY_INDEX, PRIMARY_INDEX);
                break;
            default:
                geometryWrapper = retrieveMemoryIndex(geometryLiteral, datatypeReader, PRIMARY_INDEX, SECONDARY_INDEX);
        }

        return geometryWrapper;
    }

    private static GeometryWrapper retrieveMemoryIndex(String geometryLiteral, DatatypeReader datatypeReader, Map<String, GeometryWrapper> index, Map<String, GeometryWrapper> otherIndex) {

        GeometryWrapper geometryWrapper;

        RETRIEVAL_COUNT++;

        if (IS_INDEX_ACTIVE) {

            try {
                if (index.containsKey(geometryLiteral)) {
                    geometryWrapper = index.get(geometryLiteral);
                } else {
                    if (otherIndex.containsKey(geometryLiteral)) {
                        geometryWrapper = otherIndex.get(geometryLiteral);
                    } else {
                        geometryWrapper = datatypeReader.read(geometryLiteral);
                    }
                    index.put(geometryLiteral, geometryWrapper);

                }

                return geometryWrapper;
            } catch (NullPointerException ex) {
                //Catch NullPointerException and fall through to default action.
            }
        }

        return datatypeReader.read(geometryLiteral);

    }

    /**
     * Empty the Geometry Literal Index.
     */
    public static final void clear() {
        if (PRIMARY_INDEX != null) {
            PRIMARY_INDEX.clear();
        }
        if (SECONDARY_INDEX != null) {
            SECONDARY_INDEX.clear();
        }
        RETRIEVAL_COUNT = 0L;
    }

    /**
     * Sets whether the Geometry Literal Indexes is active.
     * <br> The index will be empty after this process.
     *
     * @param maxSize : use -1 for unlimited size
     */
    public static final void setMaxSize(int maxSize) {

        IS_INDEX_ACTIVE = NO_INDEX != maxSize;

        if (IS_INDEX_ACTIVE) {
            if (PRIMARY_INDEX != null) {
                PRIMARY_INDEX.stopExpiry();
                SECONDARY_INDEX.stopExpiry();
            }
            PRIMARY_INDEX = new ExpiringIndex<>(maxSize, INDEX_EXPIRY_INTERVAL, PRIMARY_INDEX_LABEL);
            PRIMARY_INDEX.startExpiry();
            SECONDARY_INDEX = new ExpiringIndex<>(maxSize, INDEX_EXPIRY_INTERVAL, SECONDARY_INDEX_LABEL);
            SECONDARY_INDEX.startExpiry();
        } else {
            if (PRIMARY_INDEX != null) {
                PRIMARY_INDEX.stopExpiry();
                SECONDARY_INDEX.stopExpiry();
            }

            PRIMARY_INDEX = null;
            SECONDARY_INDEX = null;
        }
    }

    /**
     * Sets the expiry time in milliseconds of the Geometry Literal Indexes, if
     * active.
     *
     * @param expiryInterval : use 0 or negative for unlimited timeout
     */
    public static final void setExpiry(long expiryInterval) {

        if (IS_INDEX_ACTIVE) {
            if (expiryInterval > UNLIMITED_EXPIRY) {
                PRIMARY_INDEX.stopExpiry();
                PRIMARY_INDEX.setExpiryInterval(expiryInterval);
                PRIMARY_INDEX.startExpiry();
                SECONDARY_INDEX.stopExpiry();
                SECONDARY_INDEX.setExpiryInterval(expiryInterval);
                SECONDARY_INDEX.startExpiry();
            } else {
                PRIMARY_INDEX.stopExpiry();
                SECONDARY_INDEX.stopExpiry();
            }
        }
    }

    public static final Integer getPrimaryIndexSize() {
        if (PRIMARY_INDEX != null) {
            return PRIMARY_INDEX.size();
        } else {
            return 0;
        }
    }

    public static final Integer getSecondaryIndexSize() {
        if (SECONDARY_INDEX != null) {
            return SECONDARY_INDEX.size();
        } else {
            return 0;
        }
    }

    public static final Long getRetrievalCount() {
        return RETRIEVAL_COUNT;
    }

}
