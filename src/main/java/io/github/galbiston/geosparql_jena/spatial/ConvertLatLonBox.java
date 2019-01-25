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
package io.github.galbiston.geosparql_jena.spatial;

import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import static io.github.galbiston.geosparql_jena.implementation.WKTLiteralFactory.reducePrecision;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.util.FmtUtils;

/**
 *
 *
 */
public class ConvertLatLonBox {

    private static final String PREFIX = "<" + SRS_URI.WGS84_CRS + "> POLYGON((";

    public static final String toWKT(double latMin, double lonMin, double latMax, double lonMax) {
        ConvertLatLon.checkBounds(latMin, lonMin);
        ConvertLatLon.checkBounds(latMax, lonMax);
        return PREFIX + reducePrecision(latMin) + " " + reducePrecision(lonMin) + ", " + reducePrecision(latMax) + " " + reducePrecision(lonMin) + ", " + reducePrecision(latMax) + " " + reducePrecision(lonMax) + ", " + reducePrecision(latMin) + " " + reducePrecision(lonMax) + ", " + reducePrecision(latMin) + " " + reducePrecision(lonMin) + "))";
    }

    public static final Literal toLiteral(double latMin, double lonMin, double latMax, double lonMax) {
        String wktPolygon = toWKT(latMin, lonMin, latMax, lonMax);
        return ResourceFactory.createTypedLiteral(wktPolygon, WKTDatatype.INSTANCE);
    }

    public static final NodeValue convert(NodeValue v1, NodeValue v2, NodeValue v3, NodeValue v4) {
        if (!v1.isNumber()) {
            throw new DatatypeFormatException("Not a number: " + FmtUtils.stringForNode(v1.asNode()));
        }

        if (!v2.isNumber()) {
            throw new DatatypeFormatException("Not a number: " + FmtUtils.stringForNode(v2.asNode()));
        }

        if (!v3.isNumber()) {
            throw new DatatypeFormatException("Not a number: " + FmtUtils.stringForNode(v3.asNode()));
        }

        if (!v4.isNumber()) {
            throw new DatatypeFormatException("Not a number: " + FmtUtils.stringForNode(v4.asNode()));
        }

        double latMin = v1.getDouble();
        double lonMin = v2.getDouble();
        double latMax = v3.getDouble();
        double lonMax = v4.getDouble();
        String wktPolygon = toWKT(latMin, lonMin, latMax, lonMax);

        return NodeValue.makeNode(wktPolygon, WKTDatatype.INSTANCE);
    }

    public static final Node convert(Node n1, Node n2, Node n3, Node n4) {
        NodeValue result = convert(NodeValue.makeNode(n1), NodeValue.makeNode(n2), NodeValue.makeNode(n3), NodeValue.makeNode(n4));
        return result.asNode();
    }
}
