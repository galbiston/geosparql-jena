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

import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.util.FmtUtils;

/**
 *
 *
 */
public class ConvertLatLonFF extends FunctionBase2 {

    private static final String PREFIX = "<" + SRS_URI.WGS84_CRS + "> POINT(";

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        if (!v1.isNumber()) {
            throw new ExprEvalException("Not a number: " + FmtUtils.stringForNode(v1.asNode()));
        }

        if (!v2.isNumber()) {
            throw new ExprEvalException("Not a number: " + FmtUtils.stringForNode(v2.asNode()));
        }

        float lat = v1.getFloat();
        float lon = v2.getFloat();
        String wktPoint = toWKT(lat, lon);

        return NodeValue.makeNode(wktPoint, WKTDatatype.INSTANCE);
    }

    public static final String toWKT(float lat, float lon) {
        return PREFIX + lat + " " + lon + ")";
    }

    public static final Node convert(Node n1, Node n2) {
        ConvertLatLonFF convertLatLonFF = new ConvertLatLonFF();
        NodeValue result = convertLatLonFF.exec(NodeValue.makeNode(n1), NodeValue.makeNode(n2));
        return result.asNode();
    }
}
