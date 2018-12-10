/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.spatial.property_functions;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import io.github.galbiston.geosparql_jena.spatial.DistanceToDegrees;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.NearbyFF;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.iterator.QueryIterConcat;
import org.apache.jena.sparql.engine.iterator.QueryIterNullIterator;
import org.apache.jena.sparql.engine.iterator.QueryIterSingleton;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.util.FmtUtils;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class NearbyOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static QueryIterator exec(Binding binding, ExecutionContext execCxt, Node subject, GeometryWrapper geometryWrapper, double radius, String units, int limit) {

        //Subject is bound
        if (subject.isURI()) {
            boolean isNearby = checkBound(execCxt, subject, geometryWrapper, radius, units);
            if (isNearby) {
                return QueryIterSingleton.create(binding, execCxt);
            } else {
                return QueryIterNullIterator.create(execCxt);
            }
        }

        if (subject.isVariable()) {
            return checkUnbound(binding, execCxt, subject, geometryWrapper, radius, units, limit);
        } else {
            //Subject is not a variable (and not a URI - tested earlier).
            throw new ExprEvalException("Not a URI or variable: " + FmtUtils.stringForNode(subject));
        }

    }

    private static boolean checkBound(ExecutionContext execCxt, Node subject, GeometryWrapper geometryWrapper, double radius, String units) {

        Graph graph = execCxt.getActiveGraph();

        IteratorChain<Triple> geometryLiteralTriples = new IteratorChain();
        if (graph.contains(subject, Geo.HAS_GEOMETRY_NODE, null)) {
            //A Feature can have many geometries so add each of them. The check Geo.HAS_DEFAULT_GEOMETRY_NODE will only return one but requires the data to have these present.
            Iterator<Triple> geometryTriples = graph.find(subject, Geo.HAS_GEOMETRY_NODE, null);
            while (geometryTriples.hasNext()) {
                Node geometry = geometryTriples.next().getObject();
                geometryLiteralTriples.addIterator(graph.find(geometry, Geo.HAS_SERIALIZATION_NODE, null));
            }

        } else {
            //No GeometryLiteral so return false.
            return false;
        }

        //Check through each Geometry and stop if one is nearby.
        boolean isNearby = false;
        while (geometryLiteralTriples.hasNext()) {

            Triple triple = geometryLiteralTriples.next();
            Node geometryLiteral = triple.getObject();
            GeometryWrapper targetGeometryWrapper = GeometryWrapper.extract(geometryLiteral);
            isNearby = NearbyFF.check(geometryWrapper, targetGeometryWrapper, radius, units);
            if (isNearby) {
                //Stop checking when nearby is true.
                break;
            }
        }

        return isNearby;
    }

    private static QueryIterator checkUnbound(Binding binding, ExecutionContext execCxt, Node subject, GeometryWrapper geometryWrapper, double radius, String units, int limit) {

        QueryIterConcat queryIterConcat = new QueryIterConcat(execCxt);
        if (limit == -1) {
            limit = Integer.MAX_VALUE;
        }

        //Find all Features in the spatial index which are within the rough search envelope.
        Envelope searchEnvelope = buildSearchEnvelope(geometryWrapper, radius, units);
        List<Resource> features = SpatialIndex.query(searchEnvelope);

        Var subjectVar = Var.alloc(subject.getName());
        int count = 0;
        for (Resource feature : features) {
            boolean isNearby = checkBound(execCxt, feature.asNode(), geometryWrapper, radius, units);

            if (isNearby) {
                count++; //Exit on limit of zero.
                if (count > limit) {
                    break;
                }
                QueryIterator queryIter = QueryIterSingleton.create(binding, subjectVar, feature.asNode(), execCxt);
                queryIterConcat.add(queryIter);
            }
        }
        return queryIterConcat;
    }

    private static Envelope buildSearchEnvelope(GeometryWrapper geometryWrapper, double radius, String units) {

        try {

            //Get the envelope of the target GeometryWrapper and convert that to WGS84, in case it is a complex polygon.
            GeometryWrapper envelopeGeometryWrapper = geometryWrapper.envelope();
            //Convert to WGS84.
            GeometryWrapper wgsGeometryWrapper = envelopeGeometryWrapper.convertCRS(SRS_URI.WGS84_CRS);
            Envelope envelope = wgsGeometryWrapper.getEnvelope();

            //Expand the envelope by the radius distance in all directions,
            //i.e. a bigger box rather than circle. More precise checks made later.
            Envelope searchEnvelope = new Envelope(envelope);
            double latitude = findLatitude(wgsGeometryWrapper);
            double degreeRadius = DistanceToDegrees.convert(radius, units, latitude);
            searchEnvelope.expandBy(degreeRadius);

            return searchEnvelope;
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Exception: {}, {}, {}, {}", geometryWrapper.asLiteral(), ex.getMessage());
            throw new ExprEvalException(ex.getMessage() + ": " + geometryWrapper.asLiteral());
        }
    }

    private static double findLatitude(GeometryWrapper wgsGeometryWrapper) {
        //Latitude is Y in WGS84.
        Geometry geometry = wgsGeometryWrapper.getXYGeometry();
        Point point = geometry.getCentroid();
        return point.getY();
    }

}
