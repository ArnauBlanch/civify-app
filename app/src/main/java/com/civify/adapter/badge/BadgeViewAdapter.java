package com.civify.adapter.badge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.civify.R;

public class BadgeViewAdapter extends RecyclerView.Adapter<BadgeViewAdapter.BadgeViewHolder> {

    @Override
    public BadgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_badge, parent,
                false);
        return new BadgeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BadgeViewHolder holder, int position) {
        //TODO fill fields
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public BadgeViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
