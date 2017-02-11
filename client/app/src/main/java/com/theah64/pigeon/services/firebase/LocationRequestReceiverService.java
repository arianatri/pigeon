package com.theah64.pigeon.services.firebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.theah64.pigeon.model.SocketMessage;
import com.theah64.pigeon.services.LocationReporterService;
import com.theah64.pigeon.utils.APIRequestGateway;

import java.util.Map;

public class LocationRequestReceiverService extends FirebaseMessagingService {

    private static final String X = LocationRequestReceiverService.class.getSimpleName();
    private static final String KEY_TYPE = "type";
    private static final String TYPE_LOCATION_REQUEST = "location_request";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(X, "FCM sayssss: " + remoteMessage);
        Map<String, String> payload = remoteMessage.getData();
        Log.i(X, "FCM says : " + payload);

        if (!payload.isEmpty()) {

            final String type = payload.get(KEY_TYPE);

            if (type.equals(TYPE_LOCATION_REQUEST)) {

                new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
                    @Override
                    public void onReadyToRequest(String apiKey, String userId) {

                        final Intent locReqIntent = new Intent(LocationRequestReceiverService.this, LocationReporterService.class);
                        locReqIntent.putExtra(SocketMessage.KEY_USER_ID, userId);

                        startService(locReqIntent);
                    }

                    @Override
                    public void onFailed(String reason) {

                    }
                });

            }
        }
    }

}
