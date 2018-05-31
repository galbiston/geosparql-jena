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
import implementation.registry.CRSRegistry;
import implementation.registry.MathTransformRegistry;
import java.io.File;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Gerg
 */
public class GeometryTransformIndex {

    private static MultiKeyMap<MultiKey, GeometryWrapper> GEOMETRY_TRANSFORM_INDEX = MultiKeyMap.multiKeyMap(new LRUMap<>(IndexDefaultValues.GEOMETRY_TRANSFORM_INDEX_MAX_SIZE_DEFAULT));
    private static Boolean IS_INDEX_ACTIVE = true;

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
    @SuppressWarnings("unchecked")
    public static final GeometryWrapper transform(GeometryWrapper sourceGeometryWrapper, String srsURI, Boolean storeCRSTransform) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometryWrapper;
        MultiKey key = new MultiKey<>(sourceGeometryWrapper, srsURI);

        if (GEOMETRY_TRANSFORM_INDEX.containsKey(key)) {
            transformedGeometryWrapper = GEOMETRY_TRANSFORM_INDEX.get(key);
        } else {
            CoordinateReferenceSystem sourceCRS = sourceGeometryWrapper.getCRS();
            CoordinateReferenceSystem targetCRS = CRSRegistry.getCRS(srsURI);
            MathTransform transform = MathTransformRegistry.getMathTransform(sourceCRS, targetCRS);
            Geometry parsingGeometry = sourceGeometryWrapper.getParsingGeometry();
            Geometry transformedGeometry = JTS.transform(parsingGeometry, transform);

            GeoDatatypeEnum datatypeEnum = sourceGeometryWrapper.getGeoDatatypeEnum();
            DimensionInfo dimensionInfo = sourceGeometryWrapper.getDimensionInfo();
            transformedGeometryWrapper = new GeometryWrapper(transformedGeometry, srsURI, datatypeEnum, dimensionInfo);
            if (IS_INDEX_ACTIVE && storeCRSTransform) {
                GEOMETRY_TRANSFORM_INDEX.put(key, transformedGeometryWrapper);
            }
        }

        return transformedGeometryWrapper;
    }

    public static final void write(File indexFile) {
        IndexUtils.write(indexFile, GEOMETRY_TRANSFORM_INDEX);
    }

    public static final void read(File indexFile) {
        GEOMETRY_TRANSFORM_INDEX.clear();
        IndexUtils.read(indexFile, GEOMETRY_TRANSFORM_INDEX);
    }

    public static final void clear() {
        GEOMETRY_TRANSFORM_INDEX.clear();
    }

    /**
     * Changes the max size of the Geometry Transform Index.
     * <br> The index will be empty after this process.
     *
     * @param maxSize
     */
    public static final void setMaxSize(Integer maxSize) {

        IS_INDEX_ACTIVE = maxSize != 0;

        MultiKeyMap<MultiKey, GeometryWrapper> newGeometryTransformIndex;
        if (IS_INDEX_ACTIVE) {
            newGeometryTransformIndex = MultiKeyMap.multiKeyMap(new LRUMap<>(maxSize));
        } else {
            newGeometryTransformIndex = MultiKeyMap.multiKeyMap(new LRUMap<>(IndexDefaultValues.INDEX_MINIMUM_SIZE));
        }
        GEOMETRY_TRANSFORM_INDEX.clear();
        GEOMETRY_TRANSFORM_INDEX = newGeometryTransformIndex;
    }

}
