package com.example.bryan.patentsearch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tanner on 6/14/17.
 */

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_REPOS_TABLE =
                "CREATE TABLE " + FavoritesDatabaseContract.FavoritePatents.TABLE_NAME + " (" +
                        FavoritesDatabaseContract.FavoritePatents._ID + " INTEGER PRIMARY KEY, " +
                        FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_DESCRIPTION + " TEXT NOT NULL" +
                        ");";
        db.execSQL(SQL_CREATE_FAVORITE_REPOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesDatabaseContract.FavoritePatents.TABLE_NAME);
        onCreate(db);
    }

}