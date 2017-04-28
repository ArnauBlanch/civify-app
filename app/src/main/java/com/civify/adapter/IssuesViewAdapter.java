package com.civify.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.model.issue.Category;
import com.civify.model.issue.Issue;
import com.civify.utils.ServiceGenerator;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

public class IssuesViewAdapter extends RecyclerView.Adapter<IssuesViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Issue> mIssueList;
    private PrettyTime mPrettyTime;
    private Drawable mDrawableCategory;

    public IssuesViewAdapter(Context context, List<Issue> issueList) {
        this.mContext = context;
        this.mIssueList = issueList;
        Locale.setDefault(Locale.ENGLISH);
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
        String numConfirms = '+' + Integer.toString(issue.getConfirmVotes());
        holder.getNumConfirms().setText(numConfirms);
        Category issueCategory = issue.getCategory();
        CircularImageView categoryImage = holder.getCategory();
      
        // dist = CivifyMap.getMarkers().get(issue.getIssueAuthToken()).getDistanceFromMe().

        // loading album cover using Glide library
        String imageUrl = ServiceGenerator.BASE_URL + issue.getPicture().getMedUrl();
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
        private CircularImageView mCategory;

        public MyViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.title);
            mCount = (TextView) view.findViewById(R.id.title2);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            mNumConfirms = (AppCompatButton) view.findViewById(R.id.num_confirms);
            mCategory = (CircularImageView) view.findViewById(R.id.wall_category);
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

        public CircularImageView getCategory() {
            return mCategory;
        }
    }
}
