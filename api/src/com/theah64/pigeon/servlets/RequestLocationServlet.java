package com.theah64.pigeon.servlets;


import com.theah64.pigeon.database.tables.BaseTable;
import com.theah64.pigeon.database.tables.Users;
import com.theah64.pigeon.utils.Response;
import com.theah64.pigeon.utils.FCMUtils;
import com.theah64.pigeon.utils.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by theapache64 on 19/11/16,3:21 PM.
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/request_location"})
public class RequestLocationServlet extends AdvancedBaseServlet {

    @Override
    protected boolean isSecureServlet() {
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Users.COLUMN_ID};
    }

    @Override
    protected void doAdvancedPost() throws Request.RequestException, BaseTable.InsertFailedException, JSONException, BaseTable.UpdateFailedException {

        final String userId = getStringParameter(Users.COLUMN_ID);
        final String fcmId = Users.getInstance().get(Users.COLUMN_ID, userId, Users.COLUMN_FCM_ID, true);

        if (fcmId != null) {

            final JSONArray jaFcmIds = new JSONArray();
            jaFcmIds.put(fcmId);

            //Alright
            final JSONObject joFcmResp = FCMUtils.sendLocationRequest(jaFcmIds);

            if (joFcmResp != null) {

                final boolean isEverythingOk = joFcmResp.getInt("failure") == 0;

                if (!isEverythingOk) {
                    System.out.println("FCM FAILED: " + joFcmResp);
                    getWriter().write(new Response("Failed to fire fcm").getResponse());
                } else {
                    getWriter().write(new Response("Location request sent", null).getResponse());
                }


            } else {
                throw new Request.RequestException("Failed to send location request");
            }

        } else {
            throw new Request.RequestException("No user found");
        }

    }
}
