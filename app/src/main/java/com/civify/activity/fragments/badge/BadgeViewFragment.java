package com.civify.activity.fragments.badge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.badge.BadgeViewAdapter;
import com.civify.model.badge.Badge;
import com.civify.service.badge.ListBadgesSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BadgeViewFragment extends BasicFragment {

    private List<Badge> mBadgeList;
    private BadgeViewAdapter mBadgeViewAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.badge_recyclerView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_badges);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mBadgeViewAdapter);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userToken = AdapterFactory.getInstance().getSharedPreferences(getContext())
                .getString(LoginAdapterImpl.AUTH_TOKEN, "");
        AdapterFactory.getInstance().getBadgeAdapter(getContext())
                .getUserBadges(userToken, new ListBadgesSimpleCallback() {
                    @Override
                    public void onSuccess(List<Badge> badges) {
                        //mProgressBar.setVisibility(View.GONE);
                        setBadgeList(badges);
                    }

                    @Override
                    public void onFailure() {
                        Snackbar.make(view, "Couldn't retrieve badges",
                                Snackbar.LENGTH_SHORT);
                    }
                });
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
