package com.theah64.pigeon.asyncs;

import android.content.Context;
import android.util.Log;

import com.theah64.pigeon.model.SocketMessage;
import com.theah64.pigeon.model.User;
import com.theah64.pigeon.utils.APIRequestBuilder;
import com.theah64.pigeon.utils.APIRequestGateway;
import com.theah64.pigeon.utils.Response;
import com.theah64.pigeon.utils.OkHttpUtils;
import com.theah64.pigeon.utils.PrefUtils;
import com.theah64.pigeon.utils.WebSocketHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;

/**
 * Created by theapache64 on 28/9/16.
 */

public class FCMSynchronizer extends BaseJSONPostNetworkAsyncTask<Void> {

    private static final String X = FCMSynchronizer.class.getSimpleName();
    private final String newFcmId;
    private final boolean isFCMSynced;

    public FCMSynchronizer(Context context, String apiKey) {
        super(context, apiKey);
        final PrefUtils prefUtils = PrefUtils.getInstance(context);
        this.newFcmId = prefUtils.getString(User.KEY_FCM_ID);
        this.isFCMSynced = prefUtils.getBoolean(User.KEY_IS_FCM_SYNCED);

        Log.d(X, "Started");
    }

    @Override
    protected synchronized Void doInBackground(String... strings) {

        if (newFcmId != null && !isFCMSynced) {

            Log.d(X, "Updating...");

            new APIRequestGateway(getContext(), new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String apiKey, final String userId) {

                    try {
                        WebSocketHelper.getInstance(getContext(), userId).send(new SocketMessage("Syncing FCM"));
                    } catch (URISyntaxException | IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    final Request fcmUpdateRequest = new APIRequestBuilder("/update_fcm", apiKey)
                            .addParam(User.KEY_FCM_ID, newFcmId)
                            .build();

                    OkHttpUtils.getInstance().getClient().newCall(fcmUpdateRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, okhttp3.Response response) throws IOException {
                            try {
                                new Response(OkHttpUtils.logAndGetStringBody(response));
                                PrefUtils.getInstance(getContext()).getEditor()
                                        .putBoolean(User.KEY_IS_FCM_SYNCED, true)
                                        .commit();

                                try {
                                    Log.d(X, "FCM Synced");
                                    WebSocketHelper.getInstance(getContext(), userId).send(new SocketMessage("FCM Synced"));
                                } catch (URISyntaxException | IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException | Response.ResponseException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFailed(String reason) {
                    Log.e(X, "Failed to update fcm : " + reason);
                }
            });
        }

        return null;
    }
}
