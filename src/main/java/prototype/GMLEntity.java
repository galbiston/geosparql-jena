/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import com.spatial4j.core.context.SpatialContextFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.jena.graph.Node;
import org.apache.jena.query.spatial.SpatialQuery;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class GMLEntity {

    private final String entityField;
    private final String geoField;
    private final Set<Node> GMLPredicates;
    private final Set<Node> builtinGMLPredicates;

    public final static Property asGML = ResourceFactory
            .createProperty(Vocabulary.GML_URI + "asGML");
    public final static Property GMLLiteral = ResourceFactory
            .createProperty(Vocabulary.GEO_URI + "gmlLiteral");

    public GMLEntity(String entityField, String geoField) {
        this.entityField = entityField == null || entityField.isEmpty() ? "entityField"
                : entityField;
        this.geoField = geoField == null || geoField.isEmpty() ? "geoField"
                : geoField;
        this.GMLPredicates = new HashSet<Node>();
        this.builtinGMLPredicates = new HashSet<Node>();
        initBuiltinPredicates();
    }

    public void setSpatialContextFactory(String spatialContextFactoryClass) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("spatialContextFactory", spatialContextFactoryClass);
        SpatialQuery.ctx = SpatialContextFactory.makeSpatialContext(map,
                SpatialQuery.class.getClassLoader());
    }

    private void initBuiltinPredicates() {

        addBuiltinWKTPredicate(asGML);
        addBuiltinWKTPredicate(GMLLiteral);
    }

    private boolean addBuiltinWKTPredicate(Resource predicate) {
        builtinGMLPredicates.add(predicate.asNode());
        return addGMLPredicate(predicate);
    }

    public boolean addGMLPredicate(Resource predicate) {
        return GMLPredicates.add(predicate.asNode());
    }

    public String getEntityField() {
        return entityField;
    }

    public String getGeoField() {
        return geoField;
    }

    public boolean isGMLPredicate(Node predicate) {
        return this.GMLPredicates.contains(predicate);
    }

    public int getCustomWKTPredicateCount() {
        return this.GMLPredicates.size() - builtinGMLPredicates.size();
    }

    public int getWKTPredicateCount() {
        return this.GMLPredicates.size();
    }

    @Override
    public String toString() {
        return entityField + ":" + geoField;

    }

}
