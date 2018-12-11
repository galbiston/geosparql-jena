/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.spatial.property_functions.box;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.property_functions.GenericSpatialBoxPropertyFunction;
import java.lang.invoke.MethodHandles;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.locationtech.jts.geom.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class IntersectBoxPF extends GenericSpatialBoxPropertyFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    protected boolean testRelation(GeometryWrapper targetGeometryWrapper) {

        try {
            return geometryWrapper.intersects(targetGeometryWrapper);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Exception: {}, {}, {}", targetGeometryWrapper.asLiteral(), geometryWrapper.asLiteral(), ex.getMessage());
            throw new ExprEvalException(ex.getMessage() + ": " + targetGeometryWrapper.asLiteral() + ", " + geometryWrapper.asLiteral());
        }
    }

    @Override
    protected Envelope buildSearchEnvelope(GeometryWrapper geometryWrapper) {
        return SearchEnvelope.build(geometryWrapper);
    }
}
