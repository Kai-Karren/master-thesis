package de.kaikarren.complaints.openstreetmap;

import de.kaikarren.complaints.data.GPSCoords;

public class OSMUtils {

    /**
     * Calculates the distance between two point on earth.
     * Taken from <a href="https://www.geeksforgeeks.org/program-distance-two-points-earth/">...</a>
     *
     * @param lat1 Lat coordinate of the first point
     * @param lat2 Lat coordinate of the second point
     * @param lon1 Lon coordinate of the first point
     * @param lon2 Lon coordinate of the second point
     * @return The distance between the two point in kilometers
     */
    public static double distance(double lat1, double lat2,
                                  double lon1, double lon2) {

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers.
        double r = 3956;

        // calculate the result
        return(c * r);
    }

    public static double distance (GPSCoords a, GPSCoords b){
        return distance(a.getLat(), b.getLat(), a.getLon(), b.getLon());
    }

}
