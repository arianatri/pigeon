package com.theah64.pigeon.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.theah64.pigeon.asyncs.FCMSynchronizer;
import com.theah64.pigeon.model.SocketMessage;
import com.theah64.pigeon.model.User;
import com.theah64.pigeon.utils.APIRequestGateway;
import com.theah64.pigeon.utils.NetworkUtils;
import com.theah64.pigeon.utils.PrefUtils;
import com.theah64.pigeon.utils.WebSocketHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;


public class NetworkReceiver extends BroadcastReceiver {

    private static final String X = NetworkReceiver.class.getSimpleName();

    public NetworkReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                doNormalWork(context);
            } else {
                Log.d(X, "Permission not yet granted");
            }

        } else {
            doNormalWork(context);
        }


    }

    private static void doNormalWork(final Context context) {

        if (NetworkUtils.hasNetwork(context)) {

            new APIRequestGateway(context, new APIRequestGateway.APIRequestGatewayCallback() {

                @Override
                public void onReadyToRequest(String apiKey, final String userId) {

                    try {
                        WebSocketHelper.getInstance(context, userId).send(new SocketMessage("Device connected to the server"));
                    } catch (URISyntaxException | IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    //Syncing unsynced fcm
                    if (!PrefUtils.getInstance(context).getBoolean(User.KEY_IS_FCM_SYNCED)) {
                        new FCMSynchronizer(context, apiKey).execute();
                    }
                }

                @Override
                public void onFailed(String reason) {
                    Log.e(X, "Reason: " + reason);
                }
            });


        }
    }
}
