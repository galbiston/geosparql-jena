/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeometryWrapper;
import implementation.datatype.DatatypeReader;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.collections4.map.LRUMap;

/**
 *
 *
 */
public class GeometryLiteralIndex {

    private static Map<String, GeometryWrapper> PRIMARY_INDEX = Collections.synchronizedMap(new LRUMap<>(IndexDefaultValues.GEOMETRY_LITERAL_INDEX_MAX_SIZE_DEFAULT));
    private static Map<String, GeometryWrapper> SECONDARY_INDEX = Collections.synchronizedMap(new LRUMap<>(IndexDefaultValues.GEOMETRY_LITERAL_INDEX_MAX_SIZE_DEFAULT));
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

    public synchronized static final void write(File indexFile) {
        IndexUtils.write(indexFile, PRIMARY_INDEX);
    }

    public synchronized static final void read(File indexFile) {
        PRIMARY_INDEX.clear();
        IndexUtils.read(indexFile, PRIMARY_INDEX);
    }

    /**
     * Empty the Geometry Literal Index.
     */
    public static final void clear() {
        PRIMARY_INDEX.clear();
        SECONDARY_INDEX.clear();
    }

    /**
     * Changes the max size of the Geometry Literal Index.
     * <br> The index will be empty after this process.
     *
     * @param maxSize
     */
    public static final void setMaxSize(Integer maxSize) {

        IS_INDEX_ACTIVE = maxSize != 0;

        Map<String, GeometryWrapper> newPrimaryIndex;
        Map<String, GeometryWrapper> newSecondaryIndex;
        if (IS_INDEX_ACTIVE) {
            newPrimaryIndex = Collections.synchronizedMap(new LRUMap<>(maxSize));
            newSecondaryIndex = Collections.synchronizedMap(new LRUMap<>(maxSize));
        } else {
            newPrimaryIndex = Collections.synchronizedMap(new LRUMap<>(IndexDefaultValues.INDEX_MINIMUM_SIZE));
            newSecondaryIndex = Collections.synchronizedMap(new LRUMap<>(IndexDefaultValues.INDEX_MINIMUM_SIZE));
        }
        PRIMARY_INDEX.clear();
        PRIMARY_INDEX = newPrimaryIndex;

        SECONDARY_INDEX.clear();
        SECONDARY_INDEX = newSecondaryIndex;
    }

}
