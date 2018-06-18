/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.intersection_patterns;

/**
 *
 *
 */
public interface RCC8IntersectionPattern {

    public static final String EQUALS = "TFFFTFFFT";

    public static final String DISCONNECTED = "FF**FF****";
    //Pattern "FFTFFTTTT" stated in GeoSPARQL 11-052r4 p. 9 but means that Points are not disconnected not from Polygons. Using SimpleFeatures disjoint pattern as page 11 states they are equivalent.

    public static final String EXTERNALLY_CONNECTED = "FFTFTTTTT";

    public static final String PARTIALLY_OVERLAPPING = "TTTTTTTTT";

    public static final String TANGENTIAL_PROPER_PART_INVERSE = "TTTFTTFFT";

    public static final String TANGENTIAL_PROPER_PART = "TFFTTFTTT";

    public static final String NON_TANGENTIAL_PROPER_PART = "TFFTFFTTT";

    public static final String NON_TANGENTIAL_PROPER_PART_INVERSE = "TTTFFTFFT";

}
