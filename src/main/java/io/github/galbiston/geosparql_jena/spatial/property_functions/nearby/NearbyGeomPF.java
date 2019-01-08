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
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.NearbyFF;
import io.github.galbiston.geosparql_jena.spatial.property_functions.GenericSpatialPropertyFunction;
import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.util.FmtUtils;
import org.locationtech.jts.geom.Envelope;

/**
 *
 *
 */
public class NearbyGeomPF extends GenericSpatialPropertyFunction {

    private static final int GEOM_POS = 0;
    private static final int RADIUS_POS = 1;
    private static final int UNITS_POS = 2;
    private static final int LIMIT_POS = 3;

    protected GeometryWrapper geometryWrapper;
    protected double radius;
    protected String unitsURI;
    protected Envelope envelope;

    @Override
    protected int extractArguments(Node subject, Node predicate, PropFuncArg object) {

        //Check minimum arguments.
        List<Node> objectArgs = object.getArgList();
        if (objectArgs.size() < 2) {
            throw new ExprEvalException(FmtUtils.stringForNode(predicate) + ": Minimum of 2 arguments.");
        } else if (objectArgs.size() > 4) {
            throw new ExprEvalException(FmtUtils.stringForNode(predicate) + ": Maximum of 4 arguments.");
        }
        Node geomLit = object.getArg(GEOM_POS);
        NodeValue radiusNode = NodeValue.makeNode(objectArgs.get(RADIUS_POS));

        if (!radiusNode.isDouble()) {
            throw new ExprEvalException("Not a xsd:double: " + FmtUtils.stringForNode(radiusNode.asNode()));
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
            unitsURI = NearbyPF.DEFAULT_UNITS;
        }

        //Subject is unbound so find the number to the limit.
        int limit;
        if (objectArgs.size() > LIMIT_POS) {
            NodeValue limitNode = NodeValue.makeNode(objectArgs.get(LIMIT_POS));
            if (!limitNode.isInteger()) {
                throw new ExprEvalException("Not an integer: " + FmtUtils.stringForNode(limitNode.asNode()));
            }
            limit = limitNode.getInteger().intValue();
        } else {
            limit = DEFAULT_LIMIT;
        }

        geometryWrapper = GeometryWrapper.extract(geomLit);
        if (geometryWrapper == null) {
            throw new ExprEvalException("Not a GeometryLiteral: " + FmtUtils.stringForNode(geomLit));
        }

        envelope = SearchEnvelope.build(geometryWrapper, radius, unitsURI);

        return limit;
    }

    @Override
    protected boolean testRelation(GeometryWrapper targetGeometryWrapper) {
        return NearbyFF.relate(geometryWrapper, targetGeometryWrapper, radius, unitsURI);
    }

    @Override
    protected List<Resource> testSearchEnvelope() {
        List<Resource> features = SpatialIndex.query(envelope);
        return features;
    }

}
