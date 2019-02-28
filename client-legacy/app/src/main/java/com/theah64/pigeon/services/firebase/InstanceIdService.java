package com.theah64.pigeon.services.firebase;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.theah64.pigeon.asyncs.FCMSynchronizer;
import com.theah64.pigeon.utils.APIRequestGateway;
import com.theah64.pigeon.utils.PermissionUtils;
import com.theah64.pigeon.utils.PrefUtils;
import com.theah64.pigeon.model.User;

public class InstanceIdService extends FirebaseInstanceIdService implements PermissionUtils.Callback {

    private static final String X = InstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        final String newFcmId = FirebaseInstanceId.getInstance().getToken();
        Log.i(X, "Firebase token refreshed : " + newFcmId);

        final SharedPreferences.Editor prefEditor = PrefUtils.getInstance(this).getEditor();
        prefEditor.putString(User.KEY_FCM_ID, newFcmId);
        prefEditor.putBoolean(User.KEY_IS_FCM_SYNCED, false);
        prefEditor.commit();

        new PermissionUtils(this, this, null).begin();
    }

    private void doNormalWork() {

        new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
            @Override
            public void onReadyToRequest(String apiKey, final String id) {
                new FCMSynchronizer(InstanceIdService.this, apiKey).execute();
            }

            @Override
            public void onFailed(String reason) {

            }
        });

    }

    @Override
    public void onAllPermissionGranted() {
        doNormalWork();
    }

    @Override
    public void onPermissionDenial() {
        Log.d(X, "Permission denied");
    }
}
