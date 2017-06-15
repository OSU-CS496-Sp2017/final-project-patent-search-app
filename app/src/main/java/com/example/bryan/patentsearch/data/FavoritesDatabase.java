package com.example.bryan.patentsearch.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bryan.patentsearch.utils.PatentsViewUtils;

import java.util.ArrayList;

/**
 * Created by tanner on 6/14/17.
 */

public class FavoritesDatabase {

    private FavoritesDatabaseHelper mFavoritesDatabaseHelper;

    public FavoritesDatabase(Context context) {
        mFavoritesDatabaseHelper = new FavoritesDatabaseHelper(context);
    }

    public boolean contains(String patentId) {
        final Cursor cursor = mFavoritesDatabaseHelper.getReadableDatabase().query(
                FavoritesDatabaseContract.FavoritePatents.TABLE_NAME,
                null,
                FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_ID + " = ?",
                new String[] { patentId },
                null,
                null,
                null
        );

        return cursor.moveToNext();
    }

    public void insert(PatentsViewUtils.SearchResult searchResult) {
        ContentValues values = new ContentValues();
        values.put(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_ID, searchResult.patentId);
        values.put(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_TITLE, searchResult.patentTitle);
        values.put(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_ABSTRACT, searchResult.patentAbstract);
        values.put(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_DATE, searchResult.patentDate);

        mFavoritesDatabaseHelper.getWritableDatabase().insert(
                FavoritesDatabaseContract.FavoritePatents.TABLE_NAME, null, values);
    }

    public void delete(String patentId) {
        mFavoritesDatabaseHelper.getWritableDatabase().delete(
                FavoritesDatabaseContract.FavoritePatents.TABLE_NAME,
                FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_ID + " = ?",
                new String[] { patentId }
        );
    }

    public ArrayList<PatentsViewUtils.SearchResult> getAll() {
        final Cursor cursor = mFavoritesDatabaseHelper.getReadableDatabase().query(
                FavoritesDatabaseContract.FavoritePatents.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        ArrayList<PatentsViewUtils.SearchResult> allFavorites = new ArrayList<>();
        while(cursor.moveToNext()) {
            final PatentsViewUtils.SearchResult res = new PatentsViewUtils.SearchResult();
            res.patentId = cursor.getString(cursor.getColumnIndex(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_ID));
            res.patentTitle = cursor.getString(cursor.getColumnIndex(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_TITLE));
            res.patentAbstract = cursor.getString(cursor.getColumnIndex(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_ABSTRACT));
            res.patentDate = cursor.getString(cursor.getColumnIndex(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_DATE));

            allFavorites.add(res);
        }

        return allFavorites;
    }

}
