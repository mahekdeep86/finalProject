package com.project.dictionary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.project.R;
import com.project.flights.DialogHelpInformation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DictionaryActivity extends AppCompatActivity {
    private static final String DICTIONARY_SHARED_PREFERENCES = "DICTIONARY_SHARED_PREFERENCES";
    private static final String SEARCH_TERM = "SEARCH_TERM";
    SharedPreferences searchTermsSharedPreferences;
    EditText queryEditText;
    Button searchButton;
    ListView listViewArticles;
    AdapterDefinitions adapterDefinitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        queryEditText = findViewById(R.id.queryEditText);
        searchButton = findViewById(R.id.searchButton);
        listViewArticles = findViewById(R.id.listViewArticles);
        final View parentLayout = findViewById(android.R.id.content);
        adapterDefinitions = new AdapterDefinitions(this);
        searchTermsSharedPreferences = getSharedPreferences(DICTIONARY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        listViewArticles.setAdapter(adapterDefinitions);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!queryEditText.getText().toString().equalsIgnoreCase("")) {
                    getDefinitions(queryEditText.getText().toString());
                    SharedPreferences.Editor editor = searchTermsSharedPreferences.edit();
                    editor.putString(SEARCH_TERM, queryEditText.getText().toString());
                    editor.apply();
                } else {
                    Snackbar.make(parentLayout, "Enter the search term first", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        initToolbar();
        readPreviousSearchTerm();
    }

    /**
     * Intialize the toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Merriam Webster Dictionary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This method checks whether user has entered previous term searched
     */
    private void readPreviousSearchTerm(){
        String storedTerm = searchTermsSharedPreferences.getString(SEARCH_TERM, "");
        if (!storedTerm.equalsIgnoreCase("")) {
            queryEditText.setText(storedTerm);
            getDefinitions(storedTerm);
        }
    }

    /**
     * This method searches for the term entered by the user
     * @param query
     */
    private void getDefinitions(String query) {
        String searchWord = "";
        try {
            searchWord = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //url endpoint
        String myUrl = "https://www.dictionaryapi.com/api/v1/references/sd3/xml/" + searchWord + "?key=4556541c-b8ed-4674-9620-b6cba447184f";
        AsyncRetrieve asyncRetrieve = new AsyncRetrieve(this, myUrl, new AsyncRetrieve.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<Definition> output) {
                adapterDefinitions.addItems(output);
                adapterDefinitions.notifyDataSetChanged();
            }
        });
        asyncRetrieve.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_help:
                Help help= new Help(this);
                help.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
