/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.registry;

import implementation.index.IndexUtils;
import java.io.File;
import java.lang.invoke.MethodHandles;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.HashedMap;
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
    private static final MultiKeyMap<MultiKey, MathTransform> MATH_TRANSFORM_REGISTRY = MultiKeyMap.multiKeyMap(new HashedMap<>());

    @SuppressWarnings("unchecked")
    public synchronized static final MathTransform getMathTransform(CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) throws FactoryException, MismatchedDimensionException, TransformException {

        MathTransform transform;
        MultiKey key = new MultiKey<>(sourceCRS, targetCRS);
        if (MATH_TRANSFORM_REGISTRY.containsKey(key)) {
            transform = MATH_TRANSFORM_REGISTRY.get(key);
        } else {
            transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
            MATH_TRANSFORM_REGISTRY.put(key, transform);
        }
        return transform;
    }

    public synchronized static final void write(File indexFile) {
        IndexUtils.write(indexFile, MATH_TRANSFORM_REGISTRY);
    }

    public synchronized static final void read(File indexFile) {
        MATH_TRANSFORM_REGISTRY.clear();
        IndexUtils.read(indexFile, MATH_TRANSFORM_REGISTRY);
    }

    public synchronized static final void clear() {
        MATH_TRANSFORM_REGISTRY.clear();
    }

}
