package com.civify.adapter.achievement;

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
import com.civify.activity.fragments.achievements.AchievementsFragment;
import com.civify.model.achievement.AchievementStub;

import java.util.List;


public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter
        .AchievementViewHolder> {

    private List<AchievementStub> mItems;
    private AchievementsFragment mContext;

    public AchievementAdapter(List<AchievementStub> items, AchievementsFragment context) {
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
        AchievementStub achievement = mItems.get(position);
        holder.setAchievement(achievement);
        Glide.with(mContext)
                .load(achievement.getBadgePicture())
                .centerCrop()
                .into(holder.getImageViewImage());
        holder.getTextViewTitle().setText(achievement.getTitle());
        holder.getTextViewDescription().setText(achievement.getDescription());
        String progress = achievement.getProgress() + "%";
        holder.getTextViewProgress().setText(progress);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        private ImageView mImageViewImage;
        private TextView mTextViewTitle;
        private TextView mTextViewDescription;
        private ProgressBar mProgressBar;
        private TextView mTextViewProgress;

        private AchievementStub mAchievement;
        private View mView;

        public AchievementViewHolder(View itemView) {
            super(itemView);
            mImageViewImage = (ImageView) itemView.findViewById(R.id.achievement_item_image);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.achievement_item_title);
            mTextViewDescription =
                    (TextView) itemView.findViewById(R.id.achievement_item_description);
            mTextViewProgress =
                    (TextView) itemView.findViewById(R.id.achievement_item_progressText);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            mView = itemView;
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

        public void setAchievement(AchievementStub achievement) {
            mAchievement = achievement;
        }

        @Override
        public void onClick(View v) {
            //TODO achievement.showAchievementDetails();
        }
    }
}
