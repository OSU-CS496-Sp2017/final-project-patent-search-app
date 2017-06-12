package com.example.bryan.patentsearch.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bryan on 6/11/2017.
 */

public class PatentsViewUtils {

    public static class SearchResult implements Serializable {
        public static final String EXTRA_SEARCH_RESULT = "PatentsViewUtils.SearchResult";
        public String patentId;
        public String patentNumber;
        public String patentTitle;
    }

    public static ArrayList<SearchResult> parsePatentsSearchResultsJSON(String searchResultsJSON){
        try{
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsItems = searchResultsObj.getJSONArray("patents");

            ArrayList<SearchResult> searchResultList = new ArrayList<SearchResult>();
            for (int i = 0; i < searchResultsItems.length(); i ++){
                SearchResult searchResult = new SearchResult();
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);

                searchResult.patentId = searchResultItem.getString("patent_id");
                searchResult.patentNumber = searchResultItem.getString("patent_number");
                searchResult.patentTitle = searchResultItem.getString("patent_title");

                searchResultList.add(searchResult);
            }
            return searchResultList;
        } catch (JSONException e){
            return null;
        }
    }

}
