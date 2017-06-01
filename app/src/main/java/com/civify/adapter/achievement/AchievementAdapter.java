package com.civify.adapter.achievement;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.civify.R;
import com.civify.model.achievement.AchievementStub;

import java.util.List;


public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter
        .AchievementViewHolder> {

    private List<AchievementStub> mItems;

    public AchievementAdapter(List<AchievementStub> items) {
        this.mItems = items;
    }

    @Override
    public AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_achievement,
                parent, false);
        return new AchievementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AchievementViewHolder holder, int position) {
        holder.getTextViewTitle().setText(mItems.get(position).getTitle());
        holder.getTextViewDescription().setText(mItems.get(position).getDescription());
        String progress = mItems.get(position).getProgress() + "%";
        holder.getTextViewProgress().setText(progress);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewImage;
        private TextView mTextViewTitle;
        private TextView mTextViewDescription;
        private ProgressBar mProgressBar;
        private TextView mTextViewProgress;

        public AchievementViewHolder(View itemView) {
            super(itemView);
            mImageViewImage = (ImageView) itemView.findViewById(R.id.achievement_cardview_image);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.achievement_cardview_title);
            mTextViewDescription =
                    (TextView) itemView.findViewById(R.id.achievement_cardview_description);
            mTextViewProgress =
                    (TextView) itemView.findViewById(R.id.achievement_cardview_progressText);
        }

        public ImageView getImageViewImage() {
            return mImageViewImage;
        }

        public TextView getTextViewTitle() {
            return mTextViewTitle;
        }

        public TextView getTextViewDescription() {
            return mTextViewDescription;
        }

        public ProgressBar getProgressBar() {
            return mProgressBar;
        }

        public TextView getTextViewProgress() {
            return mTextViewProgress;
        }
    }
}
