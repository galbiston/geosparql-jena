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
public class ConvertLatLon {

    private static final String PREFIX = "<" + SRS_URI.WGS84_CRS + "> POINT(";

    public static final String toWKT(double lat, double lon) {
        checkBounds(lat, lon);
        return PREFIX + reducePrecision(lat) + " " + reducePrecision(lon) + ")";
    }

    public static final Literal toLiteral(double lat, double lon) {
        String wktPoint = toWKT(lat, lon);
        return ResourceFactory.createTypedLiteral(wktPoint, WKTDatatype.INSTANCE);
    }

    public static final NodeValue convert(NodeValue latNodeValue, NodeValue lonNodeValue) {
        if (!latNodeValue.isNumber()) {
            throw new DatatypeFormatException("Not a number: " + FmtUtils.stringForNode(latNodeValue.asNode()));
        }

        if (!lonNodeValue.isNumber()) {
            throw new DatatypeFormatException("Not a number: " + FmtUtils.stringForNode(lonNodeValue.asNode()));
        }

        double lat = latNodeValue.getDouble();
        double lon = lonNodeValue.getDouble();
        String wktPoint = toWKT(lat, lon);

        return NodeValue.makeNode(wktPoint, WKTDatatype.INSTANCE);
    }

    public static final Node convert(Node latNode, Node lonNode) {
        NodeValue result = convert(NodeValue.makeNode(latNode), NodeValue.makeNode(lonNode));
        return result.asNode();
    }

    public static final void checkBounds(double latitude, double longitude) throws DatatypeFormatException {

        if (latitude < -90.0 || latitude > 90.0) {
            throw new DatatypeFormatException("Lat/Lon out of bounds: " + latitude + ", " + longitude);
        } else if (longitude < -180.0 || longitude > 180.0) {
            throw new DatatypeFormatException("Lat/Lon out of bounds: " + latitude + ", " + longitude);
        }
    }
}
