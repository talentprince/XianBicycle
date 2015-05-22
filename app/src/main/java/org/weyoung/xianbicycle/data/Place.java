package org.weyoung.xianbicycle.data;

import java.io.Serializable;

public class Place implements Serializable{
    public static final int E6 = 1000000;
    int lat;
    int lon;
    String name;
    String description;

    public Place(double lat, double lon, String name, String description) {
        this.lat = (int) (lat * E6);
        this.lon = (int) (lon * E6);
        this.name = name;
        this.description = description;
    }

    public int getLat() {
        return lat;
    }

    public int getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
