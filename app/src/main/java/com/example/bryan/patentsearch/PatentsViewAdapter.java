package com.example.bryan.patentsearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bryan.patentsearch.utils.PatentsViewUtils;

import java.util.ArrayList;

/**
 * Created by Bryan on 6/11/2017.
 */

public class PatentsViewAdapter extends RecyclerView.Adapter<PatentsViewAdapter.PatentsViewItemViewHolder> {

    private ArrayList<PatentsViewUtils.SearchResult> mSearchResultsList;
    private OnSearchResultClickListener mSearchResultClickListener;

    public PatentsViewAdapter(OnSearchResultClickListener clickListener){
        mSearchResultClickListener = clickListener;
    }

    public void updateSearchResults(ArrayList<PatentsViewUtils.SearchResult> searchResultsList){
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public PatentsViewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new PatentsViewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatentsViewItemViewHolder holder, int position) {
        holder.bind(mSearchResultsList.get(position));
    }

    public interface OnSearchResultClickListener {
        void onSearchResultClick(PatentsViewUtils.SearchResult searchResult);
    }

    @Override
    public int getItemCount() {
        if (mSearchResultsList != null) {
            return mSearchResultsList.size();
        } else {
            return 0;
        }
    }

    class PatentsViewItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mSearchResultTV;

        public PatentsViewItemViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            itemView.setOnClickListener(this);
        }

        public void bind(PatentsViewUtils.SearchResult searchResult) {
            mSearchResultTV.setText(searchResult.patentId);
        }

        @Override
        public void onClick(View v) {
            PatentsViewUtils.SearchResult searchResult = mSearchResultsList.get(getAdapterPosition());
            mSearchResultClickListener.onSearchResultClick(searchResult);
        }

    }

}
