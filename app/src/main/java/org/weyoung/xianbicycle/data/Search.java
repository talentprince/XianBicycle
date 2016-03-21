package org.weyoung.xianbicycle.data;

import java.util.ArrayList;
import java.util.List;

public class Search {
    String term;
    Double lat;
    Double lng;
    List<String> ids;

    public Search(String s) {
        term = s;
    }

    public Search(Double lat, Double lng) {
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
