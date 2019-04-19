package com.project.dictionary;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper helper for definitions database
 */
public class SavedDefinitionsDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DefinitionsDB.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DefinitionContract.DefinitionEntry.TABLE_NAME + " (" +
                    DefinitionContract.DefinitionEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DefinitionContract.DefinitionEntry.COLUMN_HEAD_WORD + " TEXT," +
                    DefinitionContract.DefinitionEntry.COLUMN_PRONUNCIATION + " TEXT," +
                    DefinitionContract.DefinitionEntry.COLUMN_FUNCTIONAL + " TEXT," +
                    DefinitionContract.DefinitionEntry.COLUMN_DEFINITIONS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DefinitionContract.DefinitionEntry.TABLE_NAME;

    public SavedDefinitionsDatabaseHelper(Context context) {
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
