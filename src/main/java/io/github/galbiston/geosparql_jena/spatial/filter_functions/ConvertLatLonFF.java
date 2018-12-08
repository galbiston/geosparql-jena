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

        if (!v1.isFloat()) {
            throw new ExprEvalException("Not a xsd:float: " + FmtUtils.stringForNode(v1.asNode()));
        }

        if (!v2.isFloat()) {
            throw new ExprEvalException("Not a xsd:float: " + FmtUtils.stringForNode(v2.asNode()));
        }

        float lat = v1.getFloat();
        float lon = v2.getFloat();
        String wktPoint = PREFIX + lat + " " + lon + ")";

        return NodeValue.makeNode(wktPoint, WKTDatatype.INSTANCE);
    }

}
