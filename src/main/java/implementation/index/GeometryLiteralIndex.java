/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeoSPARQLSupport;
import implementation.GeometryWrapper;
import implementation.datatype.DatatypeReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gerg
 */
public class GeometryLiteralIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final LRUMap<String, GeometryWrapper> GEOMETRY_LITERAL_INDEX = new LRUMap<>(GeoSPARQLSupport.GEOMETRY_LITERAL_INDEX_MAX_SIZE);

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

    public static final void writeGeometryLiteralIndex(File geometryLiteralIndexFile) {

        LOGGER.info("Writing Geometry Literal Index - {}: Started", geometryLiteralIndexFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(geometryLiteralIndexFile))) {
            objectOutputStream.writeObject(GEOMETRY_LITERAL_INDEX);
        } catch (IOException ex) {
            LOGGER.error("Store Geometry Literal Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Geometry Literal Index - {}: Completed", geometryLiteralIndexFile);
    }

    public static final void readGeometryLiteralIndex(File geometryLiteralIndexFile) {
        LOGGER.info("Reading Geometry Literal Index - {}: Started", geometryLiteralIndexFile);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(geometryLiteralIndexFile))) {
            @SuppressWarnings("unchecked")
            LRUMap<String, GeometryWrapper> geometryLiteralIndex = (LRUMap<String, GeometryWrapper>) objectInputStream.readObject();
            GEOMETRY_LITERAL_INDEX.putAll(geometryLiteralIndex);
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Geometry Literal Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Reading Geometry Literal Index - {}: Completed", geometryLiteralIndexFile);
    }

    public static final void clearAll() {
        GEOMETRY_LITERAL_INDEX.clear();
        GEOMETRY_LITERAL_INDEX.clear();
    }

}