package org.weyoung.xianbicycle.net;

public class Search {
    String term;
    String lat;
    String lng;

    public Search(String s) {
        term = s;
    }

    public Search(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
