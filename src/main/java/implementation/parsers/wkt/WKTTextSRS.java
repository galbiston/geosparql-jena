/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.parsers.wkt;

import implementation.index.CRSRegistry;

/**
 *
 * 
 */
public class WKTTextSRS {

    private final String wktText;
    private final String srsURI;

    public WKTTextSRS(String wktLiteral) {
        int startSRS = wktLiteral.indexOf("<");
        int endSRS = wktLiteral.indexOf(">");

        //Check that both chevrons are located and extract SRS name, otherwise default.
        if (startSRS != -1 && endSRS != -1) {
            srsURI = wktLiteral.substring(startSRS + 1, endSRS);
            wktText = wktLiteral.substring(endSRS + 1);

        } else {
            srsURI = CRSRegistry.DEFAULT_WKT_CRS84;
            wktText = wktLiteral;
        }
    }

    public String getWktText() {
        return wktText;
    }

    public String getSrsURI() {
        return srsURI;
    }

}
