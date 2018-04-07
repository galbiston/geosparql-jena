/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Geometry;
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

/**
 *
 * @author Gerg
 */
public class GeometryTransformIndex {

    private static final MultiKeyMap<MultiKey, MathTransform> TRANSFORM_REGISTRY = MultiKeyMap.multiKeyMap(new LRUMap<>(20));
    private static final MultiKeyMap<MultiKey, GeometryWrapper> GEOMETRY_TRANSFORM_INDEX = MultiKeyMap.multiKeyMap(new LRUMap<>(1000));

    @SuppressWarnings("unchecked")
    public static final MathTransform getMathTransform(CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) throws FactoryException, MismatchedDimensionException, TransformException {

        MathTransform transform;
        MultiKey key = new MultiKey<>(sourceCRS, targetCRS);
        if (TRANSFORM_REGISTRY.containsKey(key)) {
            transform = TRANSFORM_REGISTRY.get(key);
        } else {
            transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
            TRANSFORM_REGISTRY.put(key, transform);
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

}
