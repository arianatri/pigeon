package com.theah64.pigeon.database.tables;

import com.theah64.pigeon.models.Trip;

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
    public void add(Trip newInstance) throws SQLException {
        
    }
}
