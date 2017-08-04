/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.intersectionpattern;

/**
 *
 * @author Greg
 */
public class EgenhoferIntersectionPattern {

    public static final String EQUALS = "T*F**FFF*";
    //Pattern "TFFFTFFFT" stated in GeoSPARQL 11-052r4 p. 9 but incorrect.

    public static final String DISJOINT = "FF*FF****";

    public static final String MEET1 = "FT*******";

    public static final String MEET2 = "F**T*****";

    public static final String MEET3 = "F***T****";

    public static final String OVERLAP = "T*T***T**";

    public static final String COVERS = "T*TFT*FF*";

    public static final String COVERED_BY = "TFF*TFT**";

    public static final String INSIDE = "TFF*FFT**";

    public static final String CONTAINS = "T*TFF*FF*";

}
