/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological;

import geof.topological.GenericFilterFunction;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprFunction2;
import org.apache.jena.sparql.expr.NodeValue;

/**
 *
 *
 */
public class GenericExpressionFunction extends ExprFunction2 {

    private final String symbol;
    private final GenericFilterFunction filterFunction;

    public GenericExpressionFunction(Expr expr1, Expr expr2, String symbol, GenericFilterFunction filterFunction) {
        super(expr1, expr2, symbol);
        this.symbol = symbol;
        this.filterFunction = filterFunction;
    }

    @Override
    public NodeValue eval(NodeValue v1, NodeValue v2) {
        return filterFunction.exec(v1, v2);
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new GenericExpressionFunction(arg1, arg2, symbol, filterFunction);
    }

}
