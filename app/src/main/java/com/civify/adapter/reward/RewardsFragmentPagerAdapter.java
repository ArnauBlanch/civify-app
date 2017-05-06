package com.civify.adapter.reward;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.civify.activity.fragments.reward.AvailableRewardsFragment;
import com.civify.activity.fragments.reward.ExchangedRewardsFragment;

public class RewardsFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 2;

    private AvailableRewardsFragment mAvailableRewardsFragment;

    public RewardsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        if (mAvailableRewardsFragment == null) {
            mAvailableRewardsFragment = AvailableRewardsFragment.newInstance();
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AvailableRewardsFragment();
            default:
                return new ExchangedRewardsFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}
