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
package io.github.galbiston.geosparql_jena.spatial.property_functions.nearby;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonFF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.SpatialArguments;
import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.util.FmtUtils;
import org.locationtech.jts.geom.Envelope;

/**
 *
 *
 */
public class NearbyPF extends NearbyGeomPF {

    private static final int LAT_POS = 0;
    private static final int LON_POS = 1;
    private static final int RADIUS_POS = 2;
    private static final int UNITS_POS = 3;
    private static final int LIMIT_POS = 4;
    public static final String DEFAULT_UNITS = Unit_URI.KILOMETRE_URL;

    @Override
    protected SpatialArguments extractObjectArguments(Node predicate, PropFuncArg object) {

        //Check minimum arguments.
        List<Node> objectArgs = object.getArgList();
        if (objectArgs.size() < 3) {
            throw new ExprEvalException(FmtUtils.stringForNode(predicate) + ": Minimum of 3 arguments.");
        } else if (objectArgs.size() > 5) {
            throw new ExprEvalException(FmtUtils.stringForNode(predicate) + ": Maximum of 5 arguments.");
        }

        Node lat = objectArgs.get(LAT_POS);
        Node lon = objectArgs.get(LON_POS);
        NodeValue radiusNode = NodeValue.makeNode(objectArgs.get(RADIUS_POS));

        //Check minimum arguments are all bound.
        if (lat.isVariable() || lon.isVariable() || !radiusNode.isDouble()) {
            throw new ExprEvalException("Arguments are not all concrete: " + FmtUtils.stringForNode(lat) + ", " + FmtUtils.stringForNode(lon) + ", " + FmtUtils.stringForNode(radiusNode.asNode()));
        }

        radius = radiusNode.getDouble();

        //Obtain optional arguments.
        if (objectArgs.size() > UNITS_POS) {
            Node unitsNode = objectArgs.get(UNITS_POS);
            if (!unitsNode.isURI()) {
                throw new ExprEvalException("Not a URI: " + FmtUtils.stringForNode(unitsNode));
            }
            unitsURI = unitsNode.getURI();
        } else {
            unitsURI = DEFAULT_UNITS;
        }

        //Subject is unbound so find the number to the limit.
        int limit;
        if (objectArgs.size() > LIMIT_POS) {
            NodeValue limitNode = NodeValue.makeNode(objectArgs.get(LIMIT_POS));
            if (!limitNode.isInteger()) {
                throw new ExprEvalException("Not an integer: " + FmtUtils.stringForNode(limitNode.getNode()));
            }
            limit = limitNode.getInteger().intValue();
        } else {
            limit = DEFAULT_LIMIT;
        }

        Node geometryNode = ConvertLatLonFF.convert(lat, lon);
        GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometryNode);

        Envelope envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        return new SpatialArguments(limit, geometryWrapper, envelope);
    }

}
