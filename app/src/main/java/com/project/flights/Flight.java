package com.project.flights;

/**
 * This class models a Flight
 */

public class Flight {
    //declaring variables
    private int flightOfflineId;
    private String iataNumber;
    private String icaoNumber;
    private String number;
    private double latitude;
    private double longitude;
    private double altitude;
    private double speed;
    private String status;

    /**
     * Class constructor
     * @param iataNumber IATA number of the flight
     * @param icaoNumber ICAO number of the flight
     * @param number the number of the flight
     * @param latitude the current latitude of the flight
     * @param longitude the current longitude of the flight
     * @param altitude the altitude of the flight
     * @param speed the speed of the flight
     * @param status the status of the flight
     */
    public Flight(String iataNumber, String icaoNumber, String number, double latitude, double longitude, double altitude, double speed, String status) {
        this.iataNumber = iataNumber;
        this.icaoNumber = icaoNumber;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speed = speed;
        this.status = status;
        this.flightOfflineId = 0;
    }

    public Flight(int flightOfflineId, String iataNumber, String icaoNumber, String number, double latitude, double longitude, double altitude, double speed, String status) {
        this.iataNumber = iataNumber;
        this.icaoNumber = icaoNumber;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speed = speed;
        this.status = status;
        this.flightOfflineId = flightOfflineId;
    }

    /**
     * This function returns the flight id
     * @return
     */
    public int getFlightOfflineId() {
        return flightOfflineId;
    }

    /**
     * This function returns the iata number of the flight
     * @return
     */
    public String getIataNumber() {
        return iataNumber;
    }

    /**
     * This method returns the icao number of the flight
     * @return
     */
    public String getIcaoNumber() {
        return icaoNumber;
    }

    /**
     * This function returns the number of the flight
     * @return
     */
    public String getNumber() {
        return number;
    }

    /**
     * This function returns the latitude of the flight
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * This method returns the longitude of the flight
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * This function returns the altitude of the flight
     * @return
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * This method returns the speed of the flight
     * @return
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * This method returns the status of the flight
     * @return
     */
    public String getStatus() {
        return status;
    }
}
