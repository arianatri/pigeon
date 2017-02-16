package com.theah64.pigeon.servlets;


import com.theah64.pigeon.database.tables.BaseTable;
import com.theah64.pigeon.database.tables.Users;
import com.theah64.pigeon.utils.Response;
import com.theah64.pigeon.utils.Request;
import org.json.JSONException;

import javax.servlet.annotation.WebServlet;

/**
 * Created by theapache64 on 19/11/16,3:11 PM.
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/update_fcm"})
public class UpdateFCMServlet extends AdvancedBaseServlet {

    @Override
    protected boolean isSecureServlet() {
        return true;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Users.COLUMN_FCM_ID};
    }

    @Override
    protected void doAdvancedPost() throws Request.RequestException, BaseTable.InsertFailedException, JSONException, BaseTable.UpdateFailedException {
        final String userId = getHeaderSecurity().getUserId();
        final String fcmId = getStringParameter(Users.COLUMN_FCM_ID);
        Users.getInstance().update(Users.COLUMN_ID, userId, Users.COLUMN_FCM_ID, fcmId);
        getWriter().write(new Response("FCM id updated", null).getResponse());
    }
}
