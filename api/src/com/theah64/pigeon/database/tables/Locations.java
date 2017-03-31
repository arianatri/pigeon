package com.theah64.pigeon.database.tables;

import com.theah64.pigeon.database.Connection;
import com.theah64.pigeon.models.Location;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Created by theapache64 on 31/3/17.
 */
public class Locations extends BaseTable<Location> {

    private static final Locations instance = new Locations();

    private Locations() {
        super("locations");
    }

    public static Locations getInstance() {
        return instance;
    }

    @Override
    public void add(Location location) throws SQLException {
        String errorMsg;
        final String query = "INSERT INTO locations (trip_id, lat, lon, speed, device_time) VALUES (?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, location.getTripId());
            ps.setString(2, location.getLat());
            ps.setString(3, location.getLon());
            ps.setString(4, location.getSpeed());
            ps.setString(5, location.getDeviceTime());
            errorMsg = ps.executeUpdate() == 1 ? null : "Failed to add location";
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            errorMsg = e.getMessage();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        super.manageError(errorMsg);
    }
}
