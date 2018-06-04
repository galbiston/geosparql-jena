/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import geo.topological.GenericPropertyFunction;
import static implementation.index.IndexDefaultValues.NO_INDEX;
import static implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import java.lang.invoke.MethodHandles;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.mina.util.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class QueryRewriteIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Boolean IS_INDEX_ACTIVE = true;
    private static Integer INDEX_MAX_SIZE = IndexDefaultValues.UNLIMITED_INDEX;
    private static Integer INDEX_TIMEOUT_SECONDS = IndexDefaultValues.INDEX_TIMEOUT_SECONDS;
    private static ExpiringMap<String, Boolean> QUERY_REWRITE_INDEX = new ExpiringMap<>(IndexDefaultValues.INDEX_TIMEOUT_SECONDS);

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

        if (IS_INDEX_ACTIVE && QUERY_REWRITE_INDEX.size() < INDEX_MAX_SIZE) {
            if (QUERY_REWRITE_INDEX.containsKey(key)) {
                result = QUERY_REWRITE_INDEX.get(key);
            } else {
                result = propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
                QUERY_REWRITE_INDEX.put(key, result);
            }
        } else {
            result = propertyFunction.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);
            if (IS_INDEX_ACTIVE) {
                LOGGER.warn("Query Rewrite Index Full: {}", INDEX_MAX_SIZE);
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
     * @param maxSize : use -1 for unlimited size
     */
    public static final void setMaxSize(Integer maxSize) {

        IS_INDEX_ACTIVE = !NO_INDEX.equals(maxSize);
        INDEX_MAX_SIZE = maxSize > UNLIMITED_INDEX ? maxSize : Integer.MAX_VALUE;

        if (IS_INDEX_ACTIVE) {
            QUERY_REWRITE_INDEX = new ExpiringMap<>(INDEX_TIMEOUT_SECONDS);
            QUERY_REWRITE_INDEX.getExpirer().startExpiringIfNotStarted();
        } else {
            QUERY_REWRITE_INDEX = null;
        }
    }

    /**
     * Sets the expiry time in seconds of the Query Rewrite Index.
     *
     * @param timeoutSeconds
     */
    public static final void setTimeoutSeconds(Integer timeoutSeconds) {
        INDEX_TIMEOUT_SECONDS = timeoutSeconds;
        if (IS_INDEX_ACTIVE) {
            QUERY_REWRITE_INDEX.setTimeToLive(timeoutSeconds);
        }
    }

}
