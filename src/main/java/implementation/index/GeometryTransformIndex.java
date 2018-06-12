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
import static implementation.index.IndexDefaultValues.INDEX_EXPIRY_INTERVAL;
import static implementation.index.IndexDefaultValues.NO_INDEX;
import static implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import implementation.index.expiring.ExpiringIndex;
import implementation.registry.CRSRegistry;
import implementation.registry.MathTransformRegistry;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 *
 *
 */
public class GeometryTransformIndex {

    private static Boolean IS_INDEX_ACTIVE = true;
    private static final String GEOMETRY_TRANSFORM_LABEL = "Geometry Transform";
    private static ExpiringIndex<String, GeometryWrapper> GEOMETRY_TRANSFORM_INDEX = new ExpiringIndex<>(UNLIMITED_INDEX, INDEX_EXPIRY_INTERVAL, GEOMETRY_TRANSFORM_LABEL);
    public static Long RETRIEVAL_COUNT = 0L;

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
        RETRIEVAL_COUNT++;

        if (IS_INDEX_ACTIVE && storeCRSTransform) {

            if (GEOMETRY_TRANSFORM_INDEX.containsKey(key)) {
                transformedGeometryWrapper = GEOMETRY_TRANSFORM_INDEX.get(key);
            } else {
                transformedGeometryWrapper = transform(sourceGeometryWrapper, srsURI);
                GEOMETRY_TRANSFORM_INDEX.put(key, transformedGeometryWrapper);
            }
        } else {
            transformedGeometryWrapper = transform(sourceGeometryWrapper, srsURI);
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
        RETRIEVAL_COUNT = 0L;
    }

    /**
     * Sets whether the Geometry Transform Index is active.
     * <br> The index will be empty after this process.
     *
     * @param maxSize : use -1 for unlimited size
     */
    public static final void setMaxSize(int maxSize) {
        IS_INDEX_ACTIVE = NO_INDEX != maxSize;

        if (IS_INDEX_ACTIVE) {
            if (GEOMETRY_TRANSFORM_INDEX != null) {
                GEOMETRY_TRANSFORM_INDEX.stopExpiry();
            }
            GEOMETRY_TRANSFORM_INDEX = new ExpiringIndex<>(maxSize, INDEX_EXPIRY_INTERVAL, GEOMETRY_TRANSFORM_LABEL);
            GEOMETRY_TRANSFORM_INDEX.startExpiry();
        } else {
            if (GEOMETRY_TRANSFORM_INDEX != null) {
                GEOMETRY_TRANSFORM_INDEX.stopExpiry();
            }
            GEOMETRY_TRANSFORM_INDEX = null;
        }
    }

    /**
     * Sets the expiry time in milliseconds of the Geometry Transform Index, if
     * active.
     *
     * @param expiryInterval : use 0 or negative for unlimited timeout
     */
    public static final void setExpiry(long expiryInterval) {

        if (IS_INDEX_ACTIVE) {
            if (expiryInterval > 0) {
                GEOMETRY_TRANSFORM_INDEX.stopExpiry();
                GEOMETRY_TRANSFORM_INDEX.setExpiryInterval(expiryInterval);
                GEOMETRY_TRANSFORM_INDEX.startExpiry();
            } else {
                GEOMETRY_TRANSFORM_INDEX.stopExpiry();
            }
        }
    }

    public static final Integer getGeometryTransformIndexSize() {
        if (GEOMETRY_TRANSFORM_INDEX != null) {
            return GEOMETRY_TRANSFORM_INDEX.size();
        } else {
            return 0;
        }
    }

    public static final Long getRetrievalCount() {
        return RETRIEVAL_COUNT;
    }
}
