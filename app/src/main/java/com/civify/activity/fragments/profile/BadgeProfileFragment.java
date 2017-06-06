package com.civify.activity.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.activity.fragments.badge.BadgeViewFragment;

public class BadgeProfileFragment extends BasicFragment {

    private BadgeViewFragment mBadgeViewFragment;
    private ProgressBar mProgressBar;

    public BadgeProfileFragment() {
        // Required empty public constructor
    }

    public static BadgeProfileFragment newInstance() {
        return new BadgeProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_badge_profile, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_badges);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mBadgeViewFragment = new BadgeViewFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.badge_container, mBadgeViewFragment)
                .commit();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.PROFILE_ID;
    }
}
