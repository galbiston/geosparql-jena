/*
 * Copyright 2019 .
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
package io.github.galbiston.geosparql_jena.implementation;

import java.lang.invoke.MethodHandles;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GreatCircleDistance {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final double EARTH_RADIUS = 6378137; //WGS84 Ellipsoid radius in metres.
    //public static final double EARTH_RADIUS = 6371e3;

    /**
     * Great circle distance between Points using Vincenty formula.
     *
     * @param point1 Lat/x and Lon/y Point in degrees.
     * @param point2 Lat/x and Lon/y Point in degrees.
     * @return Distance in metres.
     */
    public static final double vincentyFormula(Point point1, Point point2) {
        //Based on Vincenty formula: https://en.wikipedia.org/wiki/Great-circle_distance
        double lat1 = point1.getX();
        double lon1 = point1.getY();

        double lat2 = point2.getX();
        double lon2 = point2.getY();
        return vincentyFormula(lat1, lon1, lat2, lon2);
    }

    public static final double vincentyFormula(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        //double diffLatRad = Math.toRadians(lat2 - lat1);
        double diffLonRad = Math.toRadians(lon2 - lon1);

        double a = Math.pow(Math.cos(lat2Rad) * Math.sin(diffLonRad), 2);
        double b = Math.pow(Math.cos(lat1Rad) * Math.sin(lat2Rad) - Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(diffLonRad), 2);

        double c = Math.sqrt(a + b);
        double d = Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(diffLonRad);

        double e = Math.atan(c / d);

        double distance = EARTH_RADIUS * e;

        //Distance is in metres.
        return distance;
    }

    /**
     * Great circle distance between Points using Haversine formula.
     *
     * @param point1 Lat/x and Lon/y Point in degrees.
     * @param point2 Lat/x and Lon/y Point in degrees.
     * @return Distance in metres.
     */
    public static final double haversineFormula(Point point1, Point point2) {
        //Based on Haversine formula: https://www.movable-type.co.uk/scripts/latlong.html
        //Apparently there are inaccurcies for distances of points on opposite sides of the sphere so prefer Vincenty formula.
        double lat1 = point1.getX();
        double lon1 = point1.getY();

        double lat2 = point2.getX();
        double lon2 = point2.getY();

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double diffLatRad = Math.toRadians(lat2 - lat1);
        double diffLonRad = Math.toRadians(lon2 - lon1);

        double a = Math.pow(Math.sin(diffLatRad / 2), 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.pow(Math.sin(diffLonRad / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;
        return distance;
    }

}
