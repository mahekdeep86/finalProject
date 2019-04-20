package com.project.flights;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.project.R;

public class FlightActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Flight Status tracker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadOnlineFlightFragment();
    }

    public void loadOnlineFlightFragment(){
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new OnlineFlightFragment(), "");
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * This method loads the saved flights
     */
    public void loadSavedFlightsFragment(){
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new SavedFlightsFragment(), "");
        //transaction.addToBackStack(null);
        transaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flight_checker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_saved_flights:
                loadSavedFlightsFragment();
                return true;
            case R.id.item_online_flights:
                loadOnlineFlightFragment();
                return true;
            case R.id.item_help:
                DialogHelpInformation dialogHelpInformation= new DialogHelpInformation(this);
                dialogHelpInformation.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
