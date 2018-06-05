/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeometryWrapper;
import implementation.datatype.DatatypeReader;
import static implementation.index.IndexDefaultValues.FULL_INDEX_WARNING_INTERVAL;
import static implementation.index.IndexDefaultValues.NO_INDEX;
import static implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import org.apache.mina.util.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GeometryLiteralIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Boolean IS_INDEX_ACTIVE = true;
    private static Integer INDEX_MAX_SIZE = IndexDefaultValues.UNLIMITED_INDEX;
    private static Integer INDEX_TIMEOUT_SECONDS = IndexDefaultValues.INDEX_TIMEOUT_SECONDS;
    private static ExpiringMap<String, GeometryWrapper> PRIMARY_INDEX = new ExpiringMap<>(IndexDefaultValues.INDEX_TIMEOUT_SECONDS);
    private static ExpiringMap<String, GeometryWrapper> SECONDARY_INDEX = new ExpiringMap<>(IndexDefaultValues.INDEX_TIMEOUT_SECONDS);
    private static long FULL_INDEX_WARNING = System.currentTimeMillis();

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

        if (IS_INDEX_ACTIVE && index.size() < INDEX_MAX_SIZE) {
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
        } else {
            geometryWrapper = datatypeReader.read(geometryLiteral);
            if (IS_INDEX_ACTIVE) {
                long currentSystemTime = System.currentTimeMillis();
                if (FULL_INDEX_WARNING - currentSystemTime > FULL_INDEX_WARNING_INTERVAL) {
                    FULL_INDEX_WARNING = currentSystemTime;
                    LOGGER.warn("Geometry Literal Index Full: {} - Warning suppressed for {}ms", INDEX_MAX_SIZE, FULL_INDEX_WARNING_INTERVAL);
                }
            }
        }

        return geometryWrapper;
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
    }

    /**
     * Sets whether the Geometry Literal Indexes is active.
     * <br> The index will be empty after this process.
     *
     * @param maxSize : use -1 for unlimited size
     */
    public static final void setMaxSize(Integer maxSize) {

        IS_INDEX_ACTIVE = !NO_INDEX.equals(maxSize);
        INDEX_MAX_SIZE = maxSize > UNLIMITED_INDEX ? maxSize : Integer.MAX_VALUE;

        if (IS_INDEX_ACTIVE) {
            PRIMARY_INDEX = new ExpiringMap<>(INDEX_TIMEOUT_SECONDS);
            PRIMARY_INDEX.getExpirer().startExpiringIfNotStarted();
            SECONDARY_INDEX = new ExpiringMap<>(INDEX_TIMEOUT_SECONDS);
            SECONDARY_INDEX.getExpirer().startExpiringIfNotStarted();
        } else {
            PRIMARY_INDEX = null;
            SECONDARY_INDEX = null;
        }
    }

    /**
     * Sets the expiry time in seconds of the Geometry Literal Indexes.
     *
     * @param timeoutSeconds : use 0 or negative for unlimited timeout
     */
    public static final void setTimeoutSeconds(Integer timeoutSeconds) {
        INDEX_TIMEOUT_SECONDS = timeoutSeconds;

        if (IS_INDEX_ACTIVE) {
            if (INDEX_TIMEOUT_SECONDS > 0) {
                PRIMARY_INDEX.setTimeToLive(INDEX_TIMEOUT_SECONDS);
                PRIMARY_INDEX.getExpirer().startExpiringIfNotStarted();
                SECONDARY_INDEX.setTimeToLive(INDEX_TIMEOUT_SECONDS);
                SECONDARY_INDEX.getExpirer().startExpiringIfNotStarted();
            } else {
                PRIMARY_INDEX.getExpirer().stopExpiring();
                SECONDARY_INDEX.getExpirer().stopExpiring();
            }
        }
    }

}
