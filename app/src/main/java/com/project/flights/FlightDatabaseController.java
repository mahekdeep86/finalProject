package com.project.flights;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.project.flights.FlightContract.FlightEntry;

/**
 * This class controls the flight sqlite database
 */
public class FlightDatabaseController extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Flights.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FlightEntry.TABLE_NAME + " (" +
                    FlightEntry.FLIGHT_OFFLINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FlightEntry.COLUMN_IATA_NUMBER + " TEXT," +
                    FlightEntry.COLUMN_ICAO_NUMBER + " TEXT," +
                    FlightEntry.COLUMN_NUMBER + " TEXT," +
                    FlightEntry.COLUMN_LAT + " DOUBLE," +
                    FlightEntry.COLUMN_LAG + " DOUBLE," +
                    FlightEntry.COLUMN_ALTITUDE + " DOUBLE," +
                    FlightEntry.COLUMN_SPEED + " DOUBLE," +
                    FlightEntry.COLUMN_STATUS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FlightEntry.TABLE_NAME;

    public FlightDatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }
}
