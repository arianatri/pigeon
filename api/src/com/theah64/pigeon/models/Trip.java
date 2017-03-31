package com.theah64.pigeon.models;

/**
 * Created by theapache64 on 31/3/17.
 */
public class Trip {
    private final String userId;

    public Trip(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
