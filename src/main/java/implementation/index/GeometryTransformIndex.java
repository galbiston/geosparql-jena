/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import com.vividsolutions.jts.geom.Geometry;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.datatype.GeoDatatypeEnum;
import static implementation.index.IndexDefaultValues.NO_INDEX;
import static implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import implementation.registry.CRSRegistry;
import implementation.registry.MathTransformRegistry;
import java.lang.invoke.MethodHandles;
import org.apache.mina.util.ExpiringMap;
import org.geotools.geometry.jts.JTS;
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
public class GeometryTransformIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Boolean IS_INDEX_ACTIVE = true;
    private static Integer INDEX_MAX_SIZE = IndexDefaultValues.UNLIMITED_INDEX;
    private static Integer INDEX_TIMEOUT_SECONDS = IndexDefaultValues.INDEX_TIMEOUT_SECONDS;
    private static ExpiringMap<String, GeometryWrapper> GEOMETRY_TRANSFORM_INDEX = new ExpiringMap<>(IndexDefaultValues.INDEX_TIMEOUT_SECONDS);

    /**
     *
     * @param sourceGeometryWrapper
     * @param srsURI
     * @param storeCRSTransform
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public static final GeometryWrapper transform(GeometryWrapper sourceGeometryWrapper, String srsURI, Boolean storeCRSTransform) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometryWrapper;
        String key = sourceGeometryWrapper.getLexicalForm() + "@" + srsURI;

        if (IS_INDEX_ACTIVE && storeCRSTransform && GEOMETRY_TRANSFORM_INDEX.size() < INDEX_MAX_SIZE) {
            if (GEOMETRY_TRANSFORM_INDEX.containsKey(key)) {
                transformedGeometryWrapper = GEOMETRY_TRANSFORM_INDEX.get(key);
            } else {
                transformedGeometryWrapper = transform(sourceGeometryWrapper, srsURI);
                GEOMETRY_TRANSFORM_INDEX.put(key, transformedGeometryWrapper);
            }
        } else {
            transformedGeometryWrapper = transform(sourceGeometryWrapper, srsURI);
            if (IS_INDEX_ACTIVE && storeCRSTransform) {
                LOGGER.warn("Geometry Transform Index Full: {}", INDEX_MAX_SIZE);
            }
        }

        return transformedGeometryWrapper;
    }

    private static GeometryWrapper transform(GeometryWrapper sourceGeometryWrapper, String srsURI) throws FactoryException, MismatchedDimensionException, TransformException {
        CoordinateReferenceSystem sourceCRS = sourceGeometryWrapper.getCRS();
        CoordinateReferenceSystem targetCRS = CRSRegistry.getCRS(srsURI);
        MathTransform transform = MathTransformRegistry.getMathTransform(sourceCRS, targetCRS);
        Geometry parsingGeometry = sourceGeometryWrapper.getParsingGeometry();
        Geometry transformedGeometry = JTS.transform(parsingGeometry, transform);

        GeoDatatypeEnum datatypeEnum = sourceGeometryWrapper.getGeoDatatypeEnum();
        DimensionInfo dimensionInfo = sourceGeometryWrapper.getDimensionInfo();
        return new GeometryWrapper(transformedGeometry, srsURI, datatypeEnum, dimensionInfo);
    }

    public static final void clear() {
        if (GEOMETRY_TRANSFORM_INDEX != null) {
            GEOMETRY_TRANSFORM_INDEX.clear();
        }
    }

    /**
     * Sets whether the Geometry Transform Index is active.
     * <br> The index will be empty after this process.
     *
     * @param maxSize : use -1 for unlimited size
     */
    public static final void setMaxSize(Integer maxSize) {

        IS_INDEX_ACTIVE = !NO_INDEX.equals(maxSize);
        INDEX_MAX_SIZE = maxSize > UNLIMITED_INDEX ? maxSize : Integer.MAX_VALUE;

        if (IS_INDEX_ACTIVE) {
            GEOMETRY_TRANSFORM_INDEX = new ExpiringMap<>(INDEX_TIMEOUT_SECONDS);
            GEOMETRY_TRANSFORM_INDEX.getExpirer().startExpiringIfNotStarted();
        } else {
            GEOMETRY_TRANSFORM_INDEX = null;
        }
    }

    /**
     * Sets the expiry time in seconds of the Geometry Transform Index.
     *
     * @param timeoutSeconds : use 0 or negative for unlimited timeout
     */
    public static final void setTimeoutSeconds(Integer timeoutSeconds) {
        INDEX_TIMEOUT_SECONDS = timeoutSeconds;

        if (IS_INDEX_ACTIVE) {
            if (INDEX_TIMEOUT_SECONDS > 0) {
                GEOMETRY_TRANSFORM_INDEX.setTimeToLive(INDEX_TIMEOUT_SECONDS);
                GEOMETRY_TRANSFORM_INDEX.getExpirer().startExpiringIfNotStarted();
            } else {
                GEOMETRY_TRANSFORM_INDEX.getExpirer().stopExpiring();
            }
        }
    }

}
