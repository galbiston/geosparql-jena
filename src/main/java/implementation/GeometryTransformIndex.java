/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Geometry;
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
import org.geotools.geometry.jts.JTS;
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
 * @author Gerg
 */
public class GeometryTransformIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final MultiKeyMap<MultiKey, MathTransform> MATH_TRANSFORM_REGISTRY = MultiKeyMap.multiKeyMap(new LRUMap<>(GeoSPARQLSupport.MATH_TRANSFORM_REGISTRY_MAX_SIZE));
    private static final MultiKeyMap<MultiKey, GeometryWrapper> GEOMETRY_TRANSFORM_INDEX = MultiKeyMap.multiKeyMap(new LRUMap<>(GeoSPARQLSupport.GEOMETRY_TRANSFORM_INDEX_MAX_SIZE));

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

    /**
     * Convert the geometry wrapper to the provided SRS URI.
     *
     * @param sourceGeometryWrapper
     * @param srsURI
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    @SuppressWarnings("unchecked")
    public static final GeometryWrapper transform(GeometryWrapper sourceGeometryWrapper, String srsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometryWrapper;
        MultiKey key = new MultiKey<>(sourceGeometryWrapper, srsURI);
        if (GEOMETRY_TRANSFORM_INDEX.containsKey(key)) {
            transformedGeometryWrapper = GEOMETRY_TRANSFORM_INDEX.get(key);
        } else {
            CoordinateReferenceSystem targetCRS = CRSRegistry.getCRS(srsURI);
            CoordinateReferenceSystem sourceCRS = sourceGeometryWrapper.getCRS();
            Geometry sourceGeometry = sourceGeometryWrapper.getParsingGeometry();  //Retrieve the original coordinate order according to the CRS.

            MathTransform transform = getMathTransform(sourceCRS, targetCRS);
            Geometry transformedGeometry = JTS.transform(sourceGeometry, transform);
            transformedGeometryWrapper = new GeometryWrapper(transformedGeometry, srsURI, sourceGeometryWrapper.getGeoSerialisation(), sourceGeometryWrapper.getDimensionInfo());
            GEOMETRY_TRANSFORM_INDEX.put(key, transformedGeometryWrapper);
        }
        return transformedGeometryWrapper;
    }

    public static final void writeGeometryTransformIndex(File geometryTransformIndexFile) {
        LOGGER.info("Writing Geometry Transform Index - {}: Started", geometryTransformIndexFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(geometryTransformIndexFile))) {
            objectOutputStream.writeObject(GEOMETRY_TRANSFORM_INDEX);
        } catch (IOException ex) {
            LOGGER.error("Store Geometry Literal Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Geometry Transform Index - {}: Completed", geometryTransformIndexFile);
    }

    public static final void readGeometryTransformIndex(File geometryTransformIndexFile) {
        LOGGER.info("Reading Geometry Transform Index - {}: Started", geometryTransformIndexFile);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(geometryTransformIndexFile))) {
            @SuppressWarnings("unchecked")
            MultiKeyMap<MultiKey, GeometryWrapper> geometryTransformIndex = (MultiKeyMap<MultiKey, GeometryWrapper>) objectInputStream.readObject();
            GEOMETRY_TRANSFORM_INDEX.putAll(geometryTransformIndex);
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Geometry Transform Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Reading Geometry Literal Index - {}: Completed", geometryTransformIndexFile);
    }

    public static final void writeMathTransformRegistry(File mathTransformRegistryFile) {
        LOGGER.info("Writing Math Transform Registry - {}: Started", mathTransformRegistryFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(mathTransformRegistryFile))) {
            objectOutputStream.writeObject(MATH_TRANSFORM_REGISTRY);
        } catch (IOException ex) {
            LOGGER.error("Store Geometry Literal Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Math Transform Registry - {}: Completed", mathTransformRegistryFile);
    }

    public static final void readMathTransformRegistry(File mathTransformRegistryFile) {
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

    public static final void clearAll() {
        MATH_TRANSFORM_REGISTRY.clear();
        GEOMETRY_TRANSFORM_INDEX.clear();
    }

}
