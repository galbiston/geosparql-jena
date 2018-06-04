/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import geo.topological.GenericPropertyFunction;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.mina.util.ExpiringMap;

/**
 *
 */
public class QueryRewriteIndex {

    private static ExpiringMap<String, Boolean> QUERY_REWRITE_INDEX = new ExpiringMap<>();
    //private static MultiKeyMap<MultiKey, Boolean> QUERY_REWRITE_INDEX = MultiKeyMap.multiKeyMap(new LRUMap<>(IndexDefaultValues.QUERY_REWRITE_INDEX_MAX_SIZE_DEFAULT));
    private static Boolean IS_INDEX_ACTIVE = true;

    /**
     *
     * @param subjectGeometryLiteral
     * @param predicate
     * @param objectGeometryLiteral
     * @param propertyFunction
     * @return
     */
    public static final Boolean test(Literal subjectGeometryLiteral, Property predicate, Literal objectGeometryLiteral, GenericPropertyFunction propertyFunction) {

        Boolean result;
        //MultiKey key = new MultiKey<>(subjectGeometryLiteral, predicate, objectGeometryLiteral);
        String key = subjectGeometryLiteral.getLexicalForm() + "@" + predicate.getURI() + "@" + objectGeometryLiteral.getLexicalForm();

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

    public static final void clear() {
        QUERY_REWRITE_INDEX.clear();
    }

    /**
     * Sets whether the Query Rewrite Index is active.
     * <br> The index will be empty after this process.
     *
     * @param isActive
     */
    public static final void setActive(Boolean isActive) {

        IS_INDEX_ACTIVE = isActive;

        QUERY_REWRITE_INDEX.clear();
        QUERY_REWRITE_INDEX = new ExpiringMap<>();
    }

}
