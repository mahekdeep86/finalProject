package com.project.articles;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This activity displays more information about an article
 */
public class ArticleExpandViewActivity extends AppCompatActivity {
    ImageView articleImageView;
    TextView headlineTextView;
    TextView snippetTextView;
    TextView urlTextView;
    Button saveButton;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_expand_view);
        articleImageView = findViewById(R.id.articleImageView);
        headlineTextView = findViewById(R.id.headlineTextView);
        snippetTextView = findViewById(R.id.snippetTextView);
        urlTextView = findViewById(R.id.urlTextView);
        saveButton = findViewById(R.id.saveButton);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        urlTextView.setMovementMethod(LinkMovementMethod.getInstance());
        Bundle bundle = getIntent().getExtras();
        String id = "";
        String webUrl = "";
        String snippet = "";
        String imageUrl = "";
        String headline = "";
        if (bundle != null) {
            id = bundle.getString("id");
            webUrl = bundle.getString("webUrl");
            snippet = bundle.getString("snippet");
            imageUrl = bundle.getString("imageUrl");
            headline = bundle.getString("headline");
            headlineTextView.setText(headline);
            snippetTextView.setText(snippet);
            urlTextView.setText(webUrl);
            new LoadImageAsyncTask().execute(imageUrl);
        }
        final String finalWebUrl = webUrl;
        urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleExpandViewActivity.this, ArticleViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("webUrl", finalWebUrl);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        final ArticleDbHelper dbHelper = new ArticleDbHelper(this);
        final String finalWebUrl1 = webUrl;
        final String finalSnippet = snippet;
        final String finalImageUrl = imageUrl;
        final String finalHeadline = headline;
        final String finalId = id;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //create a ContentValues to hold the values
                ContentValues values = new ContentValues();
                values.put(ArticleContract.ArticleEntry.ID, finalId);
                values.put(ArticleContract.ArticleEntry.COLUMN_NAME_WEB_URL, finalWebUrl1);
                values.put(ArticleContract.ArticleEntry.COLUMN_NAME_SNIPPET, finalSnippet);
                values.put(ArticleContract.ArticleEntry.COLUMN_NAME_LEAD_IMAGE_URL, finalImageUrl);
                values.put(ArticleContract.ArticleEntry.COLUMN_NAME_HEADLINE, finalHeadline);

                //insert the article record to the database
                long newRowId = db.insert(ArticleContract.ArticleEntry.TABLE_NAME, null, values);
                Toast.makeText(ArticleExpandViewActivity.this, "Article has been saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    //load article image AsyncTask
    public class LoadImageAsyncTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url;
            Bitmap bmp;
            try {
                String imageUrl = "https://www.nytimes.com/" + strings[0];
                url = new URL(imageUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            articleImageView.setImageBitmap(bitmap);
        }
    }
}
