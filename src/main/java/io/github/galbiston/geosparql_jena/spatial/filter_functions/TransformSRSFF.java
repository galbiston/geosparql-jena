/*
 * Copyright 2019 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.util.FmtUtils;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class TransformSRSFF extends FunctionBase2 {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        try {

            if (!(v2.isIRI() || v2.isString())) {
                throw new ExprEvalException("Not a URI: " + FmtUtils.stringForNode(v2.asNode()));
            }

            String srsURI;
            if (v2.isIRI()) {
                srsURI = v2.asNode().getURI();
            } else {
                srsURI = v2.asString();
            }

            GeometryWrapper geometry = GeometryWrapper.extract(v1, GeometryLiteralIndex.GeometryIndex.PRIMARY);
            GeometryWrapper convertedGeom = geometry.transform(srsURI);

            return convertedGeom.asNodeValue();
        } catch (DatatypeFormatException | TransformException | FactoryException ex) {
            throw new ExprEvalException(ex.getMessage(), ex);
        }
    }
}
