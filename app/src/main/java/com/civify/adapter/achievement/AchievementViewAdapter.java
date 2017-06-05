package com.civify.adapter.achievement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.model.achievement.Achievement;

import java.util.List;

public class AchievementViewAdapter extends RecyclerView.Adapter<AchievementViewAdapter
        .AchievementViewHolder> {

    private static final int PERCENT = 100;

    private List<Achievement> mItems;
    private Context mContext;

    public AchievementViewAdapter(List<Achievement> items, Context context) {
        this.mItems = items;
        this.mContext = context;
    }

    @Override
    public AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievement,
                parent, false);
        return new AchievementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AchievementViewHolder holder, final int position) {
        Achievement achievement = mItems.get(position);
        holder.setAchievement(achievement);
        Glide.with(mContext)
                .load(achievement.getBadge().getLargeUrl())
                .centerCrop()
                .into(holder.getImageViewImage());
        holder.getTextViewTitle().setText(achievement.getTitle());
        holder.getTextViewDescription().setText(achievement.getDescription());
        int progress = (achievement.getProgress() * PERCENT) / achievement.getNumber();
        holder.getProgressBar().setProgress(progress);
        String progressText = progress + "%";
        holder.getTextViewProgress().setText(progressText);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        private final ImageView mImageViewImage;
        private final TextView mTextViewTitle;
        private final TextView mTextViewDescription;
        private final ProgressBar mProgressBar;
        private final TextView mTextViewProgress;

        private Achievement mAchievement;

        public AchievementViewHolder(View itemView) {
            super(itemView);
            mImageViewImage = (ImageView) itemView.findViewById(R.id.achievement_item_image);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.achievement_item_title);
            mTextViewDescription =
                    (TextView) itemView.findViewById(R.id.achievement_item_description);
            mTextViewProgress =
                    (TextView) itemView.findViewById(R.id.achievement_item_progressText);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.achievement_item_progressBar);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
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

        public void setAchievement(Achievement achievement) {
            mAchievement = achievement;
        }

        @Override
        public void onClick(View v) {
            mAchievement.showAchievementDetails();
        }
    }
}
