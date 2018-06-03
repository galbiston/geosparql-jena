/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological;

import com.vividsolutions.jts.geom.IntersectionMatrix;
import implementation.GeometryWrapper;
import implementation.index.GeometryLiteralIndex.GeometryIndex;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 */
public class RelateFF extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelateFF.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1, GeometryIndex.PRIMARY);
            if (geometry1 == null) {
                return NodeValue.FALSE;
            }

            GeometryWrapper geometry2 = GeometryWrapper.extract(v2, GeometryIndex.SECONDARY);
            if (geometry2 == null) {
                return NodeValue.FALSE;
            }

            Node node3 = v3.asNode();
            if (!node3.isLiteral()) {
                return NodeValue.FALSE;
            }

            String compareMatrix = node3.getLiteral().getLexicalForm();

            IntersectionMatrix matrix = geometry1.relate(geometry2);
            boolean result = matrix.matches(compareMatrix);

            return NodeValue.makeBoolean(result);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Filter Function Exception: {}", ex.getMessage());
            return NodeValue.FALSE;
        }

    }

}
