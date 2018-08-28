/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import implementation.GeometryWrapper;
import implementation.index.GeometryLiteralIndex.GeometryIndex;
import java.lang.invoke.MethodHandles;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public abstract class GeometryDatatype extends BaseDatatype implements DatatypeReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public GeometryDatatype(String uri) {
        super(uri);
    }

    @Override
    public abstract GeometryWrapper parse(String lexicalForm) throws DatatypeFormatException;

    public abstract GeometryWrapper parse(String lexicalForm, GeometryIndex targetIndex) throws DatatypeFormatException;

    private static final TypeMapper TYPE_MAPPER = TypeMapper.getInstance();

    public static final void registerDatatypes() {
        TYPE_MAPPER.registerDatatype(WKTDatatype.INSTANCE);
        TYPE_MAPPER.registerDatatype(GMLDatatype.INSTANCE);
    }

    public static final GeometryDatatype get(RDFDatatype rdfDatatype) {

        if (rdfDatatype instanceof GeometryDatatype) {
            return (GeometryDatatype) rdfDatatype;
        } else {
            LOGGER.error("Unrecognised Geometry Datatype: {}. Ensure that Datatype is extending GeometryDatatype.", rdfDatatype.getURI());
            throw new DatatypeFormatException("Unrecognised Geometry Datatype: " + rdfDatatype.getURI());
        }
    }

    public static final GeometryDatatype get(String datatypeURI) {
        RDFDatatype rdfDatatype = TYPE_MAPPER.getTypeByName(datatypeURI);
        return GeometryDatatype.get(rdfDatatype);
    }

    public static final boolean checkURI(String datatypeURI) {
        RDFDatatype rdfDatatype = TYPE_MAPPER.getTypeByName(datatypeURI);
        if (rdfDatatype != null) {
            return check(rdfDatatype);
        } else {
            LOGGER.error("Unrecognised Datatype: {} - Ensure that GeoSPARQLSupport is enabled and Datatype has been registered.", datatypeURI);
            throw new NullPointerException("Datatype not found: " + datatypeURI);
        }
    }

    public static final boolean check(RDFDatatype rdfDatatype) {
        return rdfDatatype instanceof GeometryDatatype;
    }

}
