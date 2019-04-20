package com.project.articles;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.articles.ArticleContract.ArticleEntry;

/**
 * References:
 * https://developer.android.com/training/data-storage/sqlite
 */
public class ArticleDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Articles.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
                    ArticleEntry.ID + " TEXT PRIMARY KEY," +
                    ArticleEntry.COLUMN_NAME_WEB_URL + " TEXT," +
                    ArticleEntry.COLUMN_NAME_SNIPPET + " TEXT," +
                    ArticleEntry.COLUMN_NAME_LEAD_IMAGE_URL + " TEXT," +
                    ArticleEntry.COLUMN_NAME_HEADLINE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME;

    /**
     * Class constructor which creates a new ArticleDbHelper object
     * @param context
     */
    public ArticleDbHelper(Context context) {
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
