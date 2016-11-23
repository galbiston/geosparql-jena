/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers;

import com.vividsolutions.jts.geom.Geometry;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import static implementation.datatype.WKTDatatype.DEFAULT_WKT_CRS_URI;
import implementation.support.GeoSerialisationEnum;

/**
 *
 * @author Greg
 */
public class WKTParser {

    public GeometryWrapper read(String wktLiteral) throws ParseException {

        WktTextSRS wktTextSRS = extractTextSRS(wktLiteral);

        WKTInfo wktInfo = WKTInfo.extract(wktTextSRS.wktText);

        Geometry geometry = wktInfo.getGeometry();
        DimensionInfo dimensionInfo = wktInfo.getDimensionInfo();

        return new GeometryWrapper(geometry, wktTextSRS.srsURI, GeoSerialisationEnum.WKT, dimensionInfo);

    }

    private WktTextSRS extractTextSRS(String wktLiteral) {

        String srsURI;
        String wktText;

        int startSRS = wktLiteral.indexOf("<");
        int endSRS = wktLiteral.indexOf(">");

        //Check that both chevrons are located and extract SRS name, otherwise default.
        if (startSRS != -1 && endSRS != -1) {
            srsURI = wktLiteral.substring(startSRS + 1, endSRS);
            wktText = wktLiteral.substring(endSRS + 1);

        } else {
            srsURI = DEFAULT_WKT_CRS_URI;
            wktText = wktLiteral;
        }

        return (new WktTextSRS(wktText, srsURI));
    }

    private class WktTextSRS {

        private final String wktText;
        private final String srsURI;

        public WktTextSRS(String wktText, String srsURI) {
            this.wktText = wktText;
            this.srsURI = srsURI;
        }

    }

    public final String write(GeometryWrapper geometryWrapper) {

        StringBuilder sb = new StringBuilder();

        sb.append("<").append(geometryWrapper.getSrsURI()).append(">");
        sb.append(" ");

        Geometry geometry = geometryWrapper.getParsingGeometry();
        String wktText = WKTWriter.expand(geometry);

        sb.append(wktText);

        return sb.toString();
    }

}
