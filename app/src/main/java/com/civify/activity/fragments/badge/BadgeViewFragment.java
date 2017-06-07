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
import com.civify.adapter.badge.BadgeViewAdapter;
import com.civify.model.badge.Badge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BadgeViewFragment extends BasicFragment {

    private List<Badge> mBadgeList;
    private BadgeViewAdapter mBadgeViewAdapter;

    public BadgeViewFragment() {
        mBadgeList = new ArrayList<>();
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

        mBadgeViewAdapter = new BadgeViewAdapter(mBadgeList, getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_badge_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.badge_recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mBadgeViewAdapter);

        return view;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.PROFILE_ID;
    }

    public void setBadgeList(List<Badge> badges) {
        mBadgeList.clear();
        ArrayList<Badge> intermediateList = new ArrayList<>(badges);
        Collections.reverse(intermediateList);
        mBadgeList.addAll(intermediateList);

        mBadgeViewAdapter.notifyDataSetChanged();
    }
}
