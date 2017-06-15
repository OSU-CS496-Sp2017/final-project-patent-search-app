package com.example.bryan.patentsearch.data;

import android.provider.BaseColumns;

/**
 * Created by tanner on 6/14/17.
 */

public class FavoritesDatabaseContract {

    private FavoritesDatabaseContract() {}

    public static class FavoritePatents implements BaseColumns {
        public static final String TABLE_NAME = "favoritePatents";
        public static final String COLUMN_PATENT_ID = "patentId";
        public static final String COLUMN_PATENT_DESCRIPTION = "description";
    }

}
