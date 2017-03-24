package com.civify.civify.activity.registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class RegistrationPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 4;

    public RegistrationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position < 0 || position >= NUM_PAGES) {
            return null;
        } else {
            Fragment pageFragment = new RegistrationFragment();
            Bundle args = new Bundle();
            args.putInt("page", position);
            pageFragment.setArguments(args);
            return pageFragment;
        }
    }

    @SuppressWarnings("SameReturnValue")
    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
