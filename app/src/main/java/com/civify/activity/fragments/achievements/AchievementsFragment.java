package com.civify.activity.fragments.achievements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.achievement.AchievementAdapter;
import com.civify.model.achievement.Achievement;
import com.civify.service.achievement.ListAchievementsSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.List;

public class AchievementsFragment extends BasicFragment {

    private ProgressBar mProgressBar;
    private AchievementViewFragment mAchievementViewFragment;
    private AchievementAdapter mAchievementAdapter;

    public AchievementsFragment() {
    }

    public static AchievementsFragment newInstance() {
        return new AchievementsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.ACHIEVEMENTS_ID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_achievements);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        AdapterFactory adapterFactory = AdapterFactory.getInstance();
        mAchievementAdapter = adapterFactory.getAchievementAdapter(getContext());
        mAchievementViewFragment = new AchievementViewFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.achievements_container, mAchievementViewFragment)
                .commit();
        mAchievementAdapter.getAchievements(new ListAchievementsSimpleCallback() {
            @Override
            public void onSuccess(List<Achievement> achievementList) {
                mProgressBar.setVisibility(View.GONE);
                mAchievementViewFragment.setAchievementsList(achievementList);
            }

            @Override
            public void onFailure() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
