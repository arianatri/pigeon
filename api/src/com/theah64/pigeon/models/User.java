package com.theah64.pigeon.models;

/**
 * Created by theapache64 on 21/1/17.
 * id          INT         NOT NULL AUTO_INCREMENT,
 * name        VARCHAR(20) NOT NULL,
 * imei        VARCHAR(20) NOT NULL,
 * device_hash TEXT        NOT NULL,
 * fcm_id      TEXT,
 * api_key     VARCHAR(20) NOT NULL,
 * is_active   TINYINT(4)  NOT NULL DEFAULT 1,
 * created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
 */
public class User {

    private String id;
    private final String name;
    private final String imei;
    private final String deviceHash;
    private final String fcmId;
    private final String apiKey;
    private final boolean hasFcm, isActive;

    public User(String id, String name, String imei, String deviceHash, String fcmId, String apiKey, boolean hasFcm, boolean isActive) {
        this.id = id;
        this.name = name;
        this.imei = imei;
        this.deviceHash = deviceHash;
        this.fcmId = fcmId;
        this.apiKey = apiKey;
        this.hasFcm = hasFcm;
        this.isActive = isActive;
    }

    public boolean hasFcm() {
        return hasFcm;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIMEI() {
        return imei;
    }

    public String getDeviceHash() {
        return deviceHash;
    }

    public String getFCMId() {
        return fcmId;
    }

    public String getAPIKey() {
        return apiKey;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imei='" + imei + '\'' +
                ", deviceHash='" + deviceHash + '\'' +
                ", fcmId='" + fcmId + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
