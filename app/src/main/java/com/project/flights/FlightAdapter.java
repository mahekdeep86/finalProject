package com.project.flights;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.project.R;

import java.util.ArrayList;

/**
 * This is the listview adapter for the flights data
 */
public class FlightAdapter extends BaseAdapter {
    private ArrayList<Flight> flights;
    private Context context;
    private boolean saved;


    public FlightAdapter(Context context, boolean saved) {
        this.context = context;
        this.flights = new ArrayList<>();
        this.saved = saved;
    }

    @Override
    public int getCount() {
        return flights.size();
    }

    @Override
    public Object getItem(int position) {
        return flights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // if convertView is null inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_flight, parent, false);
        }
        // get current flight to be displayed
        final Flight currentFlight = (Flight) getItem(position);

        //initialize text views to display flights data
        TextView iataNumberTextView = convertView.findViewById(R.id.iataNumberTextView);
        TextView icaoNumberTextView = convertView.findViewById(R.id.icaoNumberTextView);
        TextView numberTextView = convertView.findViewById(R.id.numberTextView);
        final Button deleteButton = convertView.findViewById(R.id.deleteButton);
        if (saved){
            deleteButton.setVisibility(View.VISIBLE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = FlightContract.FlightEntry.FLIGHT_OFFLINE_ID + " = ?";
                //specify the id of the item to be deleted
                String[] selectionArgs = { currentFlight.getFlightOfflineId() + "" };
                // Execute the delete statement
                int deletedRows = new FlightDatabaseController(context).getReadableDatabase().delete(FlightContract.FlightEntry.TABLE_NAME, selection, selectionArgs);
                flights.remove(position);
                notifyDataSetChanged();
            }
        });

        //display the information of the current item
        iataNumberTextView.setText("IATA Number: " + currentFlight.getIataNumber());
        icaoNumberTextView.setText("ICAO Number: " + currentFlight.getIcaoNumber());
        numberTextView.setText("Number: " + currentFlight.getNumber());

        // returns the view for the current row
        return convertView;
    }
}
