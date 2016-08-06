/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.expr;

import datatype.GmlDatatype;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprFunction2;
import org.apache.jena.sparql.expr.NodeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class EqualsExprFunc extends ExprFunction2 {

    private static Logger LOGGER = LoggerFactory.getLogger(EqualsExprFunc.class);

    private static final String symbol = "geof:sfEquals";

    public EqualsExprFunc(Expr expr1, Expr expr2) {
        super(expr1, expr2, symbol);
    }

    @Override
    public NodeValue eval(NodeValue x, NodeValue y) {
        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;

        Node node1 = x.asNode();
        Node node2 = y.asNode();

        Geometry g1 = (Geometry) gmlDataType.parse(node1.getLiteralLexicalForm());
        Geometry g2 = (Geometry) gmlDataType.parse(node2.getLiteralLexicalForm());

        boolean result = g1.equals(g2);

        if (result) {
            LOGGER.info("These 2 geometries are equal");
            return NodeValue.makeBoolean(true);
        } else {
            return NodeValue.makeBoolean(false);
        }
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new EqualsExprFunc(arg1, arg2);
    }

}
