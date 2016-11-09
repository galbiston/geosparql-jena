/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.vocabulary;

import static implementation.support.Prefixes.NTU_URI;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class NTU {

    public static final Property HAS_EXACT_GEOMETRY_PRO = ResourceFactory.createProperty(NTU_URI + "hasExactGeometry");
    public static final Property HAS_POINT_GEOMETRY_PRO = ResourceFactory.createProperty(NTU_URI + "hasPointGeometry");

}
