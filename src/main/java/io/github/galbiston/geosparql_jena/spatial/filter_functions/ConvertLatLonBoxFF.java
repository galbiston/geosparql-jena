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
import org.apache.jena.sparql.function.FunctionBase4;
import org.apache.jena.sparql.util.FmtUtils;

/**
 *
 *
 */
public class ConvertLatLonBoxFF extends FunctionBase4 {

    private static final String PREFIX = "<" + SRS_URI.WGS84_CRS + "> POLYGON(";

    public static final String toWKT(float latMin, float lonMin, float latMax, float lonMax) {
        return PREFIX + latMin + " " + lonMin + ", " + latMin + " " + lonMax + ", " + latMax + " " + lonMax + ", " + latMax + " " + lonMin + ", " + latMin + " " + lonMin + ")";
    }

    public static final Node convert(Node n1, Node n2, Node n3, Node n4) {
        ConvertLatLonBoxFF convertLatLonBoxFF = new ConvertLatLonBoxFF();
        NodeValue result = convertLatLonBoxFF.exec(NodeValue.makeNode(n1), NodeValue.makeNode(n2), NodeValue.makeNode(n3), NodeValue.makeNode(n4));
        return result.asNode();
    }

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3, NodeValue v4) {
        if (!v1.isNumber()) {
            throw new ExprEvalException("Not a number: " + FmtUtils.stringForNode(v1.asNode()));
        }

        if (!v2.isNumber()) {
            throw new ExprEvalException("Not a number: " + FmtUtils.stringForNode(v2.asNode()));
        }

        if (!v3.isNumber()) {
            throw new ExprEvalException("Not a number: " + FmtUtils.stringForNode(v3.asNode()));
        }

        if (!v4.isNumber()) {
            throw new ExprEvalException("Not a number: " + FmtUtils.stringForNode(v4.asNode()));
        }

        float latMin = v1.getFloat();
        float lonMin = v2.getFloat();
        float latMax = v3.getFloat();
        float lonMax = v4.getFloat();
        String wktPolygon = toWKT(latMin, lonMin, latMax, lonMax);

        return NodeValue.makeNode(wktPolygon, WKTDatatype.INSTANCE);
    }
}
