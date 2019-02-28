package com.theah64.pigeon.model;


import com.theah64.pigeon.utils.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by theapache64 on 17/1/17.
 */

public class SocketMessage {

    private static final String KEY_TYPE = "type";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";
    private static final String KEY_DEVICE_TIME = "device_time";

    public static final String TYPE_LOCATION = "location";
    public static final String TYPE_MESSAGE = "message";
    public static final String TYPE_SEARCHING_FOR_SATELLITE = "satellite";

    private static final String KEY_SPEED = "speed";
    public static final String KEY_USER_ID = "user_id";

    private final JSONObject joSocketMessage;

    public SocketMessage(String message, boolean isError, String type, String lat, String lon, String speed) throws JSONException, IOException {

        joSocketMessage = new JSONObject();
        joSocketMessage.put(Response.KEY_MESSAGE, message);
        joSocketMessage.put(Response.KEY_ERROR, isError);

        final JSONObject joData = new JSONObject();

        if (!isError) {

            joData.put(KEY_TYPE, type);

            if (type.equals(TYPE_LOCATION)) {
                joData.put(KEY_LAT, lat);
                joData.put(KEY_LON, lon);
                joData.put(KEY_SPEED, speed);
                joData.put(KEY_DEVICE_TIME, DateFormat.getDateTimeInstance().format(new Date()));
            }
        }

        joSocketMessage.put(Response.KEY_DATA, joData);

    }

    public SocketMessage(final String message) throws IOException, JSONException {
        this(message, false, TYPE_MESSAGE, null, null, null);
    }

    public SocketMessage(final String message, final boolean isError, final String type) throws IOException, JSONException {
        this(message, isError, type, null, null, null);
    }

    public SocketMessage(final String message, boolean isError) throws IOException, JSONException {
        this(message, isError, TYPE_MESSAGE, null, null, null);
    }

    @Override
    public String toString() {
        return joSocketMessage.toString();
    }
}
