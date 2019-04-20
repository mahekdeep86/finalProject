package com.project.flights;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFlightsFragment extends Fragment {
    FlightAdapter mFlightAdapter;
    ListView listViewFlights;

    public SavedFlightsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_flights, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewFlights = view.findViewById(R.id.listViewFlights);
        mFlightAdapter = new FlightAdapter(getActivity(), true);
        listViewFlights.setAdapter(mFlightAdapter);
        listViewFlights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Flight clickedFlight = (Flight) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), FlightMoreDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("iataNumber", clickedFlight.getIataNumber());
                bundle.putString("icaoNumber", clickedFlight.getIcaoNumber());
                bundle.putString("number", clickedFlight.getNumber());
                bundle.putDouble("latitude", clickedFlight.getLatitude());
                bundle.putDouble("longitude", clickedFlight.getLongitude());
                bundle.putDouble("altitude", clickedFlight.getAltitude());
                bundle.putDouble("speed", clickedFlight.getSpeed());
                bundle.putString("status", clickedFlight.getStatus());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        loadSavedFlights();
    }

    private void loadSavedFlights() {
        SQLiteDatabase db = new FlightDatabaseController(getActivity()).getReadableDatabase();

        Cursor cursor = db.query(
                FlightContract.FlightEntry.TABLE_NAME,null,null,null,null,null,null
        );

        ArrayList<Flight> flights = new ArrayList<>();
        while(cursor.moveToNext()) {
            int flightOfflineId = cursor.getInt(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.FLIGHT_OFFLINE_ID));
            String iataNumber = cursor.getString(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.COLUMN_IATA_NUMBER));
            String icaoNumber = cursor.getString(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.COLUMN_ICAO_NUMBER));
            String number = cursor.getString(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.COLUMN_NUMBER));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.COLUMN_LAT));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.COLUMN_LAG));
            double altitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.COLUMN_ALTITUDE));
            double speed = cursor.getDouble(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.COLUMN_SPEED));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(FlightContract.FlightEntry.COLUMN_STATUS));
            Flight flight = new Flight(flightOfflineId, iataNumber, icaoNumber, number, latitude, longitude, altitude, speed, status);
            flights.add(flight);
        }
        mFlightAdapter.setFlights(flights);
        mFlightAdapter.notifyDataSetChanged();
        cursor.close();
    }
}
