package com.objects;

public class Spot {
    private double lat;
    private double lon;
    private String spotName;

    Spot(double lat, double lon, String spotName) {
        this.lat = lat;
        this.lon = lon;
        this.spotName = spotName;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }
    /**
     * @return the lon
     */
    public double getLon() {
        return lon;
    }
    /**
     * @return the spotName
     */
    public String getSpotName() {
        return spotName;
    }
}
