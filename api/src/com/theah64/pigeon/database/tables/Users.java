package com.theah64.pigeon.database.tables;

import com.theah64.pigeon.database.Connection;
import com.theah64.pigeon.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 21/1/17.
 */
public class Users extends BaseTable<User> {

    private static final Users instance = new Users();
    public static final String COLUMN_API_KEY = "api_key";
    public static final String COLUMN_FCM_ID = "fcm_id";
    public static final String COLUMN_DEVICE_HASH = "device_hash";
    public static final String COLUMN_IMEI = "imei";
    private static final String COLUMN_AS_HAS_FCM = "has_fcm";

    private Users() {
        super("users");
        System.out.println("Created user table instance");
    }

    public static Users getInstance() {
        return instance;
    }

    @Override
    public User get(String column, String value) {
        User user = null;
        final String query = String.format("SELECT id,name,fcm_id, api_key FROM users WHERE %s = ? AND is_active = 1 LIMIT 1;", column);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, value);
            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                final String id = rs.getString(COLUMN_ID);
                final String name = rs.getString(COLUMN_NAME);
                final String fcmId = rs.getString(COLUMN_FCM_ID);
                final String apiKey = rs.getString(COLUMN_API_KEY);
                user = new User(id, name, null, null, fcmId, apiKey, fcmId != null, true);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public String addv3(User user) throws InsertFailedException {
        String userId = null;
        final String query = "INSERT INTO users (name,imei,device_hash,fcm_id,api_key) VALUES (?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getIMEI());
            ps.setString(3, user.getDeviceHash());
            ps.setString(4, user.getFCMId());
            ps.setString(5, user.getAPIKey());
            ps.executeUpdate();
            final ResultSet rs = ps.getGeneratedKeys();
            if (rs.first()) {
                userId = rs.getString(1);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userId;
    }

    @Override
    public List<User> getAll(String whereColumn, String whereColumnValue) {
        List<User> userList = null;
        final String query = String.format("SELECT id, name , imei , !ISNULL(fcm_id) AS has_fcm FROM users WHERE %s = ? ORDER BY id DESC", whereColumn);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, whereColumnValue);
            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                userList = new ArrayList<>();
                do {

                    final String name = rs.getString(COLUMN_NAME);
                    final String imei = rs.getString(COLUMN_IMEI);
                    final boolean hasFcm = rs.getBoolean(COLUMN_AS_HAS_FCM);

                    userList.add(new User(null, name, imei, null, null, null, hasFcm, true));
                } while (rs.next());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userList;

    }
}
