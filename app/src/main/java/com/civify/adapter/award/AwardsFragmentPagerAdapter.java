package com.civify.adapter.award;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.civify.activity.fragments.award.AvailableAwardsFragment;
import com.civify.activity.fragments.award.ExchangedAwardsFragment;

public class AwardsFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 2;

    private AvailableAwardsFragment mAvailableAwardsFragment;
    private ExchangedAwardsFragment mExchangedAwardsFragment;

    public AwardsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        if (mAvailableAwardsFragment == null) {
            mAvailableAwardsFragment = AvailableAwardsFragment.newInstance();
        }

        if (mExchangedAwardsFragment == null) {
            mExchangedAwardsFragment = ExchangedAwardsFragment.newInstance();
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mAvailableAwardsFragment;
            default:
                return mExchangedAwardsFragment;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    public void updateAvailableAwards() {
        mAvailableAwardsFragment.updateList();
    }

    public void updateExchangedAwards() {
        mExchangedAwardsFragment.updateList();
    }
}
