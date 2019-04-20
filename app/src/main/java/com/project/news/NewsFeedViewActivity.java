package com.project.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsFeedViewActivity extends AppCompatActivity {
    TextView fullTitleTv;
    Toolbar toolbar;
    ImageView mainImageView;
    TextView sectionTitleTV;
    TextView publishDateTv;
    TextView urlTv;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_view);
        fullTitleTv = findViewById(R.id.fullTitleTv);
        mainImageView = findViewById(R.id.mainImageView);
        sectionTitleTV = findViewById(R.id.sectionTitleTV);
        publishDateTv = findViewById(R.id.publishDateTv);
        urlTv = findViewById(R.id.urlTv);
        saveButton = findViewById(R.id.saveButton);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String url = "";
        String title;
        String fullTitle;
        String sectionTitle;
        String publishedOn;
        String mainImage;
        DownLoadImageAsyncTask imageAsyncTask = new DownLoadImageAsyncTask();
        if (bundle != null) {
            mainImage = bundle.getString("mainImage");
            imageAsyncTask.execute(mainImage);
            url = bundle.getString("url");
            urlTv.setText(url);
            title = bundle.getString("title");
            fullTitle = bundle.getString("fullTitle");
            fullTitleTv.setText(fullTitle);
            sectionTitle = bundle.getString("sectionTitle");
            sectionTitleTV.setText("Section: " + sectionTitle);
            publishedOn = bundle.getString("publishedOn");
            publishDateTv.setText("Published On: " + publishedOn);
        }
        final String finalUrl = url;
        urlTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsFeedViewActivity.this, NewsArticleViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", finalUrl);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public class DownLoadImageAsyncTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            Bitmap bmp;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mainImageView.setImageBitmap(bitmap);
        }
    }
}
