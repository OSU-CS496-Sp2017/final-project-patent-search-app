package com.example.bryan.patentsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.bryan.patentsearch.utils.PatentsViewUtils;

/**
 * Created by Bryan on 6/11/2017.
 */

public class PatentsViewItemDetailActivity extends AppCompatActivity {

    private TextView mSearchResultNumber;
    private TextView mSearchResultTitle;
    private TextView mSearchResultId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultNumber = (TextView)findViewById(R.id.tv_search_result_number);
        mSearchResultTitle =(TextView)findViewById(R.id.tv_search_result_title);
        mSearchResultId = (TextView) findViewById(R.id.tv_search_result_id);

        Intent intent = getIntent();
        PatentsViewUtils.SearchResult searchResult = (PatentsViewUtils.SearchResult)intent.getSerializableExtra(PatentsViewUtils.SearchResult.EXTRA_SEARCH_RESULT);
        mSearchResultNumber.setText(searchResult.patentNumber);
        mSearchResultTitle.setText(searchResult.patentTitle);
        mSearchResultId.setText(searchResult.patentId);
    }
}
