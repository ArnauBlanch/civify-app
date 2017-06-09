package com.civify.activity.introduction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class IntroductionPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 5;

    private ArrayList<IntroductionFragment> mFragmentList;

    public IntroductionPagerAdapter(FragmentManager fm, ArrayList<IntroductionFragment> fragments) {
        super(fm);
        this.mFragmentList = fragments;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
