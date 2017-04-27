package com.civify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;
import com.civify.model.issue.Issue;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.civify.utils.ServiceGenerator;

public class IssuesViewAdapter extends RecyclerView.Adapter<IssuesViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Issue> mIssueList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView, mCount;
        public ImageView mThumbnail;

        public MyViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.title);
            mCount = (TextView) view.findViewById(R.id.count);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public IssuesViewAdapter(Context mContext, List<Issue> issueList) {
        this.mContext = mContext;
        this.mIssueList = issueList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issues_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Issue issue = mIssueList.get(position);
        holder.mTextView.setText(issue.getTitle());
        holder.mCount.setText("fa 3 dies");

        // loading album cover using Glide library
        String imageUrl = ServiceGenerator.BASE_URL + issue.getPicture().getLargeUrl();
        Glide.with(mContext).load(imageUrl).into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mIssueList.size();
    }
}
