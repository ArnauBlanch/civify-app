package com.civify.activity.fragments.achievements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;

public class AchievementsFragment extends BasicFragment {

    private ProgressBar mProgressBar;
    private AchievementViewFragment mAchievementViewFragment;

    public AchievementsFragment() {
    }

    public static AchievementsFragment newInstance() {
        return new AchievementsFragment();
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
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_rewards);
        return view;
    }

    /*
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        mAchievementViewFragment = new AchievementViewFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.rewards_container, mAchievementViewFragment)
                .commit();
        AdapterFactory.getInstance().getAwardAdapter(getContext())
                .getOfferedAwards(new ListAwardsSimpleCallback() {
                    @Override
                    public void onSuccess(List<Award> awards) {
                        mProgressBar.setVisibility(View.GONE);
                        mAwardsViewFragment.setRewardList(awards);
                    }

                    @Override
                    public void onFailure() {
                        Snackbar.make(view, "Couldn't retrieve rewards.", Snackbar.LENGTH_SHORT);
                    }
                });
    }
    */
}
