package com.project.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * This activity contains a webview to load the news article url passed to it
 */
public class NewsArticleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView myWebView = new WebView(this);
        setContentView(myWebView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String url = bundle.getString("url");
            myWebView.loadUrl(url);
        }
    }
}
