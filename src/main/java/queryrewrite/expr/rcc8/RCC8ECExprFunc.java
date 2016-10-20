/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.expr.rcc8;

import com.vividsolutions.jts.geom.Geometry;
import datatype.GeneralDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
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
public class RCC8ECExprFunc extends ExprFunction2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(RCC8ECExprFunc.class);

    public RCC8ECExprFunc(Expr expr1, Expr expr2) {
        super(expr1, expr2, Vocabulary.RCC_EXTCONNECTED_SYMBOL);
    }

    @Override
    public NodeValue eval(NodeValue x, NodeValue y) {

        GeneralDatatype generalDatatype = new GeneralDatatype();

        Node node1 = x.asNode();
        Node node2 = y.asNode();

        try {
            Geometry g1 = (Geometry) generalDatatype.parse(node1.getLiteralLexicalForm());
            Geometry g2 = (Geometry) generalDatatype.parse(node2.getLiteralLexicalForm());

            // Use DE-9IM to model the relationship
            // Use JTS's relate function to implement
            boolean result = g1.relate(g2, "FFTFTTTTT");

            return NodeValue.makeBoolean(result);
        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);
            return NodeValue.FALSE;
        }
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new RCC8ECExprFunc(arg1, arg2);
    }

}
