package org.weyoung.xianbicycle.data;

import java.io.Serializable;

public class BicycleLocation implements Serializable{
    double lat;
    double lon;
    String name;

    public BicycleLocation(double lat, double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }
}