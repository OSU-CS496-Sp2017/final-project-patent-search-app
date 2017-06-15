package com.example.bryan.patentsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bryan.patentsearch.data.FavoritesDatabase;
import com.example.bryan.patentsearch.utils.PatentsViewUtils;

/**
 * Created by Bryan on 6/11/2017.
 */

public class PatentsViewItemDetailActivity extends AppCompatActivity {

    private static final String TAG = PatentsViewItemDetailActivity.class.getSimpleName();

    private PatentsViewUtils.SearchResult mSearchResult;
    private MenuItem mFavoriteButton;
    private TextView mSearchResultNumber;
    private TextView mSearchResultTitle;
    private TextView mSearchResultAbstract;
    private FavoritesDatabase mFavoritesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultNumber = (TextView)findViewById(R.id.tv_search_result_number);
        mSearchResultTitle =(TextView)findViewById(R.id.tv_search_result_title);
        mSearchResultAbstract = (TextView) findViewById(R.id.tv_search_result_abstract);

        Intent intent = getIntent();
        mSearchResult = (PatentsViewUtils.SearchResult)
                intent.getSerializableExtra(PatentsViewUtils.SearchResult.EXTRA_SEARCH_RESULT);
        mSearchResultNumber.setText(mSearchResult.patentId);
        mSearchResultTitle.setText(mSearchResult.patentTitle);
        mSearchResultAbstract.setText(mSearchResult.patentAbstract);

        mFavoritesDatabase = new FavoritesDatabase(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patent_view, menu);

        mFavoriteButton = menu.findItem(R.id.action_favorite);
        if(mFavoritesDatabase.contains(mSearchResult.patentId)) {
            mFavoriteButton.setIcon(android.R.drawable.btn_star_big_on);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_favorite:
                if(mFavoritesDatabase.contains(mSearchResult.patentId)) {
                    mFavoritesDatabase.delete(mSearchResult.patentId);
                    mFavoriteButton.setIcon(android.R.drawable.btn_star_big_off);
                    Log.d(TAG, "Removed favorite from database.");
                } else {
                    mFavoritesDatabase.insert(mSearchResult);
                    mFavoriteButton.setIcon(android.R.drawable.btn_star_big_on);
                    Log.d(TAG, "Inserted favorite into database.");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
