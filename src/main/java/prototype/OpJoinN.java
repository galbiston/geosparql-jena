/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.util.List;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpVisitor;
import org.apache.jena.sparql.algebra.Transform;
import org.apache.jena.sparql.algebra.op.OpN;
import org.apache.jena.sparql.util.NodeIsomorphismMap;

/**
 *
 * @author haozhechen
 */
public class OpJoinN extends OpN {

    private OpJoinN(List<Op> elts) {
        super(elts);
    }

    @Override
    public Op apply(Transform transform, List<Op> elts) {
        return null;
    }

    @Override
    public OpN copy(List<Op> elts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equalTo(Op other, NodeIsomorphismMap labelMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(OpVisitor opVisitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
