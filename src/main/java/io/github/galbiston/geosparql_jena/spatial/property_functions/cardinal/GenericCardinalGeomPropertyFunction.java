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
package io.github.galbiston.geosparql_jena.spatial.property_functions.cardinal;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.spatial.property_functions.GenericSpatialGeomPropertyFunction;
import java.lang.invoke.MethodHandles;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public abstract class GenericCardinalGeomPropertyFunction extends GenericSpatialGeomPropertyFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected GeometryWrapper originalSearchEnvelope;

    @Override
    protected boolean testRelation(GeometryWrapper geometryWrapper, GeometryWrapper targetGeometryWrapper) {
        //Test against the target against the search envelope in the original SRS.
        try {
            return originalSearchEnvelope.within(targetGeometryWrapper);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Exception: {}, {}, {}", targetGeometryWrapper.asLiteral(), originalSearchEnvelope.asLiteral(), ex.getMessage());
            throw new ExprEvalException(ex.getMessage() + ": " + targetGeometryWrapper.asLiteral() + ", " + originalSearchEnvelope.asLiteral());
        }
    }

    @Override
    public QueryIterator search(Binding binding, ExecutionContext execCxt, Node subject, int limit) {
        GeometryWrapper geometryWrapper = getGeometryWrapper();
        originalSearchEnvelope = geometryWrapper.envelope();
        return super.search(binding, execCxt, subject, limit);
    }

}
