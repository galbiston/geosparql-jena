/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.datatype.DatatypeReader;
import org.apache.commons.collections4.map.LRUMap;

/**
 *
 * @author Gerg
 */
public class GeometryLiteralIndex {

    private static final LRUMap<String, GeometryWrapper> GEOMETRY_LITERAL_INDEX = new LRUMap(10000);

    public static final GeometryWrapper retrieve(String geometryLiteral, DatatypeReader datatypeReader) {
        GeometryWrapper geometryWrapper;
        if (GEOMETRY_LITERAL_INDEX.containsKey(geometryLiteral)) {
            geometryWrapper = GEOMETRY_LITERAL_INDEX.get(geometryLiteral);
        } else {
            geometryWrapper = datatypeReader.read(geometryLiteral);
            GEOMETRY_LITERAL_INDEX.put(geometryLiteral, geometryWrapper);
        }

        return geometryWrapper;
    }

}
