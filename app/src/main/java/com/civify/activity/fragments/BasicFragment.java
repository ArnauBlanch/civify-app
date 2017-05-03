package com.civify.activity.fragments;

import android.support.v4.app.Fragment;

import com.civify.activity.DrawerActivity;

public class BasicFragment extends Fragment {
    protected int mId = DrawerActivity.UNDEFINED_ID;

    public int getFragmentId() {
        return mId;
    }
}
