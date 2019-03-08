package com.theah64.pigeon.servlets;

import com.theah64.pigeon.database.tables.BaseTable;
import com.theah64.pigeon.database.tables.Users;
import com.theah64.pigeon.models.User;
import com.theah64.pigeon.utils.Response;
import com.theah64.pigeon.utils.RandomString;
import com.theah64.pigeon.utils.Request;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;

/**
 * Created by theapache64 on 18/11/16,1:33 AM.if
 * route: /get_api_key
 * -------------------
 * on_req: company_code,name, imei, fcm_id
 * on_resp: api_key
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/get_api_key"})
public class GetAPIKeyServlet extends AdvancedBaseServlet {

    private static final int API_KEY_LENGTH = 10;

    @Override
    protected boolean isSecureServlet() {
        return false;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Users.COLUMN_NAME, Users.COLUMN_DEVICE_HASH, Users.COLUMN_IMEI};
    }


    @Override
    protected void doAdvancedPost() throws Request.RequestException, BaseTable.InsertFailedException, JSONException, BaseTable.UpdateFailedException {

        System.out.println("----------------------");
        System.out.println("New api request received....");

        final String deviceHash = getStringParameter(Users.COLUMN_DEVICE_HASH);
        final String fcmId = getStringParameter(Users.COLUMN_FCM_ID);

        final Users usersTable = Users.getInstance();
        User user = usersTable.get(Users.COLUMN_DEVICE_HASH, deviceHash);

        if (user != null) {

            //EMP exist.
            if (fcmId != null) {

                //Updating fcm id
                usersTable.update(Users.COLUMN_ID, user.getId(), Users.COLUMN_FCM_ID, fcmId);
            }


        } else {

            //EMP doesn't exist. so create new one.
            final String name = getStringParameter(Users.COLUMN_NAME);
            final String imei = getStringParameter(Users.COLUMN_IMEI);

            final String apiKey = RandomString.getNewApiKey(API_KEY_LENGTH);

            user = new User(null, name, imei, deviceHash, fcmId, apiKey, fcmId != null, true);
            final String empId = usersTable.addv3(user);
            user.setId(empId);
        }

        System.out.println("User: " + user);

        final JSONObject joData = new JSONObject();
        joData.put(Users.COLUMN_API_KEY, user.getAPIKey());
        joData.put(Users.COLUMN_ID, user.getId());

        //Finally showing api key
        getWriter().write(new Response("User verified", joData).getResponse());


    }
}
