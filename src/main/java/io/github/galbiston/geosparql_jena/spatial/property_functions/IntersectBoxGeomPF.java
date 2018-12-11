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
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.util.FmtUtils;
import org.locationtech.jts.geom.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class IntersectBoxGeomPF extends GenericSpatialPropertyFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int GEOM_POS = 0;
    private static final int LIMIT_POS = 1;

    protected GeometryWrapper geometryWrapper;
    protected Envelope envelope;
    protected int limit;

    @Override
    public QueryIterator execEvaluated(Binding binding, Node subject, Node predicate, PropFuncArg object, ExecutionContext execCxt) {

        //Check minimum arguments.
        List<Node> objectArgs = object.getArgList();
        if (objectArgs.size() < 1) {
            throw new ExprEvalException(FmtUtils.stringForNode(predicate) + ": Minimum of 1 arguments.");
        } else if (objectArgs.size() > 2) {
            throw new ExprEvalException(FmtUtils.stringForNode(predicate) + ": Maximum of 2 arguments.");
        }
        Node geomLit = object.getArg(GEOM_POS);

        //Subject is unbound so find the number to the limit.
        if (objectArgs.size() > LIMIT_POS) {
            NodeValue limitNode = NodeValue.makeNode(objectArgs.get(LIMIT_POS));
            if (!limitNode.isInteger()) {
                throw new ExprEvalException("Not an integer: " + FmtUtils.stringForNode(limitNode.asNode()));
            }
            limit = limitNode.getInteger().intValue();
        } else {
            limit = NearbyPF.DEFAULT_LIMIT;
        }

        geometryWrapper = GeometryWrapper.extract(geomLit);
        if (geometryWrapper == null) {
            throw new ExprEvalException("Not a GeometryLiteral: " + FmtUtils.stringForNode(geomLit));
        }

        envelope = SearchEnvelope.build(geometryWrapper);

        return exec(binding, execCxt, subject, limit);
    }

    @Override
    protected boolean testRelation(GeometryWrapper targetGeometryWrapper) {

        try {
            return geometryWrapper.intersects(targetGeometryWrapper);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Exception: {}, {}, {}", targetGeometryWrapper.asLiteral(), geometryWrapper.asLiteral(), ex.getMessage());
            throw new ExprEvalException(ex.getMessage() + ": " + targetGeometryWrapper.asLiteral() + ", " + geometryWrapper.asLiteral());
        }
    }

    @Override
    protected Envelope getSearchEnvelope() {
        return envelope;
    }

}
