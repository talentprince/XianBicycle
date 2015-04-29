package org.weyoung.xianbicycle.data;

import com.google.gson.annotations.Expose;

public class BicycleData {
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

}