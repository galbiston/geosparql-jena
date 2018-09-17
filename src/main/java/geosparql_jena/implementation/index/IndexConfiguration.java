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

import static geosparql_jena.implementation.index.IndexDefaultValues.NO_INDEX;
import static geosparql_jena.implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import geosparql_jena.implementation.registry.CRSRegistry;
import geosparql_jena.implementation.registry.MathTransformRegistry;
import java.util.UUID;

/**
 *
 *
 */
public class IndexConfiguration {

    public enum IndexOption {
        NONE, MEMORY
    }

    /*
     * Index Configuration Parameters
     */
    private static IndexOption indexOptionEnum = IndexOption.MEMORY;

    public static final void setConfig(IndexOption indexOption) {
        indexOptionEnum = indexOption;

        switch (indexOptionEnum) {
            case MEMORY:
                IndexConfiguration.setupMemoryIndex();
                break;
            default:
                IndexConfiguration.setupNoIndex();
        }
    }

    public static void setupNoIndex() {
        GeometryLiteralIndex.setMaxSize(NO_INDEX);
        GeometryTransformIndex.setMaxSize(NO_INDEX);
        QueryRewriteIndex.setMaxSize(NO_INDEX);
    }

    public static void setupMemoryIndex() {
        GeometryLiteralIndex.setMaxSize(UNLIMITED_INDEX);
        GeometryTransformIndex.setMaxSize(UNLIMITED_INDEX);
        QueryRewriteIndex.setMaxSize(UNLIMITED_INDEX);
    }

    /**
     * Set the maximum size of the indexes and switch spatial index on/off. Zero
     * for no index and -1 for unlimited size.
     *
     * @param geometryLiteralIndex
     * @param geometryTransformIndex
     * @param queryRewriteIndex
     */
    public static final void setIndexMaxSize(int geometryLiteralIndex, int geometryTransformIndex, int queryRewriteIndex) {
        GeometryLiteralIndex.setMaxSize(geometryLiteralIndex);
        GeometryTransformIndex.setMaxSize(geometryTransformIndex);
        QueryRewriteIndex.setMaxSize(queryRewriteIndex);
    }

    /**
     * Set the index expiry interval in milliseconds.
     *
     * @param geometryLiteralIndex
     * @param geometryTransformIndex
     * @param queryRewriteIndex
     */
    public static final void setIndexExpiry(long geometryLiteralIndex, long geometryTransformIndex, long queryRewriteIndex) {
        GeometryLiteralIndex.setExpiry(geometryLiteralIndex);
        GeometryTransformIndex.setExpiry(geometryTransformIndex);
        QueryRewriteIndex.setExpiry(queryRewriteIndex);
    }

    public static final void resetIndexesAndRegistries() {
        GeometryLiteralIndex.clear();
        QueryRewriteIndex.clear();
        CRSRegistry.reset();
        MathTransformRegistry.clear();
    }

    public static final IndexOption getIndexOption() {
        return indexOptionEnum;
    }

    public static final String createURI(String namespaceURI, String prefix) {
        return namespaceURI + prefix + "-" + UUID.randomUUID().toString();
    }

}
