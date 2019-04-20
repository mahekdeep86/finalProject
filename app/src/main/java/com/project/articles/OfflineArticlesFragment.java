package com.project.articles;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * References:
 * https://developer.android.com/training/data-storage/sqlite
 */
public class OfflineArticlesFragment extends Fragment {
    ListView articlesListView;
    ArticlesAdapter mArticlesAdapter;
    ArticleDbHelper dbHelper;

    public OfflineArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offline_articles_frament, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        articlesListView = view.findViewById(R.id.listViewArticles);
        mArticlesAdapter = new ArticlesAdapter(getActivity());
        dbHelper = new ArticleDbHelper(getActivity());

        articlesListView.setAdapter(mArticlesAdapter);
        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article selectedArticle = (Article) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ArticleExpandViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", selectedArticle.getId());
                bundle.putString("headline", selectedArticle.getHeadline());
                bundle.putString("webUrl", selectedArticle.getWebUrl());
                bundle.putString("snippet", selectedArticle.getSnippet());
                bundle.putString("imageUrl", selectedArticle.getImageUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        loadArticles();
    }

    private void loadArticles() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                ArticleContract.ArticleEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Article> articles = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(cursor.getColumnIndexOrThrow(ArticleContract.ArticleEntry.ID));
            String webUrl = cursor.getString(cursor.getColumnIndexOrThrow(ArticleContract.ArticleEntry.COLUMN_NAME_WEB_URL));
            String snippet = cursor.getString(cursor.getColumnIndexOrThrow(ArticleContract.ArticleEntry.COLUMN_NAME_SNIPPET));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(ArticleContract.ArticleEntry.COLUMN_NAME_LEAD_IMAGE_URL));
            String headline = cursor.getString(cursor.getColumnIndexOrThrow(ArticleContract.ArticleEntry.COLUMN_NAME_HEADLINE));
            Article newArticle = new Article(itemId, webUrl, headline, imageUrl, snippet);
            newArticle.setOffline(true);
            articles.add(newArticle);
        }
        mArticlesAdapter.setArticles(articles);
        mArticlesAdapter.notifyDataSetChanged();
        cursor.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title bar
        ((ArticleSearchActivity) getActivity())
                .setActionBarTitle("Saved Articles");
    }
}
