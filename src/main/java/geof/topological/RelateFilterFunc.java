/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological;

import com.vividsolutions.jts.geom.IntersectionMatrix;
import implementation.datatype.GeometryWrapper;
import org.apache.jena.datatypes.DatatypeFormatException;
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
 * @author haozhechen
 */
public class RelateFilterFunc extends FunctionBase3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelateFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
            GeometryWrapper geometry2 = GeometryWrapper.extract(v2);

            Node node3 = v3.asNode();
            String compreMatrix = node3.getLiteral().getLexicalForm();

            IntersectionMatrix matrix = geometry1.relate(geometry2);
            boolean result = matrix.matches(compreMatrix);

            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException | FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Filter Function Exception: {}", ex.getMessage());
            return NodeValue.FALSE;
        }

    }

}
