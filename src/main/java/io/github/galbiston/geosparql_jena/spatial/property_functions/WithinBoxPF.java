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
package io.github.galbiston.geosparql_jena.spatial.property_functions;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonBoxFF;
import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.util.FmtUtils;

/**
 *
 *
 */
public class WithinBoxPF extends WithinBoxGeomPF {

    private static final int LAT_MIN_POS = 0;
    private static final int LON_MIN_POS = 1;
    private static final int LAT_MAX_POS = 2;
    private static final int LON_MAX_POS = 3;
    private static final int LIMIT_POS = 4;
    public static final int DEFAULT_LIMIT = -1;

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, PropFuncArg object, ExecutionContext execCxt) {

        //Check minimum arguments.
        List<Node> objectArgs = object.getArgList();
        if (objectArgs.size() < 4) {
            throw new ExprEvalException(FmtUtils.stringForNode(predicate) + ": Minimum of 4 arguments.");
        } else if (objectArgs.size() > 5) {
            throw new ExprEvalException(FmtUtils.stringForNode(predicate) + ": Maximum of 5 arguments.");
        }

        Node latMin = objectArgs.get(LAT_MIN_POS);
        Node lonMin = objectArgs.get(LON_MIN_POS);
        Node latMax = objectArgs.get(LAT_MAX_POS);
        Node lonMax = objectArgs.get(LON_MAX_POS);

        //Check minimum arguments are all bound.
        if (latMin.isVariable() || lonMin.isVariable() || latMax.isVariable() || lonMax.isVariable()) {
            throw new ExprEvalException("Arguments are not all concrete: " + FmtUtils.stringForNode(latMin) + ", " + FmtUtils.stringForNode(lonMin) + FmtUtils.stringForNode(latMax) + ", " + FmtUtils.stringForNode(lonMax));
        }

        //Subject is unbound so find the number to the limit.
        if (objectArgs.size() > LIMIT_POS) {
            NodeValue limitNode = NodeValue.makeNode(objectArgs.get(LIMIT_POS));
            if (!limitNode.isInteger()) {
                throw new ExprEvalException("Not an integer: " + FmtUtils.stringForNode(limitNode.getNode()));
            }
            limit = limitNode.getInteger().intValue();
        } else {
            limit = DEFAULT_LIMIT;
        }

        Node geometryNode = ConvertLatLonBoxFF.convert(latMin, lonMin, latMax, lonMax);
        geometryWrapper = GeometryWrapper.extract(geometryNode);

        envelope = SearchEnvelope.build(geometryWrapper);

        return exec(binding, execCxt, subject, limit);
    }

}
