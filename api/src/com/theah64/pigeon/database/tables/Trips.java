package com.theah64.pigeon.database.tables;

import com.theah64.pigeon.database.Connection;
import com.theah64.pigeon.models.Trip;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 31/3/17.
 */
public class Trips extends BaseTable<Trip> {

    private static final Trips instance = new Trips();

    private Trips() {
        super("trips");
    }

    public static Trips getInstance() {
        return instance;
    }


    @Override
    public String addv3(Trip trip) throws SQLException {
        String tripId = null;
        String errorMsg = null;
        final String query = "INSERT INTO trips (user_id) VALUES (?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, trip.getUserId());
            errorMsg = ps.executeUpdate() == 1 ? null : "Failed to add trip: QUERY";
            final ResultSet rs = ps.getGeneratedKeys();
            tripId = rs.getString(1);
            rs.close();
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
        manageError(errorMsg);
        if (tripId == null) {
            throw new InsertFailedException("Failed to add trip");
        }
        return tripId;
    }

}
