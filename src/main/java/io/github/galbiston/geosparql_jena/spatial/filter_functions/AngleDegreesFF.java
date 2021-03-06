/*
 * Copyright 2019 .
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

import io.github.galbiston.geosparql_jena.implementation.great_circle.Angle;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase4;
import org.apache.jena.sparql.util.FmtUtils;

/**
 *
 *
 */
public class AngleDegreesFF extends FunctionBase4 {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3, NodeValue v4) {

        try {
            if (!v1.isNumber()) {
                throw new ExprEvalException("Not a Number: " + FmtUtils.stringForNode(v1.asNode()));
            }

            if (!v2.isNumber()) {
                throw new ExprEvalException("Not a Number: " + FmtUtils.stringForNode(v2.asNode()));
            }

            if (!v3.isNumber()) {
                throw new ExprEvalException("Not a Number: " + FmtUtils.stringForNode(v3.asNode()));
            }

            if (!v4.isNumber()) {
                throw new ExprEvalException("Not a Number: " + FmtUtils.stringForNode(v4.asNode()));
            }

            double x1 = v1.getDouble();
            double y1 = v2.getDouble();
            double x2 = v3.getDouble();
            double y2 = v4.getDouble();

            double radians = Angle.find(x1, y1, x2, y2);

            double degrees = Math.toDegrees(radians);
            return NodeValue.makeDouble(degrees);
        } catch (DatatypeFormatException ex) {
            throw new ExprEvalException(ex.getMessage(), ex);
        }
    }

}
