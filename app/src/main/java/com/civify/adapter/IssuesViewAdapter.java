package com.civify.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.model.issue.Issue;
import com.civify.utils.ServiceGenerator;

import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;

public class IssuesViewAdapter extends RecyclerView.Adapter<IssuesViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Issue> mIssueList;
    private PrettyTime mPrettyTime;

    public IssuesViewAdapter(Context context, List<Issue> issueList) {
        this.mContext = context;
        this.mIssueList = issueList;
        mPrettyTime = new PrettyTime();
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
        holder.getTextView().setText(issue.getTitle());
        Date created = issue.getCreatedAt();
        holder.getCount().setText(mPrettyTime.format(created));
        String numConfirms = Integer.toString(issue.getConfirmVotes());
        holder.getNumConfirms().setText(numConfirms);
        // dist = CivifyMap.getMarkers().get(issue.getIssueAuthToken()).getDistanceFromMe().

        // loading album cover using Glide library
        String imageUrl = ServiceGenerator.BASE_URL + issue.getPicture().getLargeUrl();
        Glide.with(mContext).load(imageUrl).into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mIssueList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView, mCount;
        private ImageView mThumbnail;
        private AppCompatButton mNumConfirms;

        public MyViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.title);
            mCount = (TextView) view.findViewById(R.id.title2);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            mNumConfirms = (AppCompatButton) view.findViewById(R.id.num_confirms);
        }

        public TextView getTextView() {
            return mTextView;
        }

        public TextView getCount() {
            return mCount;
        }

        public AppCompatButton getNumConfirms() {
            return mNumConfirms;
        }

    }
}
