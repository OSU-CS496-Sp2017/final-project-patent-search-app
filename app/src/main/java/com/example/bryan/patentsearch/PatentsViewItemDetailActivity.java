package com.example.bryan.patentsearch;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bryan.patentsearch.data.FavoritesDatabaseContract;
import com.example.bryan.patentsearch.data.FavoritesDatabaseHelper;
import com.example.bryan.patentsearch.utils.PatentsViewUtils;

/**
 * Created by Bryan on 6/11/2017.
 */

public class PatentsViewItemDetailActivity extends AppCompatActivity {

    private static final String TAG = PatentsViewItemDetailActivity.class.getSimpleName();

    private PatentsViewUtils.SearchResult mSearchResult;
    private TextView mSearchResultNumber;
    private TextView mSearchResultTitle;
    private TextView mSearchResultAbstract;
    private FavoritesDatabaseHelper mFavoritesDatabaseHelper;

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

        mFavoritesDatabaseHelper = new FavoritesDatabaseHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patent_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_favorite:
                ContentValues values = new ContentValues();
                values.put(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_ID, mSearchResult.patentId);
                values.put(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_TITLE, mSearchResult.patentTitle);
                values.put(FavoritesDatabaseContract.FavoritePatents.COLUMN_PATENT_ABSTRACT, mSearchResult.patentAbstract);

                mFavoritesDatabaseHelper.getWritableDatabase().insert(
                        FavoritesDatabaseContract.FavoritePatents.TABLE_NAME, null, values);

                Log.d(TAG, "Inserted favorite into database.");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
