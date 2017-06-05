package com.civify.adapter.achievement;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.activity.fragments.RewardDialogFragment;
import com.civify.activity.fragments.achievements.AchievementsFragment;
import com.civify.model.Reward;
import com.civify.model.achievement.Achievement;
import com.civify.service.award.RewardCallback;
import com.civify.utils.AdapterFactory;

import java.util.List;

public class AchievementViewAdapter extends RecyclerView.Adapter<AchievementViewAdapter
        .AchievementViewHolder> {

    private static final int PERCENT = 100;

    private List<Achievement> mItems;
    private Context mContext;
    private AchievementsFragment mAchievementsFragment;

    public AchievementViewAdapter(List<Achievement> items, AchievementsFragment context) {
        this.mItems = items;
        this.mContext = context.getContext();
        this.mAchievementsFragment = context;
    }

    @Override
    public AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievement,
                parent, false);
        return new AchievementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AchievementViewHolder holder, final int position) {
        final Achievement achievement = mItems.get(position);
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

        // CLAIM button
        if (achievement.isClaimed() || !achievement.isCompleted()) {
            holder.getClaimButton().setVisibility(View.GONE);
        } else {
            setClaimListener(holder, achievement);
        }
    }

    private void setClaimListener(AchievementViewHolder holder, final Achievement achievement) {
        holder.getClaimButton().setClickable(true);
        holder.getClaimButton().setFocusable(true);
        holder.getClaimButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterFactory.getInstance().getAchievementAdapter(mContext).claimAchievement(
                        achievement.getToken(), new RewardCallback() {
                            @Override
                            public void onSuccess(Reward reward) {
                                RewardDialogFragment.show((FragmentActivity) mContext, reward);
                                mAchievementsFragment.updateList();
                            }

                            @Override
                            public void onFailure() {
                                Snackbar.make(mAchievementsFragment.getView(),
                                        mContext.getString(R.string.couldnt_claim_achievement),
                                        Snackbar.LENGTH_SHORT);
                            }
                        });
            }
        });
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
        private final LinearLayout mClaimButton;

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
            mClaimButton = (LinearLayout) itemView.findViewById(R.id.claim_linear_layout);
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

        public LinearLayout getClaimButton() {
            return mClaimButton;
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
