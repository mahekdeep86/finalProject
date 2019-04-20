package com.project.flights;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.R;

public class FlightMoreDetailActivity extends AppCompatActivity {
    TextView iataNumberTextView;
    TextView icaoNumberTextView;
    TextView numberTextView;
    TextView locationTextView;
    TextView speedTextView;
    TextView altitudeTextView;
    TextView statusTextView;
    Toolbar toolbar;
    Button savedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_more_detail);
        iataNumberTextView = findViewById(R.id.iataNumberTextView);
        icaoNumberTextView = findViewById(R.id.icaoNumberTextView);
        numberTextView = findViewById(R.id.numberTextView);
        locationTextView = findViewById(R.id.locationTextView);
        speedTextView = findViewById(R.id.speedTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        statusTextView = findViewById(R.id.statusTextView);
        savedButton = findViewById(R.id.saveButton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Flight");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        Flight currentFlight = null;
        if (bundle != null){
            iataNumberTextView.setText("IATA Number: " + bundle.getString("iataNumber"));
            icaoNumberTextView.setText("ICAO Number: " + bundle.getString("icaoNumber"));
            numberTextView.setText("Number: " + bundle.getString("number"));
            locationTextView.setText("Location: Lat" + bundle.getDouble("latitude") + ", Log" + bundle.getDouble("longitude"));
            speedTextView.setText("Speed: " + String.valueOf(bundle.getDouble("speed")));
            altitudeTextView.setText("Altitude: " + String.valueOf(bundle.getDouble("altitude")));
            statusTextView.setText("Status: " + bundle.getString("status"));
            currentFlight = new Flight(bundle.getString("iataNumber"), bundle.getString("icaoNumber"), bundle.getString("number"),bundle.getDouble("latitude"),bundle.getDouble("longitude"),bundle.getDouble("speed"),bundle.getDouble("altitude"),bundle.getString("status"));
        }
        final Flight finalCurrentFlight = currentFlight;
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the database
                SQLiteDatabase db = new FlightDatabaseController(FlightMoreDetailActivity.this).getWritableDatabase();
                //create a ContentValues to hold the values
                ContentValues values = new ContentValues();

                values.put(FlightContract.FlightEntry.COLUMN_IATA_NUMBER, finalCurrentFlight.getIataNumber());
                values.put(FlightContract.FlightEntry.COLUMN_ICAO_NUMBER, finalCurrentFlight.getIcaoNumber());
                values.put(FlightContract.FlightEntry.COLUMN_NUMBER, finalCurrentFlight.getNumber());
                values.put(FlightContract.FlightEntry.COLUMN_LAT, finalCurrentFlight.getLatitude());
                values.put(FlightContract.FlightEntry.COLUMN_LAG, finalCurrentFlight.getLongitude());
                values.put(FlightContract.FlightEntry.COLUMN_ALTITUDE, finalCurrentFlight.getAltitude());
                values.put(FlightContract.FlightEntry.COLUMN_SPEED, finalCurrentFlight.getSpeed());
                values.put(FlightContract.FlightEntry.COLUMN_STATUS, finalCurrentFlight.getStatus());


                //insert the flight record to the database
                long newRowId = db.insert(FlightContract.FlightEntry.TABLE_NAME, null, values);
                Toast.makeText(FlightMoreDetailActivity.this, "The flight has been added", Toast.LENGTH_LONG).show();
            }
        });
    }
}
