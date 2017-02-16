package com.theah64.pigeon.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by theapache64 on 14/9/16,6:07 PM.
 */
public class FCMUtils {

    private static final String FCM_SEND_URL = "https://fcm.googleapis.com/fcm/send";


    public static final String KEY_TYPE = "type";
    public static final String TYPE_LOCATION_REQUEST = "location_request";
    public static final String KEY_DATA = "data";
    public static final String KEY_TO = "to";
    private static final String FCM_NOTIFICATION_KEY = "AAAAPhR8lrk:APA91bFQjEEOrSQUg2y5fPOD0Xe_mv4bb6MsgYsZPu3nJhzHEsT-LA74GSDcpDOasDjH3Ms8EdkLb6oXbk0NIENRAEW6dkh3jgyZitScDYS8sAYIvaWOmhMh_xNGUEu9ix0zs1H05LSL";
    private static final String KEY_REG_IDS = "registration_ids";

    public static JSONObject sendLocationRequest(final JSONArray jaFcmIds) {

        System.out.println(jaFcmIds);

        final JSONObject joFcm = new JSONObject();
        try {

            joFcm.put(jaFcmIds.length() == 1 ? FCMUtils.KEY_TO : FCMUtils.KEY_REG_IDS, jaFcmIds.length() == 1 ? jaFcmIds.get(0) : jaFcmIds);

            final JSONObject joData = new JSONObject();
            joData.put(FCMUtils.KEY_TYPE, FCMUtils.TYPE_LOCATION_REQUEST);
            joFcm.put(FCMUtils.KEY_DATA, joData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sendPayload(joFcm.toString());
    }

    private static JSONObject sendPayload(String payload) {


        try {

            System.out.println("To FCM: " + payload);

            final URL url = new URL(FCM_SEND_URL);
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty("Authorization", "key=" + FCM_NOTIFICATION_KEY);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            OutputStream os = urlConnection.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            os.close();

            final BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line).append("\n");
            }

            br.close();

            System.out.println("FCM Says : " + response.toString());

            return new JSONObject(response.toString());

        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
