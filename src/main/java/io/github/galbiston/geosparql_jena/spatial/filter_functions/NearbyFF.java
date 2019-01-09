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
package io.github.galbiston.geosparql_jena.spatial.filter_functions;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.index.GeometryLiteralIndex;
import java.lang.invoke.MethodHandles;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase4;
import org.apache.jena.sparql.util.FmtUtils;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class NearbyFF extends FunctionBase4 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3, NodeValue v4) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1, GeometryLiteralIndex.GeometryIndex.PRIMARY);
            GeometryWrapper geometry2 = GeometryWrapper.extract(v2, GeometryLiteralIndex.GeometryIndex.SECONDARY);

            if (!v3.isNumber()) {
                throw new ExprEvalException("Not a number: " + FmtUtils.stringForNode(v3.asNode()));
            }

            if (!(v4.isIRI() || v4.isString())) {
                throw new ExprEvalException("Not an IRI or String: " + FmtUtils.stringForNode(v4.asNode()));
            }

            double radius = v3.getDouble();

            String unitURI;
            if (v4.isIRI()) {
                unitURI = v4.asNode().getURI();
            } else {
                unitURI = v4.asString();
            }
            boolean result = relate(geometry1, geometry2, radius, unitURI);
            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException ex) {
            throw new ExprEvalException(ex.getMessage());
        }
    }

    public static final boolean relate(GeometryWrapper geometry1, GeometryWrapper geometry2, double radius, String unitsURI) {
        try {
            double distance = geometry1.distance(geometry2, unitsURI);
            return distance < radius;
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Exception: {}, {}, {}, {}, {}", geometry1.asLiteral(), geometry2.asLiteral(), radius, unitsURI, ex.getMessage());
            throw new ExprEvalException(ex.getMessage() + ": " + geometry1.asLiteral() + ", " + geometry2.asLiteral() + ", " + Double.toString(radius) + ", " + unitsURI);
        }
    }

}
