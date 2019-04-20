package com.project.flights;


import android.provider.BaseColumns;

/**
 * This class contains the column names for Flight sqlite database
 */
public class FlightContract {
    private FlightContract(){

    }
    public static class FlightEntry implements BaseColumns {
        public static final String TABLE_NAME = "savedflights";
        public static final String FLIGHT_OFFLINE_ID = "flightOfflineId";
        public static final String COLUMN_IATA_NUMBER = "iataNumber";
        public static final String COLUMN_ICAO_NUMBER = "icaoNumber";
        public static final String COLUMN_NUMBER = "colnumber";
        public static final String COLUMN_LAT = "latitude";
        public static final String COLUMN_LAG = "longitude";
        public static final String COLUMN_ALTITUDE = "altitude";
        public static final String COLUMN_SPEED = "speed";
        public static final String COLUMN_STATUS = "status";
    }
}
