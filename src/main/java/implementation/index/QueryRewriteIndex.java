/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import geo.topological.GenericPropertyFunction;
import java.io.File;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;

/**
 *
 * @author Gerg
 */
public class QueryRewriteIndex {

    private static MultiKeyMap<MultiKey, Boolean> QUERY_REWRITE_INDEX = MultiKeyMap.multiKeyMap(new LRUMap<>(IndexDefaultValues.QUERY_REWRITE_INDEX_MAX_SIZE_DEFAULT));
    private static Boolean IS_INDEX_ACTIVE = true;

    /**
     *
     * @param subjectGeometryLiteral
     * @param predicate
     * @param objectGeometryLiteral
     * @param propertyFunction
     * @return
     */
    @SuppressWarnings("unchecked")
    public static final Boolean test(Literal subjectGeometryLiteral, Property predicate, Literal objectGeometryLiteral, GenericPropertyFunction propertyFunction) {

        Boolean result;
        MultiKey key = new MultiKey<>(subjectGeometryLiteral, predicate, objectGeometryLiteral);
        if (QUERY_REWRITE_INDEX.containsKey(key)) {
            result = QUERY_REWRITE_INDEX.get(key);
        } else {
            result = propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
            if (IS_INDEX_ACTIVE) {
                QUERY_REWRITE_INDEX.put(key, result);
            }
        }
        return result;
    }

    public static final void write(File indexFile) {
        IndexUtils.write(indexFile, QUERY_REWRITE_INDEX);
    }

    public static final void read(File indexFile) {
        QUERY_REWRITE_INDEX.clear();
        IndexUtils.read(indexFile, QUERY_REWRITE_INDEX);
    }

    public static final void clear() {
        QUERY_REWRITE_INDEX.clear();
    }

    /**
     * Changes the max size of the Geometry Transform Index.
     * <br> The index will be empty after this process.
     *
     * @param maxSize
     */
    public static final void setMaxSize(Integer maxSize) {

        IS_INDEX_ACTIVE = maxSize != 0;

        MultiKeyMap<MultiKey, Boolean> newQueryRewriteIndex;
        if (IS_INDEX_ACTIVE) {
            newQueryRewriteIndex = MultiKeyMap.multiKeyMap(new LRUMap<>(maxSize));
        } else {
            newQueryRewriteIndex = MultiKeyMap.multiKeyMap(new LRUMap<>(IndexDefaultValues.INDEX_MINIMUM_SIZE));
        }
        QUERY_REWRITE_INDEX.clear();
        QUERY_REWRITE_INDEX = newQueryRewriteIndex;
    }

}
