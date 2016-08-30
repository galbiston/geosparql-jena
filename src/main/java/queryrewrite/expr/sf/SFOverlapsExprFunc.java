/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.expr.sf;

import com.vividsolutions.jts.geom.Geometry;
import datatype.WktDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprFunction2;
import org.apache.jena.sparql.expr.NodeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class SFOverlapsExprFunc extends ExprFunction2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(SFOverlapsExprFunc.class);

    public SFOverlapsExprFunc(Expr expr1, Expr expr2) {
        super(expr1, expr2, Vocabulary.SF_OVERLAPS_SYMBOL);
    }

    @Override
    public NodeValue eval(NodeValue x, NodeValue y) {
        RDFDatatype wktDataType = WktDatatype.theWktDatatype;

        Node node1 = x.asNode();
        Node node2 = y.asNode();

        try {
            Geometry g1 = (Geometry) wktDataType.parse(node1.getLiteralLexicalForm());
            Geometry g2 = (Geometry) wktDataType.parse(node2.getLiteralLexicalForm());

            boolean result = g1.overlaps(g2);

            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);
            return NodeValue.FALSE;
        }
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new SFOverlapsExprFunc(arg1, arg2);
    }

}
