package com.project.flights;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineFlightFragment extends Fragment {
    private static final String FLIGHT_PREFERENCES = "FlightsPreferences";
    private static final String FLIGHT_QUERY = "FLIGHT_QUERY";
    ListView listViewFlights;
    EditText searchTermEditText;
    Button searchButton;
    ProgressBar mProgressBar;
    FlightAdapter mFlightAdapter;
    SharedPreferences flightsSharedPreferences;

    public OnlineFlightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_online_flight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewFlights = view.findViewById(R.id.listViewFlights);
        searchTermEditText = view.findViewById(R.id.query_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        mProgressBar = view.findViewById(R.id.simple_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mFlightAdapter = new FlightAdapter(getActivity(), false);
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
        flightsSharedPreferences = getActivity().getSharedPreferences(FLIGHT_PREFERENCES, Context.MODE_PRIVATE);
        String storedQuery = flightsSharedPreferences.getString(FLIGHT_QUERY, null);
        if (storedQuery != null) {
            searchTermEditText.setText(storedQuery);
            searchFlights(storedQuery);
        }
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(searchTermEditText.getText())) {
                    SharedPreferences.Editor editor = flightsSharedPreferences.edit();
                    editor.putString(FLIGHT_QUERY, searchTermEditText.getText().toString());
                    editor.apply();
                    searchFlights(searchTermEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Enter airport code", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * This method searches triggers online flight check
     * @param query
     */
    private void searchFlights(String query) {
        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Searching for " + query, Snackbar.LENGTH_LONG);
        snackBar.show();
        try {
            query = URLEncoder.encode(query, "UTF-8");
            String url = "http://aviation-edge.com/v2/public/flights?key=aab621-944f0b&arrIata=" + query;
            new FlightSearchTask().execute(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //this class implements online flight search task
    public class FlightSearchTask extends AsyncTask<String, Void, ArrayList<Flight>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Flight> doInBackground(String... strings) {
            URL url;
            ArrayList<Flight> results = new ArrayList<>();
            try {
                url = new URL(strings[0]);
                // Setup HttpURLConnection class to receive data from the api
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");

                //connection.setDoOutput(false);

                connection.connect();

                InputStream stream = connection.getInputStream();
                results.addAll(getFlightsInformation(stream));
                stream.close();
                return results;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<Flight> flights) {
            super.onPostExecute(flights);
            mProgressBar.setVisibility(View.GONE);
            mFlightAdapter.setFlights(flights);
            mFlightAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This method gets flights information from InputStream stream
     * @param inputStream
     * @return
     */
    private ArrayList<Flight> getFlightsInformation(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuffer buffer = new StringBuffer();
        ArrayList<Flight> data = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String result = buffer.toString();
            JSONArray jsonArray = new JSONArray(result);
            data = new ArrayList<>();
            for (int i = 0; i < buffer.length(); i++) {
                Flight flight;
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject flightJSONObject = jsonObject.getJSONObject("flight");
                String iataNumber = flightJSONObject.getString("iataNumber");
                String icaoNumber = flightJSONObject.getString("icaoNumber");
                double speed = jsonObject.getJSONObject("speed").getDouble("horizontal");
                double latitude = jsonObject.getJSONObject("geography").getDouble("latitude");
                double longitude = jsonObject.getJSONObject("geography").getDouble("longitude");
                double altitude = jsonObject.getJSONObject("geography").getDouble("altitude");
                String status = jsonObject.getString("status");
                String number = flightJSONObject.getString("number");
                flight = new Flight(iataNumber, icaoNumber, number, latitude, longitude, altitude, speed, status);
                data.add(flight);
                Log.e("abc",flight.getIataNumber());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
