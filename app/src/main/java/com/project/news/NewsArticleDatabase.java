package com.project.news;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsArticleDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "news.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsArticle.TABLE_NAME + " (" +
                    NewsArticle.UUID + " TEXT PRIMARY KEY," +
                    NewsArticle.URL + " TEXT," +
                    NewsArticle.TITLE + " TEXT," +
                    NewsArticle.FULL_TITLE + " TEXT," +
                    NewsArticle.SECTION_TITLE + " TEXT," +
                    NewsArticle.PUBLISHED_ON + " TEXT," +
                    NewsArticle.MAIN_IMAGE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsArticle.TABLE_NAME;

    public NewsArticleDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }
}
