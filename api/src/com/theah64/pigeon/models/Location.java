package com.theah64.pigeon.models;

/**
 * Created by theapache64 on 21/1/17.
 */
public class Location {
    private final String tripId, lat, lon, speed, deviceTime;

    public Location(String tripId, String lat, String lon, String speed, String deviceTime) {
        this.tripId = tripId;
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.deviceTime = deviceTime;
    }

    public String getTripId() {
        return tripId;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getSpeed() {
        return speed;
    }

    public String getDeviceTime() {
        return deviceTime;
    }
}
