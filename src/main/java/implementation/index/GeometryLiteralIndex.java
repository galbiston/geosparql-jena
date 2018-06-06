/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeometryWrapper;
import implementation.datatype.DatatypeReader;
import static implementation.index.IndexDefaultValues.INDEX_EXPIRY_INTERVAL;
import static implementation.index.IndexDefaultValues.NO_INDEX;
import static implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import implementation.index.expiring.ExpiringIndex;
import java.util.Map;

/**
 *
 *
 */
public class GeometryLiteralIndex {

    private static Boolean IS_INDEX_ACTIVE = true;
    private static final String PRIMARY_INDEX_LABEL = "Primary Geometry Literal";
    private static final String SECONDARY_INDEX_LABEL = "Secondary Geometry Literal";
    private static ExpiringIndex<String, GeometryWrapper> PRIMARY_INDEX = new ExpiringIndex<>(UNLIMITED_INDEX, INDEX_EXPIRY_INTERVAL, PRIMARY_INDEX_LABEL);
    private static ExpiringIndex<String, GeometryWrapper> SECONDARY_INDEX = new ExpiringIndex<>(UNLIMITED_INDEX, INDEX_EXPIRY_INTERVAL, SECONDARY_INDEX_LABEL);

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

        if (IS_INDEX_ACTIVE) {
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
            if (expiryInterval > 0) {
                PRIMARY_INDEX.stopExpiry();
                PRIMARY_INDEX.setExpiryInterval(expiryInterval);
                PRIMARY_INDEX.startExpiry();
                PRIMARY_INDEX.stopExpiry();
                SECONDARY_INDEX.setExpiryInterval(expiryInterval);
                SECONDARY_INDEX.startExpiry();
            } else {
                PRIMARY_INDEX.stopExpiry();
                SECONDARY_INDEX.stopExpiry();
            }
        }
    }

}
