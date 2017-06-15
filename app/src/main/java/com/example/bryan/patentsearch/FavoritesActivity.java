package com.example.bryan.patentsearch;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bryan.patentsearch.data.FavoritesDatabaseContract;
import com.example.bryan.patentsearch.data.FavoritesDatabaseHelper;
import com.example.bryan.patentsearch.utils.PatentsViewUtils;

import java.util.ArrayList;

/**
 * Created by tanner on 6/14/17.
 */

public class FavoritesActivity extends AppCompatActivity
        implements PatentsViewAdapter.OnSearchResultClickListener, LoaderManager.LoaderCallbacks<ArrayList<PatentsViewUtils.SearchResult>> {

    private static final String TAG = FavoritesActivity.class.getSimpleName();

    private RecyclerView mSearchResultsRV;
    private PatentsViewAdapter mPatentsViewAdapter;
    private FavoritesDatabaseHelper favoritesDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);
        mSearchResultsRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mPatentsViewAdapter = new PatentsViewAdapter(this);
        mSearchResultsRV.setAdapter(mPatentsViewAdapter);

        favoritesDatabaseHelper = new FavoritesDatabaseHelper(this);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onSearchResultClick(PatentsViewUtils.SearchResult searchResult) {
        Intent intent = new Intent(this, PatentsViewItemDetailActivity.class);
        intent.putExtra(PatentsViewUtils.SearchResult.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }

    @Override
    public Loader<ArrayList<PatentsViewUtils.SearchResult>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<PatentsViewUtils.SearchResult>>(this) {

            private ArrayList<PatentsViewUtils.SearchResult> mFavorites;

            @Override
            protected void onStartLoading() {
                if(mFavorites != null) {
                    deliverResult(mFavorites);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<PatentsViewUtils.SearchResult> loadInBackground() {
                final Cursor cursor = favoritesDatabaseHelper.getReadableDatabase().query(
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

                    allFavorites.add(res);
                }

                mFavorites = allFavorites;

                return mFavorites;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PatentsViewUtils.SearchResult>> loader, ArrayList<PatentsViewUtils.SearchResult> data) {
        mPatentsViewAdapter.updateSearchResults(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PatentsViewUtils.SearchResult>> loader) {
        // Nothing necessary to do here
    }
}
