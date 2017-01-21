package com.theah64.pigeon.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.theah64.pigeon.model.SocketMessage;
import com.theah64.pigeon.utils.App;
import com.theah64.pigeon.utils.PermissionUtils;
import com.theah64.pigeon.utils.WebSocketHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.Date;

public class LocationReporterService extends Service implements LocationListener, PermissionUtils.Callback {

    private static final String X = LocationReporterService.class.getSimpleName();
    private String userId;
    private LocationManager locationManager;

    public LocationReporterService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(X, "Location reporter started...");
        Log.d(X, "Google api client connected");

        userId = intent.getStringExtra(SocketMessage.KEY_USER_ID);

        sendSocketMessage("Location request received", false);
        new PermissionUtils(this, this, null).begin();
        return START_STICKY;
    }

    private void sendSocketMessage(String text, boolean isError, String type) {
        try {
            final SocketMessage message = new SocketMessage(text, isError, type);
            WebSocketHelper.getInstance(this, userId).send(message);
        } catch (URISyntaxException | IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendSocketMessage(String text, boolean isError) {
        sendSocketMessage(text, isError, SocketMessage.TYPE_MESSAGE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressWarnings("MissingPermission")
    private void doNormalWork() {
        Log.d(X, "Requesting location");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            sendSocketMessage("Searching for satellites...", false, SocketMessage.TYPE_SEARCHING_FOR_SATELLITE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            ((App) getApplicationContext()).setLocationReporterService(this);
        } else {
            sendSocketMessage("GPS not enabled", false);
        }

    }


    @Override
    public void onLocationChanged(final Location location) {

        Log.i(X, "Location retrieved: " + location);
        final String latitude = String.valueOf(location.getLatitude());
        final String longitude = String.valueOf(location.getLongitude());
        final String speed = String.valueOf(location.getSpeed());
        final String deviceTime = DateFormat.getDateTimeInstance().format(new Date());

        try {

            final SocketMessage socketMessage = new SocketMessage(
                    deviceTime, false, SocketMessage.TYPE_LOCATION, latitude, longitude, speed);

            WebSocketHelper.getInstance(LocationReporterService.this, userId).send(socketMessage);

        } catch (JSONException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public void stopLocationReporting() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(X, "Returing");
            return;
        }

        Log.d(X, "Removing updated");

        locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(X, "GPS enabled");
        sendSocketMessage("GPS enabled", false);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e(X, "GPS disabled");
        sendSocketMessage("GPS disabled", true);
    }

    @Override
    public void onAllPermissionGranted() {
        doNormalWork();
    }

    @Override
    public void onPermissionDenial() {
        sendSocketMessage("Permissions are not accepted", true);
    }
}
