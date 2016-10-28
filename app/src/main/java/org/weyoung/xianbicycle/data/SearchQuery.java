/*
 * Copyright (C) 2015 A Weyoung App
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
package org.weyoung.xianbicycle.data;

import java.util.ArrayList;
import java.util.List;

public class SearchQuery {
    String term;
    Double lat;
    Double lng;
    List<String> ids;

    public SearchQuery(String s) {
        term = s;
    }

    public SearchQuery(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public SearchQuery(List<String> ids) {
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
