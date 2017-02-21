package com.theah64.pigeon.sockets;

import com.theah64.pigeon.database.tables.Users;
import com.theah64.pigeon.models.User;
import com.theah64.pigeon.servlets.AdvancedBaseServlet;
import com.theah64.pigeon.utils.FCMUtils;
import com.theah64.pigeon.utils.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * Created by theapache64 on 21/1/17.
 */
@ServerEndpoint(AdvancedBaseServlet.VERSION_CODE + "/pigeon_socket/{type}/{user_id}")
public class PigeonSocket {

    private static final String TYPE_LISTENER = "listener";
    private static final String TYPE_TELLER = "teller";

    private static final Map<String, Set<Session>> listenerSessions = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Session> tellerSessions = Collections.synchronizedMap(new HashMap<>());
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TYPE = "type";

    @OnOpen
    public void onOpen(@PathParam(KEY_USER_ID) String userId, @PathParam(KEY_TYPE) String type, Session session) throws IOException, JSONException {

        System.out.println("-------------------------");
        System.out.println("Socket open request : " + session.getQueryString());

        try {
            final User user = Users.getInstance().get(Users.COLUMN_ID, userId);

            if (user != null) {

                System.out.println("User: " + user);

                if (type.equals(TYPE_LISTENER)) {

                    System.out.println("It's a listener");

                    //listenr session
                    Set<Session> lSessions = listenerSessions.get(user.getId());

                    if (lSessions == null) {
                        lSessions = new HashSet<>();
                        lSessions.add(session);
                        //Added listener
                        listenerSessions.put(userId, lSessions);

                    } else {
                        lSessions.add(session);
                    }


                    System.out.println("Added listener for " + userId);
                    System.out.println("Total listener for " + userId + " - " + listenerSessions.get(userId).size());
                    System.out.println("Total tellers : " + tellerSessions.size());

                    sendTellerPing(user);

                } else if (type.equals(TYPE_TELLER)) {
                    //Connection made by an android device normally - new connection

                    System.out.println("It's a teller");

                    //Adding teller session
                    tellerSessions.put(userId, session);

                    System.out.println("Added teller :" + userId);
                    System.out.println("Total listener for " + userId + " - " + (listenerSessions.get(userId) != null ? listenerSessions.get(userId).size() : "0"));
                    System.out.println("Total tellers : " + tellerSessions.size());

                    //First checking if there's any listener for the teller
                    final boolean hasListeners = listenerSessions.get(userId) != null && !listenerSessions.get(userId).isEmpty();

                    if (hasListeners) {

                        //The teller has one or more listeners.. so notify every listener that the teller is connected
                        for (final Session listener : listenerSessions.get(userId)) {
                            listener.getBasicRemote().sendText(new Response("Device connected", null).getResponse());
                        }

                        //Sending teller a location request
                        sendTellerPing(user);

                    } else {
                        //No listener for the teller, so telling a story to the air is meaning less. therefore, closing the teller
                        throw new PigeonSocketException("No listeners found for " + userId, PigeonSocketException.ERROR_CODE_NO_LISTENER_FOUND);
                    }


                } else {
                    throw new PigeonSocketException("Invalid type: " + type, PigeonSocketException.ERROR_CODE_INVALID_TYPE);
                }
            } else {
                throw new PigeonSocketException("Invalid userId " + userId, PigeonSocketException.ERROR_CODE_INVALID_USER_ID);
            }

        } catch (PigeonSocketException e) {
            e.printStackTrace();

            if (e.getErrorCode() % 2 != 0) {
                //even number error codes are fatal errors
                System.out.println("FATAL ERROR");
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, e.getMessage()));
            } else {
                //Building error
                session.getBasicRemote().sendText(new Response(e.getErrorCode(), e.getMessage()).getResponse());
            }
        }
    }

    private void sendTellerPing(final User user) throws IOException, PigeonSocketException, JSONException {

        //Checking if there's a teller session
        System.out.println("Sending teller ping...");

        //No teller session available, so send a new request
        final JSONArray jaFcmIds = new JSONArray();
        jaFcmIds.put(user.getFCMId());

        //Alright
        final JSONObject joFcmResp = FCMUtils.sendLocationRequest(jaFcmIds);

        final boolean isEverythingOk = joFcmResp != null && joFcmResp.getInt("failure") == 0;

        if (isEverythingOk) {
            if (listenerSessions.get(user.getId()) != null && !listenerSessions.get(user.getId()).isEmpty()) {
                for (final Session listener : listenerSessions.get(user.getId())) {
                    listener.getBasicRemote().sendText(new Response("Location request sent", null).getResponse());
                }
            }
        } else {
            final String error = joFcmResp.getJSONArray("results").getJSONObject(0).getString("error");
            throw new PigeonSocketException("Failed to send location request : " + error, PigeonSocketException.ERROR_CODE_FCM_FIRE_FAILED);
        }

    }

    @OnMessage
    public void onMessage(Session session, String data) throws IOException {

        final String type = session.getRequestParameterMap().get(KEY_TYPE).get(0);
        final String userId = session.getRequestParameterMap().get(KEY_USER_ID).get(0);

        if (type.equals(TYPE_TELLER)) {

            if (listenerSessions.get(userId) != null && !listenerSessions.get(userId).isEmpty()) {

                //Teller got something to tell to the listener
                for (final Session listener : listenerSessions.get(userId)) {
                    listener.getBasicRemote().sendText(data);
                }

            } else {
                System.out.println("No listener found for teller : " + userId);
                session.getBasicRemote().sendText(new Response(PigeonSocketException.ERROR_CODE_NO_LISTENER_FOUND, "No listener found for " + userId).getResponse());
            }

        } else {
            //Listener got something to tell to the teller
            tellerSessions.get(userId).getBasicRemote().sendText(data);
        }

    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
        System.out.println("ERROR:" + e.getMessage());
    }

    @OnClose
    public void onClose(Session session) throws IOException {

        System.out.println("Socket onClose called");

        final String type = session.getRequestParameterMap().get(KEY_TYPE).get(0);
        final String userId = session.getRequestParameterMap().get(KEY_USER_ID).get(0);

        if (type.equals(TYPE_TELLER)) {

            System.out.println("Removing a teller: " + userId);

            //Remove teller session
            tellerSessions.remove(userId);

            if (listenerSessions.get(userId) != null && !listenerSessions.isEmpty()) {

                System.out.println("Notifying listeners about teller death");

                //Teller closed, remove every listener session
                for (final Session listener : listenerSessions.get(userId)) {
                    listener.getBasicRemote().sendText(new Response(PigeonSocketException.ERROR_CODE_TELLER_CLOSED, "Device gone offline").getResponse());
                }

            } else {
                System.out.println("Listener session is null");
            }

            System.out.println("Total listener for " + userId + " - " + listenerSessions.get(userId).size());
            System.out.println("Total tellers : " + tellerSessions.size());

        } else {

            System.out.println("Removing a listener of " + userId);

            //A listener closed
            listenerSessions.get(userId).remove(session);

            //Checking if there's at least one listener and if not close the teller
            final boolean hasListeners = !listenerSessions.get(userId).isEmpty();

            System.out.println("Has more listeners: " + listenerSessions.get(userId).size());

            if (!hasListeners) {

                if (tellerSessions.get(userId) != null) {
                    //No more listener found for the teller, so close the teller
                    tellerSessions.get(userId).getBasicRemote().sendText(new Response(PigeonSocketException.ERROR_CODE_NO_LISTENER_FOUND, "No more listener found").getResponse());
                } else {
                    System.out.println("Teller is null for :" + userId);
                }

            }

        }
    }

    private class PigeonSocketException extends Exception {

        static final int ERROR_CODE_INVALID_TYPE = 1;
        static final int ERROR_CODE_INVALID_USER_ID = 3;
        static final int ERROR_CODE_NO_LISTENER_FOUND = 2;
        static final int ERROR_CODE_TELLER_CLOSED = 4;
        static final int ERROR_CODE_FCM_FIRE_FAILED = 7;

        private final int errorCode;

        PigeonSocketException(String message, int errorCode) {
            super(message);
            this.errorCode = errorCode;
        }

        int getErrorCode() {
            return errorCode;
        }
    }

}
