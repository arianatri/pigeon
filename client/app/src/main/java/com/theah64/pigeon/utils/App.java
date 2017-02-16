package com.theah64.pigeon.utils;

import android.app.Application;

import com.theah64.pigeon.model.SocketMessage;
import com.theah64.pigeon.services.LocationReporterService;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by theapache64 on 19/11/16.
 */
public class App extends Application {

    public static final boolean IS_DEBUG_MODE = false;
    private static final String X = App.class.getSimpleName();

    private LocationReporterService locationReporterService;

    @Override
    public void onCreate() {
        super.onCreate();

        new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
            @Override
            public void onReadyToRequest(String apiKey, String userId) {
                try {
                    WebSocketHelper.getInstance(App.this, userId).send(new SocketMessage("Pigeon on air!"));
                } catch (IOException | JSONException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String reason) {

            }
        });

    }

    public LocationReporterService getLocationReporterService() {
        return locationReporterService;
    }

    public void setLocationReporterService(LocationReporterService locationReporterService) {
        this.locationReporterService = locationReporterService;
    }
}
