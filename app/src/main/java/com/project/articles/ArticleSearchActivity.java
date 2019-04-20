package com.project.articles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.project.R;

/**
 * Activity for article search which contains a field to enter a query and a button to trigger search
 */
public class ArticleSearchActivity extends AppCompatActivity {
    private static final String ONLINE_ARTICLES_FRAGMENT = "ONLINE_ARTICLES_FRAGMENT";
    private static final String OFFLINE_ARTICLES_FRAGMENT = "OFFLINE_ARTICLES_FRAGMENT";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search_activitity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadFragment(new OfflineArticlesFragment(), OFFLINE_ARTICLES_FRAGMENT);
    }

    /**
     * This method loads the fragment passed in it
     * @param fragment
     * @param tag
     */
    private void loadFragment(Fragment fragment, String tag) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, tag);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Sets the title of the fragment
     * @param title
     */
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_articles_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_offline_articles:
                loadFragment(new OfflineArticlesFragment(), OFFLINE_ARTICLES_FRAGMENT);
                return true;
            case R.id.item_online_articles:
                loadFragment(new OnlineArticlesFragment(), ONLINE_ARTICLES_FRAGMENT);
                return true;
            case R.id.item_help:
                HelpDialog dialog = new HelpDialog(this);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
