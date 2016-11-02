/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.vocabulary;

import implementation.support.Prefixes;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 * @author Greg
 */
public class Geo {

    public static final Property HAS_SERIALISATION_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "hasSerialisation");
    public static final Node HAS_SERIALISATION_NODE = HAS_SERIALISATION_PRO.asNode();

    public static final Property AS_WKT_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "asWKT");
    public static final Property AS_GML_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "asGML");

    public static final Property HAS_DEFAULT_GEOMETRY_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "hasDefaultGeometry");
    public static final Node HAS_DEFAULT_GEOMETRY_NODE = HAS_DEFAULT_GEOMETRY_PRO.asNode();

    public static final Property HAS_GEOMETRY_PRO = ResourceFactory.createProperty(Prefixes.GEO_URI + "hasGeometry");

    public static final Resource GEOMETRY_RES = ResourceFactory.createResource(Prefixes.GEO_URI + "Geometry");
    public static final Node GEOMETRY_NODE = GEOMETRY_RES.asNode();

    public static final Resource FEATURE_RES = ResourceFactory.createResource(Prefixes.GEO_URI + "Feature");
    public static final Node FEATURE_NODE = FEATURE_RES.asNode();

}
