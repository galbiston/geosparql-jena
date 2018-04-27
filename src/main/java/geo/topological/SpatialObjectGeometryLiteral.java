/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;

/**
 *
 *
 */
public class SpatialObjectGeometryLiteral {

    private final Resource spatialObject;
    private final Literal geometryLiteral;

    public SpatialObjectGeometryLiteral(Resource spatialObject, Literal geometryLiteral) {
        this.spatialObject = spatialObject;
        this.geometryLiteral = geometryLiteral;
    }

    public Resource getSpatialObject() {
        return spatialObject;
    }

    public Literal getGeometryLiteral() {
        return geometryLiteral;
    }

    @Override
    public String toString() {
        return "SpatialObjectGeometryLiteral{" + "spatialObject=" + spatialObject + ", geometryLiteral=" + geometryLiteral + '}';
    }

}
