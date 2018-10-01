/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.implementation.index;

import io.github.galbiston.geosparql_jena.implementation.DimensionInfo;
import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.jts.GeometryTransformation;
import io.github.galbiston.geosparql_jena.implementation.registry.CRSRegistry;
import io.github.galbiston.geosparql_jena.implementation.registry.MathTransformRegistry;
import io.github.galbiston.expiring_index.ExpiringIndex;
import static io.github.galbiston.expiring_index.IndexDefaultValues.INDEX_EXPIRY_INTERVAL;
import static io.github.galbiston.expiring_index.IndexDefaultValues.NO_INDEX;
import static io.github.galbiston.expiring_index.IndexDefaultValues.UNLIMITED_EXPIRY;
import static io.github.galbiston.expiring_index.IndexDefaultValues.UNLIMITED_INDEX;
import org.locationtech.jts.geom.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class GeometryTransformIndex {

    private static Boolean IS_INDEX_ACTIVE = true;
    private static final String GEOMETRY_TRANSFORM_LABEL = "Geometry Transform";
    private static ExpiringIndex<String, GeometryWrapper> GEOMETRY_TRANSFORM_INDEX = new ExpiringIndex<>(GEOMETRY_TRANSFORM_LABEL, UNLIMITED_INDEX, INDEX_EXPIRY_INTERVAL);
    public static Long RETRIEVAL_COUNT = 0L;

    /**
     *
     * @param sourceGeometryWrapper
     * @param srsURI
     * @param storeCRSTransform
     * @return
     * @throws TransformException
     * @throws org.opengis.util.FactoryException
     */
    public static final GeometryWrapper transform(GeometryWrapper sourceGeometryWrapper, String srsURI, Boolean storeCRSTransform) throws TransformException, FactoryException {

        GeometryWrapper transformedGeometryWrapper;
        String key = sourceGeometryWrapper.getLexicalForm() + "@" + srsURI;
        RETRIEVAL_COUNT++;

        if (IS_INDEX_ACTIVE && storeCRSTransform) {
            try {
                if (GEOMETRY_TRANSFORM_INDEX.containsKey(key)) {

                    transformedGeometryWrapper = GEOMETRY_TRANSFORM_INDEX.get(key);

                } else {
                    transformedGeometryWrapper = transform(sourceGeometryWrapper, srsURI);
                    GEOMETRY_TRANSFORM_INDEX.put(key, transformedGeometryWrapper);
                }
                return transformedGeometryWrapper;
            } catch (NullPointerException ex) {
                //Catch NullPointerException and fall through to default action.
            }
        }
        return transform(sourceGeometryWrapper, srsURI);

    }

    private static GeometryWrapper transform(GeometryWrapper sourceGeometryWrapper, String srsURI) throws MismatchedDimensionException, FactoryException, TransformException {
        CoordinateReferenceSystem sourceCRS = sourceGeometryWrapper.getCRS();
        CoordinateReferenceSystem targetCRS = CRSRegistry.getCRS(srsURI);
        MathTransform transform = MathTransformRegistry.getMathTransform(sourceCRS, targetCRS);
        Geometry parsingGeometry = sourceGeometryWrapper.getParsingGeometry();

        //Transform the coordinates into a new Geometry.
        Geometry transformedGeometry = GeometryTransformation.transform(parsingGeometry, transform);

        //Construct a new GeometryWrapper using info from original GeometryWrapper.
        String geometryDatatypeURI = sourceGeometryWrapper.getGeometryDatatypeURI();
        DimensionInfo dimensionInfo = sourceGeometryWrapper.getDimensionInfo();
        return new GeometryWrapper(transformedGeometry, srsURI, geometryDatatypeURI, dimensionInfo);
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
            GEOMETRY_TRANSFORM_INDEX = new ExpiringIndex<>(GEOMETRY_TRANSFORM_LABEL, maxSize, INDEX_EXPIRY_INTERVAL);
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
            if (expiryInterval > UNLIMITED_EXPIRY) {
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
