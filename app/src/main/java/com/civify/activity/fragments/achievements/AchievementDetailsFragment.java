package com.civify.activity.fragments.achievements;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.model.achievement.Achievement;

public class AchievementDetailsFragment extends BasicFragment {

    private static final String DEBUG = "debug-AchievementDetail";

    private static final String TAG_ACHIEVEMENT = "achievement";
    private static final int PERCENT = 100;

    private Achievement mAchievement;
    private View mView;

    public AchievementDetailsFragment() {
        // Required empty public constructor
    }

    public static AchievementDetailsFragment newInstance(@NonNull Achievement achievement) {
        AchievementDetailsFragment fragment = new AchievementDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG_ACHIEVEMENT, achievement);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_achievement_details, container, false);
        init();
        return mView;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.DETAILS_ACHIEVEMENTS_ID;
    }

    private void init() {
        Log.d(DEBUG, "init");

        Log.d(DEBUG, "Getting arguments from bundle");
        Bundle bundle = getArguments();
        mAchievement = (Achievement) bundle.getSerializable(TAG_ACHIEVEMENT);

        setAchievement();

        Log.d(DEBUG, "init finished");
    }

    private void setAchievement() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mAchievement.getTitle());

        addAchievementBadge();
        addAchievementCoins();
        addAchievementXp();
        addAchievementTitle();
        addAchievementDescription();
        addAchievementProgress();
    }

    private void addAchievementBadge() {
        Log.d(DEBUG, "Adding badge...");
        ImageView imageAchievement = (ImageView) mView.findViewById(R.id.achievement_details_image);
        Glide.with(this)
                .load(mAchievement.getBadge().getLargeUrl())
                .centerCrop()
                .into(imageAchievement);
    }

    private void addAchievementCoins() {
        Log.d(DEBUG, "Adding coins...");
        TextView coins = (TextView) mView.findViewById(R.id.achievement_details_coins);
        coins.setText(String.valueOf(mAchievement.getCoins()));
    }

    private void addAchievementXp() {
        Log.d(DEBUG, "Adding xp...");
        TextView xp = (TextView) mView.findViewById(R.id.achievement_details_xp);
        xp.setText(mAchievement.getXp() + " xp");
    }

    private void addAchievementTitle() {
        Log.d(DEBUG, "Adding title...");
        TextView title = (TextView) mView.findViewById(R.id.achievement_details_title);
        title.setText(mAchievement.getTitle());
    }

    private void addAchievementDescription() {
        Log.d(DEBUG, "Adding description...");
        TextView description = (TextView) mView.findViewById(R.id.achievement_details_description);
        description.setText(mAchievement.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addAchievementProgress() {
        Log.d(DEBUG, "Adding progress...");
        TextView progressTotal = (TextView) mView.findViewById(R.id
                .achievement_details_progressTotal);
        progressTotal.setText(mAchievement.getProgress() + "/" + mAchievement.getNumber());
        int progress = (mAchievement.getProgress() * PERCENT) / mAchievement.getNumber();
        ProgressBar progressBar = (ProgressBar) mView.findViewById(R.id
                .achievement_details_progressBar);
        progressBar.setProgress(progress);
        TextView progressText =
                (TextView) mView.findViewById(R.id.achievement_details_progressText);
        progressText.setText(progress + "%");
    }
}
