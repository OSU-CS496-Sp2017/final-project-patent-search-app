package com.example.bryan.patentsearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bryan.patentsearch.utils.NetworkUtils;
import com.example.bryan.patentsearch.utils.PatentsViewUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private EditText mEditText;
    private Button mSearchButton;
    private TextView mLoadingErrorMessageTV;
    private RecyclerView mSearchResultsRV;
    private PatentsViewAdapter mPatentsViewAdapter;

    private String mSearchString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.et_search_box);
        mSearchButton = (Button) findViewById(R.id.btn_search);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

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
        String searchUrl = "";

        // We need to check if the user is searching for a patent number or a title
        int searchNumber;
        try{
            searchNumber = Integer.parseInt(searchString);
            searchUrl = "http://www.patentsview.org/api/patents/query?q={%22patent_number%22:%22" +
                    searchString +
                    "%22}";
        } catch (NumberFormatException e) {
            searchUrl = "http://www.patentsview.org/api/patents/query?q={%22patent_title%22:%22" +
                    searchString +
                    "%22}";
        }

        Log.d("MainActivity", "got search url: " + searchUrl);
        new PatentSearchTask().execute(searchUrl);

    }

    //@Override
    public void onSearchResultClick(PatentsViewUtils.SearchResult searchResult){
        Intent intent = new Intent(this, PatentsViewItemDetailActivity.class);
        intent.putExtra(PatentsViewUtils.SearchResult.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
