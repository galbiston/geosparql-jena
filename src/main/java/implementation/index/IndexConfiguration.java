/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

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
        GeometryLiteralIndex.setActive(Boolean.FALSE);
        GeometryTransformIndex.setActive(Boolean.FALSE);
        QueryRewriteIndex.setActive(Boolean.FALSE);
    }

    public static final void clearAllIndexesAndRegistries() {
        GeometryLiteralIndex.clear();
        QueryRewriteIndex.clear();
        CRSRegistry.clearAll();
        MathTransformRegistry.clear();
    }

    public static final void setIndex(Boolean geometryLiteralIndex, Boolean geometryTransformIndex, Boolean queryRewriteIndex) {
        GeometryLiteralIndex.setActive(geometryLiteralIndex);
        GeometryTransformIndex.setActive(geometryTransformIndex);
        QueryRewriteIndex.setActive(queryRewriteIndex);
    }

    public static void setupMemoryIndex() {
        GeometryLiteralIndex.setActive(Boolean.TRUE);
        GeometryTransformIndex.setActive(Boolean.TRUE);
        QueryRewriteIndex.setActive(Boolean.TRUE);
    }

    public static final IndexOption getIndexOption() {
        return indexOptionEnum;
    }

    public static final String createURI(String namespaceURI, String prefix) {
        return namespaceURI + prefix + "-" + UUID.randomUUID().toString();
    }

}
