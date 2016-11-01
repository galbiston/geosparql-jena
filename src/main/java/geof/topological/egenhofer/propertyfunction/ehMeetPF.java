/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.egenhofer.propertyfunction;

import geof.topological.GenericPropertyFunction;
import geof.topological.egenhofer.expressionfunction.ehMeetEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class ehMeetPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new ehMeetEF(expr1, expr2);
    }

}
