/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import static implementation.index.IndexDefaultValues.NO_INDEX;
import static implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import implementation.registry.CRSRegistry;
import implementation.registry.MathTransformRegistry;
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
     * Set the maximum size of the indexes. Zero for no index and -1 for
     * unlimited size.
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

    public static final void clearAllIndexesAndRegistries() {
        GeometryLiteralIndex.clear();
        QueryRewriteIndex.clear();
        CRSRegistry.clearAll();
        MathTransformRegistry.clear();
    }

    public static final IndexOption getIndexOption() {
        return indexOptionEnum;
    }

    public static final String createURI(String namespaceURI, String prefix) {
        return namespaceURI + prefix + "-" + UUID.randomUUID().toString();
    }

}
