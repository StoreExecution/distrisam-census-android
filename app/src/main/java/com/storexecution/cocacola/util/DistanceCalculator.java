package com.storexecution.cocacola.util;

/**
 * Created by Koceila on 17/12/2016.
 */

public class DistanceCalculator {

    /**
     * Fucntion to calculate the distance in meters between two Geo points
     *
     * @param orginLatitude        origin latitude
     * @param originLongitude      origin longitude
     * @param distinationLatitude  distination latitude
     * @param distinationLongitude distination longitude
     * @return the distance in meters
     */
    public static double getDistance(double orginLatitude, double originLongitude, double distinationLatitude, double distinationLongitude) {
        double theta = originLongitude - distinationLongitude;
        double dist = Math.sin(deg2rad(orginLatitude)) * Math.sin(deg2rad(distinationLatitude)) + Math.cos(deg2rad(orginLatitude)) * Math.cos(deg2rad(distinationLatitude)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;


        return (dist * 1.609344 * 1000);
    }

    /**
     * This function converts decimal degrees to radians
     *
     * @param deg
     * @return
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * This function converts radians to decimal degrees
     *
     * @param rad
     * @return
     */
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
