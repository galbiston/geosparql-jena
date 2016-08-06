/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.nontopo;

import datatype.GmlDatatype;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Return type ogc:geomLiteral ogc:geomLiteral can be a string value like
 * "<gml:Point ...>...</gml:Point>"
 *
 * @author haozhechen
 */
public class BufferFilterFunc extends FunctionBase3 {

    private static Logger log = LoggerFactory.getLogger(BufferFilterFunc.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {

        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;

        //Transfer the parameters as Nodes
        Node node1 = v1.asNode();
        Node node2 = v2.asNode();
        //the units
        Node node3 = v3.asNode();

        Geometry g1 = (Geometry) gmlDataType.parse(node1.getLiteralLexicalForm());

        double radius = Double.parseDouble(node2.getLiteralLexicalForm());

        Geometry buffer = g1.buffer(radius);
        return NodeValue.makeNodeString(gmlDataType.unparse(buffer));
    }

}
