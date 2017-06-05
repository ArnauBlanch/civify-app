package com.civify.activity.fragments.achievements;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.achievement.AchievementViewAdapter;
import com.civify.model.achievement.Achievement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AchievementViewFragment extends BasicFragment {

    private List<Achievement> mAchievementList;
    private AchievementViewAdapter mAchievementAdapter;
    private AchievementsFragment mAchievementsFragment;

    public AchievementViewFragment() {
        mAchievementList = new ArrayList<>();
    }

    public static AchievementViewFragment newInstance() {
        AchievementViewFragment fragment = new AchievementViewFragment();
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

        mAchievementAdapter = new AchievementViewAdapter(mAchievementList, mAchievementsFragment);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.achievement_recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAchievementAdapter);

        return view;
    }

    public void setAchievementsList(List<Achievement> items) {
        mAchievementList.clear();
        ArrayList<Achievement> intermediateList = new ArrayList<>(items);
        Collections.reverse(intermediateList);
        mAchievementList.addAll(intermediateList);

        mAchievementAdapter.notifyDataSetChanged();
    }

    public void setAchievementsFragment(AchievementsFragment achievementsFragment) {
        this.mAchievementsFragment = achievementsFragment;
    }
}
