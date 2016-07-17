/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomPF;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.gml2.GMLReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Substitute;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.pfunction.PropertyFunctionBase;
import org.apache.jena.sparql.util.IterLib;
import org.apache.jena.sparql.util.NodeFactoryExtra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author haozhechen
 */
public class Distance extends PropertyFunctionBase {

    private static Logger log = LoggerFactory.getLogger(Distance.class);

    @Override
    public QueryIterator exec(Binding binding, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCtx) {
        log.info("Property Function called");
        argSubject = Substitute.substitute(argSubject, binding);
        argObject = Substitute.substitute(argObject, binding);
        List<Node> list = argObject.getArgList();

        //-----------------------
        // actual string in the list:
        //"<gml:Point srsName='urn:ogc:def:crs:EPSG::27700' xmlns:gml='http://www.opengis.net/ont/gml'><gml:coordinates>455483.192,339437.019</gml:coordinates></gml:Point>"^^<geo:gmlLiteral> | "<gml:Point srsName='urn:ogc:def:crs:EPSG::27700' xmlns:gml='http://www.opengis.net/ont/gml'><gml:coordinates>457858.03,339326.045</gml:coordinates></gml:Point>"^^<geo:gmlLiteral>
        //this will cause a SAX parser error
        //so I use string builder to omit the first " and the "^^<geo:gmlLiteral> in the end(indluding the double quote mark)
        //Improvement point: Is there a easier way to do it?
        //-----------------------
        String str1 = list.get(0).toString();
        int index1 = list.get(0).toString().lastIndexOf("\"");
        str1 = str1.substring(1, index1);

        String str2 = list.get(1).toString();
        int index2 = str2.lastIndexOf("\"");
        str2 = str2.substring(1, index2);

        if (!list.isEmpty()) {
            log.info("Arguments in the List: {}.", str1);
        }

        GMLReader gmlReader = new GMLReader();
        double distance = -1;
        try {
            Geometry g1 = gmlReader.read(str1, null);
            Geometry g2 = gmlReader.read(str2, null);
            distance = g1.distance(g2);
            log.info("distance is: {}", distance);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(Distance.class.getName()).log(Level.SEVERE, null, ex);
        }

        Node dis = NodeFactoryExtra.doubleToNode(distance);

        return IterLib.oneResult(binding, Var.alloc(argSubject.getArg()), dis, execCtx);
    }

}
