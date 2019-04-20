package com.project.news;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.project.R;

import java.util.ArrayList;

/**
 * This class is a list adapter for the news items list
 */
public class NewsAdapter extends BaseAdapter {
    private ArrayList<NewsArticle> newsArticleList;
    private Context context;
    private boolean online;


    public NewsAdapter(Context context, ArrayList<NewsArticle> newsArticleList, boolean online) {
        this.context = context;
        this.newsArticleList = newsArticleList;
        this.online = online;
    }

    @Override
    public int getCount() {
        return newsArticleList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsArticleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNewsArticleList(ArrayList<NewsArticle> newsArticleList) {
        if (newsArticleList == null){
            this.newsArticleList = new ArrayList<>();
        }else {
            this.newsArticleList = newsArticleList;
        }
        notifyDataSetChanged();
    }

    public void addNewsItem(NewsArticle newsArticle){
        this.newsArticleList.add(newsArticle);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // if convertView is null inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_news, parent, false);
        }
        // get current news item to be displayed
        final NewsArticle newsItemArticle = (NewsArticle) getItem(position);


        //initialize text view for the full title
        TextView fullTitleTextView = convertView.findViewById(R.id.fullTitleTextView);

        ImageButton saveButton = convertView.findViewById(R.id.saveButton);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        if (online){
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        } else {
            deleteButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.GONE);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewsArticle(newsItemArticle, position);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNewsArticle(newsItemArticle, position);
            }
        });

        //display the full time of the current item
        if (newsItemArticle.getFullTitle().equalsIgnoreCase("")){
            fullTitleTextView.setText(newsItemArticle.getTitle());
        } else {
            fullTitleTextView.setText(newsItemArticle.getFullTitle());
        }
        // returns the view for the current row
        return convertView;
    }

    /**
     * This function  saves the news article to mysqlite database
     * @param newsItemArticle
     * @param position
     */
    private void saveNewsArticle(NewsArticle newsItemArticle, int position){
        NewsArticleDatabase articleDatabase = new NewsArticleDatabase(context);
        //get the database
        SQLiteDatabase db = articleDatabase.getWritableDatabase();
        //create a ContentValues to hold the values
        ContentValues values = new ContentValues();
        values.put(NewsArticle.UUID, newsItemArticle.getUuid());
        values.put(NewsArticle.URL, newsItemArticle.getUrl());
        values.put(NewsArticle.TITLE, newsItemArticle.getTitle());
        values.put(NewsArticle.FULL_TITLE, newsItemArticle.getFullTitle());
        values.put(NewsArticle.SECTION_TITLE, newsItemArticle.getSectionTitle());
        values.put(NewsArticle.PUBLISHED_ON, newsItemArticle.getPublishedOn());
        values.put(NewsArticle.MAIN_IMAGE, newsItemArticle.getMainImage());

        //insert the article record to the database
        long newRowId = db.insert(NewsArticle.TABLE_NAME, null, values);
        Toast.makeText(context, "The news article has been saved", Toast.LENGTH_LONG).show();
    }

    /**
     * This function deletes the news article to mysqlite database
     * @param newsItemArticle
     * @param position
     */
    private void deleteNewsArticle(NewsArticle newsItemArticle, int position){
        NewsArticleDatabase articleDatabase = new NewsArticleDatabase(context);
        String selection = NewsArticle.UUID + " = ?";
        //specify the id of the item to be deleted
        String[] selectionArgs = { newsItemArticle.getUuid() };
        // Execute the delete statement
        int deletedRows = articleDatabase.getReadableDatabase().delete(NewsArticle.TABLE_NAME, selection, selectionArgs);
        newsArticleList.remove(position);
        Toast.makeText(context, deletedRows + " item has been deleted successfully", Toast.LENGTH_LONG).show();
        notifyDataSetChanged();
    }
}
