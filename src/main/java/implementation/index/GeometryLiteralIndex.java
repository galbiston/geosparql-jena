/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeometryWrapper;
import implementation.datatype.DatatypeReader;
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
                LOGGER.warn("Geometry Literal Index Full: {}", INDEX_MAX_SIZE);
            }
        }

        return geometryWrapper;
    }

    /**
     * Empty the Geometry Literal Index.
     */
    public static final void clear() {
        PRIMARY_INDEX.clear();
        SECONDARY_INDEX.clear();
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
            SECONDARY_INDEX = new ExpiringMap<>(INDEX_TIMEOUT_SECONDS);
        } else {
            PRIMARY_INDEX = null;
            SECONDARY_INDEX = null;
        }
    }

    /**
     * Sets the expiry time in seconds of the Geometry Literal Indexes.
     *
     * @param timeoutSeconds
     */
    public static final void setTimeoutSeconds(Integer timeoutSeconds) {
        INDEX_TIMEOUT_SECONDS = timeoutSeconds;
        if (IS_INDEX_ACTIVE) {
            PRIMARY_INDEX.setTimeToLive(timeoutSeconds);
            SECONDARY_INDEX.setTimeToLive(timeoutSeconds);
        }
    }

}
