/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.TransformBase;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.algebra.op.OpJoin;
import org.apache.jena.sparql.algebra.op.OpNull;

/**
 *
 * @author haozhechen
 */
public class QueryCleaner extends TransformBase {

    @Override
    public Op transform(OpFilter opFilter, Op subOp) {
        System.out.println("Filter Op: " + opFilter.toString());
        System.out.println("Sub Op: " + subOp.toString());

        return OpJoin.create(opFilter, OpNull.create());
    }

}
