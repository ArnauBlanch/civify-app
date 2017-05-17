package com.civify.activity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AchievementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchievementsFragment extends BasicFragment {

    public AchievementsFragment() {
    }

    public static AchievementsFragment newInstance() {
        AchievementsFragment fragment = new AchievementsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.ACHIEVEMENTS_ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievements, container, false);
    }
}
