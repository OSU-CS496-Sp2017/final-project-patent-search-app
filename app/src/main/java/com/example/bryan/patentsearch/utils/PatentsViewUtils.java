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

    private final static String PATENT_SEARCH_BASE_URL = "http://www.patentsview.org/api/patents/query";
    private final static String PATENT_SEARCH_QUERY_PARAM = "q";
    private final static String PATENT_QUERY_OPTION_PARAM = "o";
    private final static String PATENT_QUERY_SORT_PARAM = "s";
    private final static String PATENT_TITLE_PARAM = "patent_title";
    private final static String PATENT_NUMBER_PARAM = "patent_number";
    private final static String PATENT_DATE_PARAM = "patent_date";
    private final static String PATENT_PER_PAGE_PARAM = "per_page";

    public static class SearchResult implements Serializable {
        public static final String EXTRA_SEARCH_RESULT = "PatentsViewUtils.SearchResult";
        public String patentId;
        public String patentTitle;
        public String patentAbstract;
    }

    public static String buildPatentsViewURL(String patentTitle, String date, String perPage) {

        String query = "";
        String options = "";
        String sort = "";
        final String fields = "&f=[\"patent_id\", \"patent_title\", \"patent_abstract\"]";

        if (!patentTitle.equals("")) {
            query = "?q={\"_and\":[{\"_text_any\":{\"" + PATENT_TITLE_PARAM + "\":\"" + patentTitle.replaceAll(" ", "%20") + "\"}}]}";
        }
        if (!perPage.equals("")) {
            options = "&" + PATENT_QUERY_OPTION_PARAM + "={\"" + PATENT_PER_PAGE_PARAM + "\":\"" + perPage + "\"}";
        }
        if (!date.equals("")) {
            sort = "&" + PATENT_QUERY_SORT_PARAM + "=[{\"" + PATENT_DATE_PARAM + "\":\"" + date + "\"}]";
        }


        return PATENT_SEARCH_BASE_URL + query + options + sort + fields;
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
                searchResult.patentTitle = searchResultItem.getString("patent_title");
                searchResult.patentAbstract = searchResultItem.getString("patent_abstract");

                searchResultList.add(searchResult);
            }
            return searchResultList;
        } catch (JSONException e){
            return null;
        }
    }

}
