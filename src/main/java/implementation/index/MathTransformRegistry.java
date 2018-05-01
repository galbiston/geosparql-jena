/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeoSPARQLSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class MathTransformRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static MultiKeyMap<MultiKey, MathTransform> MATH_TRANSFORM_REGISTRY = MultiKeyMap.multiKeyMap(new LRUMap<>(GeoSPARQLSupport.MATH_TRANSFORM_REGISTRY_MAX_SIZE));

    @SuppressWarnings("unchecked")
    public static final MathTransform getMathTransform(CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) throws FactoryException, MismatchedDimensionException, TransformException {

        MathTransform transform;
        MultiKey key = new MultiKey<>(sourceCRS, targetCRS);
        if (MATH_TRANSFORM_REGISTRY.containsKey(key)) {
            transform = MATH_TRANSFORM_REGISTRY.get(key);
        } else {
            transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
            MATH_TRANSFORM_REGISTRY.put(key, transform);
        }
        return transform;
    }

    public static final void write(File mathTransformRegistryFile) {
        LOGGER.info("Writing Math Transform Registry - {}: Started", mathTransformRegistryFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(mathTransformRegistryFile))) {
            objectOutputStream.writeObject(MATH_TRANSFORM_REGISTRY);
        } catch (IOException ex) {
            LOGGER.error("Store Geometry Literal Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Math Transform Registry - {}: Completed", mathTransformRegistryFile);
    }

    public static final void read(File mathTransformRegistryFile) {
        LOGGER.info("Reading Math Transform Registry - {}: Started", mathTransformRegistryFile);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(mathTransformRegistryFile))) {
            @SuppressWarnings("unchecked")
            MultiKeyMap<MultiKey, MathTransform> mathTransformRegistry = (MultiKeyMap<MultiKey, MathTransform>) objectInputStream.readObject();
            MATH_TRANSFORM_REGISTRY.putAll(mathTransformRegistry);
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Math Transform Registry exception: {}", ex.getMessage());
        }
        LOGGER.info("Reading Math Transform Registry - {}: Completed", mathTransformRegistryFile);
    }

    public static final void clear() {
        MATH_TRANSFORM_REGISTRY.clear();
    }

    /**
     * Changes the max size of the Math Transform Registry.
     * <br> The registry will be empty after this process.
     *
     * @param maxSize
     */
    public static final void setRegistryMaxSize(Integer maxSize) {

        MultiKeyMap<MultiKey, MathTransform> newMathRegistry = MultiKeyMap.multiKeyMap(new LRUMap<>(maxSize));
        MATH_TRANSFORM_REGISTRY.clear();
        MATH_TRANSFORM_REGISTRY = newMathRegistry;
    }

}
