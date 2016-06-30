/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof;

import java.util.List ;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node ;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.query.QueryBuildException ;
import org.apache.jena.query.spatial.SpatialIndexException;
import org.apache.jena.query.spatial.SpatialQuery;
import org.apache.jena.query.spatial.SpatialValueUtil;
import org.apache.jena.query.spatial.pfunction.SpatialMatch;
import org.apache.jena.query.spatial.pfunction.SpatialOperationPFBase;
import org.apache.jena.sparql.engine.ExecutionContext ;
import org.apache.jena.sparql.pfunction.PropFuncArg ;
import org.apache.jena.sparql.util.NodeFactoryExtra;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author haozhechen
 */
public class distance extends SpatialOperationPFBase{
    
    private static Logger log = LoggerFactory.getLogger(distance.class);
    
    public distance() {
    }
    
    @Override
    public void build(PropFuncArg argSubject, Node predicate,
                    PropFuncArg argObject, ExecutionContext execCxt) {
            super.build(argSubject, predicate, argObject, execCxt);

            if (!argSubject.isNode())
                    throw new QueryBuildException("Subject is not a single node: "
                                    + argSubject);

            if (argObject.isList()) {
                    List<Node> list = argObject.getArgList();
                    if (list.size() < 2)
                            throw new QueryBuildException("Not enough arguments in list");

                    if (list.size() > 3)
                            throw new QueryBuildException("Too many arguments in list : "
                                            + list);
            }
    }
    
    @Override
    protected SpatialMatch objectToStruct(PropFuncArg argObject) {
        
        if (argObject.isNode()) {
            log.warn("Object not a List: " + argObject);
            return null;
        }

	List<Node> list = argObject.getArgList();
        
        if (list.size() < 2 || list.size() > 3)
            throw new SpatialIndexException("Change in object list size");
        
        int idx = 0;
        
        Node x = list.get(idx);
        
        if (!x.isLiteral()) {
            log.warn("Latitude is not a literal " + list);
            return null;
        }
        if (!SpatialValueUtil.isDecimal(x)) {
            log.warn("Latitude is not a decimal " + list);
            return null;
        }
        Double latitude = Double.parseDouble(x.getLiteralLexicalForm());

        idx++;

        x = list.get(idx);
        if (!x.isLiteral()) {
            log.warn("Longitude is not a literal " + list);
            return null;
        }
        if (!SpatialValueUtil.isDecimal(x)) {
            log.warn("Longitude is not a decimal " + list);
            return null;
        }
        Double longitude = Double.parseDouble(x.getLiteralLexicalForm());
        
        idx++;
        
        int limit =-1;
        
        if (idx < list.size()) {
            x = list.get(idx);

            if (!x.isLiteral()) {
                    log.warn("Limit is not a literal " + list);
                    return null;
            }

            LiteralLabel lit = x.getLiteral();

            if (!XSDDatatype.XSDinteger.isValidLiteral(lit)) {
                    log.warn("Limit is not an integer " + list);
                    return null;
            }

            int v = NodeFactoryExtra.nodeToInt(x);
            limit = (v < 0) ? -1 : v;

            idx++;
            if (idx < list.size()) {
                    log.warn("Limit is not the last parameter " + list);
                    return null;
            }
        }
        
        SpatialMatch match = this.getSpatialMatch(latitude, longitude, limit);
        
        if (log.isDebugEnabled())
            log.debug("Trying SpatialMatch: " + match.toString());
                
	return match;
        
    }
    
    public SpatialMatch getSpatialMatch(Double latitude, Double longitude, int limit) {
        SpatialMatch match = new SpatialMatch(SpatialQuery.ctx.getWorldBounds().getMinY(),
                        longitude, SpatialQuery.ctx.getWorldBounds().getMaxY(), SpatialQuery.ctx.getWorldBounds()
                                        .getMaxX(), limit, getSpatialOperation());
        return match;
    }

    @Override
    protected SpatialOperation getSpatialOperation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
