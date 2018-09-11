/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geof.topological.filter_functions.egenhofer;

import geof.topological.GenericFilterFunction;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.datatype.GeometryDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.sparql.expr.NodeValue;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class EhEqualsFF extends GenericFilterFunction {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        if (v1.isLiteral() && v2.isLiteral()) {
            if (v1.asString().equals(v2.asString())) {
                String datatypeURI = v1.getDatatypeURI();
                boolean isGeometryDatatype = GeometryDatatype.checkURI(datatypeURI);
                return NodeValue.makeBoolean(isGeometryDatatype);
            } else {
                return super.exec(v1, v2);
            }
        }
        return NodeValue.FALSE;
    }

    @Override
    public Boolean exec(Literal v1, Literal v2) {
        if (v1.getLexicalForm().equals(v2.getLexicalForm())) {
            String datatypeURI = v1.getDatatypeURI();
            return GeometryDatatype.checkURI(datatypeURI);
        } else {
            return super.exec(v1, v2);
        }
    }

    //Simple Features and Egenhofer equals intersection patterns are the same, see GeoSPARQL standard page 11.
    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.equals(targetGeometry);
    }

    @Override
    protected boolean isDisjoint() {
        return false;
    }

    @Override
    protected boolean permittedTopology(DimensionInfo sourceDimensionInfo, DimensionInfo targetDimensionInfo) {
        return true;
    }

    @Override
    protected boolean isDisconnected() {
        return false;
    }
}
