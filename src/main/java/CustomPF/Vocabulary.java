/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomPF;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 * @author haozhechen
 */
public class Vocabulary {

    public static final String NAMESPACE = "http://ntu.ac.uk/";

    public static final Resource BUILDING_TYPE_RES = ResourceFactory.createResource(NAMESPACE + "building");

}
