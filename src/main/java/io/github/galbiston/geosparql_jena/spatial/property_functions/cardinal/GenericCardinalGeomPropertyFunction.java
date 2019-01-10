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
import io.github.galbiston.geosparql_jena.spatial.property_functions.SpatialArguments;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public abstract class GenericCardinalGeomPropertyFunction extends GenericSpatialGeomPropertyFunction {

    @Override
    protected boolean testRelation(SpatialArguments spatialArguments, GeometryWrapper targetGeometryWrapper) {
        //Test Geometry against the Geometry from Object to see if it is a match.
        //Used when checking against bound Subjects.
        GeometryWrapper geometryWrapper = spatialArguments.getGeometryWrapper();
        try {
            return geometryWrapper.equals(targetGeometryWrapper);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            throw new ExprEvalException(ex.getMessage() + ": " + targetGeometryWrapper.asLiteral() + ", " + geometryWrapper.asLiteral(), ex);
        }
    }

}
