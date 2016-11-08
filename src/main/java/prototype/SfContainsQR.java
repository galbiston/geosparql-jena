/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import org.apache.jena.sparql.expr.Expr;
import queryrewrite.expr.sf.SFContainsExprFunc;
import geo.topological.BasePropertyFunction;

/**
 *
 * @author haozhechen
 */
public class SfContainsQR extends BasePropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new SFContainsExprFunc(expr1, expr2);
    }

}
