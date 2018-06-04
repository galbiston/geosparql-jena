/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeometryWrapper;
import implementation.datatype.DatatypeReader;
import java.util.Map;
import org.apache.mina.util.ExpiringMap;

/**
 *
 *
 */
public class GeometryLiteralIndex {

    private static ExpiringMap<String, GeometryWrapper> PRIMARY_INDEX = new ExpiringMap<>();
    private static ExpiringMap<String, GeometryWrapper> SECONDARY_INDEX = new ExpiringMap<>();
    //private static Map<String, GeometryWrapper> PRIMARY_INDEX = Collections.synchronizedMap(new LRUMap<>(IndexDefaultValues.GEOMETRY_LITERAL_INDEX_MAX_SIZE_DEFAULT));
    //private static Map<String, GeometryWrapper> SECONDARY_INDEX = Collections.synchronizedMap(new LRUMap<>(IndexDefaultValues.GEOMETRY_LITERAL_INDEX_MAX_SIZE_DEFAULT));
    private static Boolean IS_INDEX_ACTIVE = true;

    public enum GeometryIndex {
        PRIMARY, SECONDARY
    }

    public static final GeometryWrapper retrieve(String geometryLiteral, DatatypeReader datatypeReader, GeometryIndex targetIndex) {
        GeometryWrapper geometryWrapper;

        switch (targetIndex) {
            case SECONDARY:
                geometryWrapper = retrieveMemoryIndex(geometryLiteral, datatypeReader, SECONDARY_INDEX);
                break;
            default:
                geometryWrapper = retrieveMemoryIndex(geometryLiteral, datatypeReader, PRIMARY_INDEX);
        }

        return geometryWrapper;
    }

    private static GeometryWrapper retrieveMemoryIndex(String geometryLiteral, DatatypeReader datatypeReader, Map<String, GeometryWrapper> index) {
        GeometryWrapper geometryWrapper;
        if (index.containsKey(geometryLiteral)) {
            geometryWrapper = index.get(geometryLiteral);
        } else {
            geometryWrapper = datatypeReader.read(geometryLiteral);
            if (IS_INDEX_ACTIVE) {
                index.put(geometryLiteral, geometryWrapper);
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
     * Sets whether the Geometry Literal Index is active.
     * <br> The index will be empty after this process.
     *
     * @param isActive
     */
    public static final void setActive(Boolean isActive) {

        IS_INDEX_ACTIVE = isActive;
        PRIMARY_INDEX.clear();

        PRIMARY_INDEX = new ExpiringMap<>();

        SECONDARY_INDEX.clear();
        //SECONDARY_INDEX = newSecondaryIndex;
        SECONDARY_INDEX = new ExpiringMap<>();
    }

}
