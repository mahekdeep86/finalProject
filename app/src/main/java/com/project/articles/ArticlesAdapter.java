package com.project.articles;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.R;

import java.util.ArrayList;

/**
 * Adapter for news articles
 */
public class ArticlesAdapter extends BaseAdapter {
    private ArrayList<Article> articles;
    private Context context;
    public ArticlesAdapter(Context context) {
        this.context = context;
        this.articles = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setArticles(ArrayList<Article> articles) {
        if (articles == null){
            this.articles = new ArrayList<>();
        }else {
            this.articles = articles;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // if convertView is null inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_article, parent, false);
        }
        // get current article item to be displayed
        final Article currentArticle = (Article) getItem(position);

        //initialize text view for the full title
        final TextView articleTitle = convertView.findViewById(R.id.articleTitle);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        if (currentArticle.isOffline()){
            deleteButton.setVisibility(View.VISIBLE);
        }else {
            deleteButton.setVisibility(View.GONE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDbHelper dbHelper = new ArticleDbHelper(context);
                String selection = ArticleContract.ArticleEntry.ID + " = ?";
                //specify the id of the item to be deleted
                String[] selectionArgs = { currentArticle.getId() };
                // Execute the delete statement
                int deletedRows = dbHelper.getReadableDatabase().delete(ArticleContract.ArticleEntry.TABLE_NAME, selection, selectionArgs);
                articles.remove(position);
                notifyDataSetChanged();
            }
        });

        //display the full time of the current item
        articleTitle.setText(currentArticle.getHeadline());

        // returns the view for the current row
        return convertView;
    }
}
