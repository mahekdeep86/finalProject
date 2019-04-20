package com.project.articles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.project.R;

public class ArticleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);
        WebView myWebView = findViewById(R.id.webview);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String url = bundle.getString("webUrl");
            myWebView.loadUrl(url);
        }
    }
}
