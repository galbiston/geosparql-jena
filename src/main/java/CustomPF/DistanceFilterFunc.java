/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomPF;




import java.io.IOException;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.SAXException;


/**
 *
 * @author haozhechen
 */
public class DistanceFilterFunc extends FunctionBase2 {
    
    private static Logger log = LoggerFactory.getLogger(DistanceFilterFunc.class);

    public DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    @Override
    public NodeValue exec(NodeValue nv1, NodeValue nv2) {
     
        
        String str1 = nv1.getString();
        String str2 = nv2.getString();
        log.info("str1 is: {}", str1);
        log.info("str1 is: {}", str2);

        return NodeValue.makeDouble(2);
    }
    
}
