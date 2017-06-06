package com.civify.activity.fragments.badge;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;

public class BadgeViewFragment extends BasicFragment {

    public BadgeViewFragment() {
        // Required empty public constructor
    }

    public static BadgeViewFragment newInstance() {
        return new BadgeViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_badge_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.event_recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(mBadgeViewAdapter);
        return view;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.PROFILE_ID;
    }
}
