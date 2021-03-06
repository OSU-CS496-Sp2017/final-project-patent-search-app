package com.example.bryan.patentsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bryan.patentsearch.data.FavoritesDatabase;
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
    private FavoritesDatabase mFavoritesDatabase;

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

        mFavoritesDatabase = new FavoritesDatabase(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getSupportLoaderManager().getLoader(0) == null)
            getSupportLoaderManager().initLoader(0, null, this);
        else
            getSupportLoaderManager().restartLoader(0, null, this);
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
                mFavorites = mFavoritesDatabase.getAll();
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
