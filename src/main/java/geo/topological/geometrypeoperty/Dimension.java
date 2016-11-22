/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.geometrypeoperty;

import geo.topological.GeneticGeometryPropertyFunction;
import geof.topological.geometryproperty.DimensionEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 */
public class Dimension extends GeneticGeometryPropertyFunction {

    @Override
    protected Expr propFunc(Expr expr) {
        return new DimensionEF(expr);
    }

}
