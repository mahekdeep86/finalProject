package com.project.articles;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineArticlesFragment extends Fragment implements ArticleSearchTask.ArticleResponse{
    private static final String API_KEY = "DOHsw0YlNM0MlqZu4pqcCVKxEAzcKQOn";
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String PREF_QUERY = "query";
    ListView articlesListView;
    ProgressBar mProgressBar;
    ArticlesAdapter mArticlesAdapter;
    SharedPreferences sharedPreferences;
    SearchView searchView;

    public OnlineArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_online_articles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        articlesListView = view.findViewById(R.id.listViewArticles);
        mProgressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        mProgressBar.setVisibility(View.GONE);
        mArticlesAdapter = new ArticlesAdapter(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        articlesListView.setAdapter(mArticlesAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREF_QUERY, s);
                    editor.apply();
                    searchArticles(s);
                } else {
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Enter article topic", Snackbar.LENGTH_LONG);
                    snackBar.show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
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
        readSharedPreferences();
    }

    /**
     * This method reads the last search term
     */
    private void readSharedPreferences() {
        String storedQuery = sharedPreferences.getString(PREF_QUERY, null);
        if (storedQuery != null) {
            searchView.setQuery(storedQuery, false);
            searchArticles(storedQuery);
        }
    }

    private void searchArticles(String query) {

        try {
            query = URLEncoder.encode(query, "UTF-8");
            String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + query + "&api-key=" + API_KEY;
            new ArticleSearchTask(this).execute(url);
            mProgressBar.setVisibility(View.VISIBLE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(ArrayList<Article> output) {
        mArticlesAdapter.setArticles(output);
        mArticlesAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title bar
        ((ArticleSearchActivity) getActivity())
                .setActionBarTitle("Online Articles");
    }
}
