package com.example.bryan.patentsearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bryan on 6/11/2017.
 */

public class PatentsViewAdapter extends RecyclerView.Adapter<PatentsViewAdapter.PatentsViewItemViewHolder> {

    @Override
    public PatentsViewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PatentsViewItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class PatentsViewItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public PatentsViewItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
