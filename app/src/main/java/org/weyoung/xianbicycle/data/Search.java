package org.weyoung.xianbicycle.data;

import java.util.ArrayList;
import java.util.List;

public class Search {
    String term;
    String lat;
    String lng;
    List<String> ids;

    public Search(String s) {
        term = s;
    }

    public Search(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Search(List<String> ids) {
        this.ids = new ArrayList<>();
        this.ids.addAll(ids);
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
