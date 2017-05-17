package com.civify.adapter.award;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.civify.activity.fragments.award.AvailableAwardsFragment;
import com.civify.activity.fragments.award.ExchangedRewardsFragment;

public class AwardsFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 2;

    private AvailableAwardsFragment mAvailableAwardsFragment;

    public AwardsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        if (mAvailableAwardsFragment == null) {
            mAvailableAwardsFragment = AvailableAwardsFragment.newInstance();
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AvailableAwardsFragment();
            default:
                return new ExchangedRewardsFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}
