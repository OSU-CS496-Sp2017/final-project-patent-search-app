package com.example.bryan.patentsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bryan.patentsearch.utils.NetworkUtils;
import com.example.bryan.patentsearch.utils.PatentsViewUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements PatentsViewAdapter.OnSearchResultClickListener, LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SEARCH_URL_KEY = "patentsViewSearchURL";
    private static final int PATENTS_VIEW_SEARCH_LOADER_ID = 0;

    private EditText mEditText;
    private Button mSearchButton;
    private TextView mLoadingErrorMessageTV;
    private RecyclerView mSearchResultsRV;
    private PatentsViewAdapter mPatentsViewAdapter;
    private ProgressBar mLoadingIndicatorPB;


    private String mSearchString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.et_search_box);
        mSearchButton = (Button) findViewById(R.id.btn_search);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);
        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mPatentsViewAdapter = new PatentsViewAdapter(this);
        mSearchResultsRV.setAdapter(mPatentsViewAdapter);


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            String userText = mEditText.getText().toString();
            @Override
            public void onClick(View v) {
                doPatentSearch(userText);
            }
        });
    }

    private void doPatentSearch(String searchString){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String date = sharedPreferences.getString(getString(R.string.pref_date_sort_key), getString(R.string.pref_sort_default));
        //String number = sharedPreferences.getString(getString(R.string.pref_patent_sort_key), getString(R.string.pref_sort_default));
        String resultNumber = sharedPreferences.getString(getString(R.string.pref_result_sort_key), getString(R.string.pref_result_default));

        String url = PatentsViewUtils.buildPatentsViewURL(mEditText.getText().toString(), date, resultNumber);

        Log.d("MainActivity", "got search url: " + url);
        Bundle argsBundle = new Bundle();
        argsBundle.putString(SEARCH_URL_KEY, url);
        getSupportLoaderManager().restartLoader(PATENTS_VIEW_SEARCH_LOADER_ID, argsBundle, this);

    }

    //@Override
    public void onSearchResultClick(PatentsViewUtils.SearchResult searchResult){
        Intent intent = new Intent(this, PatentsViewItemDetailActivity.class);
        intent.putExtra(PatentsViewUtils.SearchResult.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mSearchResultsJSON;

            @Override
            protected void onStartLoading() {
                if (args != null) {
                    if (mSearchResultsJSON != null) {
                        Log.d(TAG, "AsyncTaskLoader delivering cached results");
                        deliverResult(mSearchResultsJSON);
                    } else {
                        mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }
            }

            @Override
            public String loadInBackground() {
                if (args != null) {
                    String patentsViewSearchUrl = args.getString(SEARCH_URL_KEY);
                    Log.d(TAG, "AsyncTaskLoader making network call: " + patentsViewSearchUrl);
                    String searchResults = null;
                    try {
                        searchResults = NetworkUtils.doHTTPGet(patentsViewSearchUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return searchResults;
                } else {
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mSearchResultsJSON = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "AsyncTaskLoader's onLoadFinished called");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
            ArrayList<PatentsViewUtils.SearchResult> searchResultsList = PatentsViewUtils.parsePatentsSearchResultsJSON(data);
            mPatentsViewAdapter.updateSearchResults(searchResultsList);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    /*
    public class PatentSearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String patentSearchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.doHTTPGet(patentSearchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                //mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                //mSearchResultsRV.setVisibility(View.VISIBLE);
                ArrayList<PatentsViewUtils.SearchResult> searchResultsList = PatentsViewUtils.parsePatentsSearchResultsJSON(s);
                //mGitHubSearchAdapter.updateSearchResults(searchResultsList);
            } else {
                //mSearchResultsRV.setVisibility(View.INVISIBLE);
                //mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
