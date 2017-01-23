package com.theah64.pigeon.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.theah64.pigeon.model.SocketMessage;
import com.theah64.pigeon.services.LocationReporterService;
import com.theah64.pigeon.services.firebase.LocationRequestReceiverService;
import com.theah64.pigeon.utils.APIRequestGateway;
import com.theah64.pigeon.utils.NetworkUtils;
import com.theah64.pigeon.utils.PermissionUtils;
import com.theah64.pigeon.utils.WebSocketHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

public class LocationProviderListener extends BroadcastReceiver implements PermissionUtils.Callback {

    private static final String X = LocationProviderListener.class.getSimpleName();
    private Context context;

    public LocationProviderListener() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        new PermissionUtils(context, this, null).begin();
    }

    @Override
    public void onAllPermissionGranted() {

        Log.d(X, "GPS Enabled");
        new APIRequestGateway(context, new APIRequestGateway.APIRequestGatewayCallback() {
            @Override
            public void onReadyToRequest(String apiKey, String userId) {
                try {
                    final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        WebSocketHelper.getInstance(context, userId).send(new SocketMessage("GPS enabled"));
                    } else {
                        WebSocketHelper.getInstance(context, userId).send(new SocketMessage("GPS disabled"));
                    }
                } catch (URISyntaxException | IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String reason) {

            }
        });


    }

    @Override
    public void onPermissionDenial() {
        Log.e(X, "Permissions are not accepted");
    }
}
