/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import com.vividsolutions.jts.geom.Geometry;
import org.apache.jena.datatypes.DatatypeFormatException;

/**
 *
 * @author Greg
 */
public class DatatypeSelector {

    public static final String unparse(Geometry geometry, GeoSerialisationEnum serialisation) {

        String result;
        if (serialisation.equals(GeoSerialisationEnum.WKT)) {
            WKTDatatype datatype = WKTDatatype.theWKTDatatype;
            result = datatype.unparse(geometry);
        } else {
            GMLDatatype datatype = GMLDatatype.theGMLDatatype;
            result = datatype.unparse(geometry);
        }
        return result;
    }

    public static final GeometryWrapper parse(String lexicalForm, String datatypeURI) throws DatatypeFormatException {

        GeometryWrapper geometry;
        if (datatypeURI.equals(WKTDatatype.theTypeURI)) {
            WKTDatatype datatype = WKTDatatype.theWKTDatatype;
            geometry = datatype.parse(lexicalForm);
        } else {
            GMLDatatype datatype = GMLDatatype.theGMLDatatype;
            geometry = datatype.parse(lexicalForm);
        }
        return geometry;
    }

}
