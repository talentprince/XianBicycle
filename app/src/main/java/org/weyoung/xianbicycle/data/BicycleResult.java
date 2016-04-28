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

import com.google.gson.annotations.Expose;

public class BicycleResult {
    @Expose
    private String emptynum;
    @Expose
    private String latitude;
    @Expose
    private String location;
    @Expose
    private String locknum;
    @Expose
    private String longitude;
    @Expose
    private String siteid;
    @Expose
    private String sitename;
    @Expose
    private Double distance;

    /**
     *
     * @return
     * The emptynum
     */
    public String getEmptynum() {
        return emptynum;
    }

    /**
     *
     * @param emptynum
     * The emptynum
     */
    public void setEmptynum(String emptynum) {
        this.emptynum = emptynum;
    }

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The locknum
     */
    public String getLocknum() {
        return locknum;
    }

    /**
     *
     * @param locknum
     * The locknum
     */
    public void setLocknum(String locknum) {
        this.locknum = locknum;
    }

    /**
     *
     * @return
     * The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The siteid
     */
    public String getSiteid() {
        return siteid;
    }

    /**
     *
     * @param siteid
     * The siteid
     */
    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    /**
     *
     * @return
     * The sitename
     */
    public String getSitename() {
        return sitename;
    }

    /**
     *
     * @param sitename
     * The sitename
     */
    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}