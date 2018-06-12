/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import geo.topological.GenericPropertyFunction;
import static implementation.index.IndexDefaultValues.INDEX_EXPIRY_INTERVAL;
import static implementation.index.IndexDefaultValues.NO_INDEX;
import static implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import implementation.index.expiring.ExpiringIndex;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;

/**
 *
 */
public class QueryRewriteIndex {

    private static Boolean IS_INDEX_ACTIVE = true;
    private static final String QUERY_REWRITE_LABEL = "Geometry Transform";
    private static ExpiringIndex<String, Boolean> QUERY_REWRITE_INDEX = new ExpiringIndex<>(UNLIMITED_INDEX, INDEX_EXPIRY_INTERVAL, QUERY_REWRITE_LABEL);
    public static Long RETRIEVAL_COUNT = 0L;

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
        String key = subjectGeometryLiteral.getLexicalForm() + "@" + predicate.getURI() + "@" + objectGeometryLiteral.getLexicalForm();
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
            QUERY_REWRITE_INDEX = new ExpiringIndex<>(maxSize, INDEX_EXPIRY_INTERVAL, QUERY_REWRITE_LABEL);
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
            if (expiryInterval > 0) {
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
