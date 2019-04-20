package com.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;


import com.project.articles.ArticleSearchActivity;
import com.project.dictionary.DictionaryActivity;
import com.project.flights.FlightActivity;
import com.project.news.NewsFeedActivity;

/**
 * The main activity
 */
public class MainActivity extends AppCompatActivity {
    RelativeLayout dictionaryActivity;
    RelativeLayout newsFeedActivity;
    RelativeLayout flightActivity;
    RelativeLayout articleSearchActivity;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dictionaryActivity = findViewById(R.id.dictionary_activity);
        newsFeedActivity = findViewById(R.id.news_feed_activity);
        flightActivity = findViewById(R.id.flight_activity);
        articleSearchActivity = findViewById(R.id.article_search_activity);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        dictionaryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DictionaryActivity.class);
                startActivity(intent);
            }
        });
        newsFeedActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsFeedActivity.class);
                startActivity(intent);
            }
        });
        flightActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlightActivity.class);
                startActivity(intent);
            }
        });
        articleSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArticleSearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
