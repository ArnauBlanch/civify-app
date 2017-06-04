package com.civify.activity.fragments.achievements;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.achievement.AchievementAdapter;
import com.civify.model.achievement.AchievementStub;

import java.util.ArrayList;
import java.util.List;

public class AchievementViewFragment extends BasicFragment {

    private static final int AUX_MOCK = 101;

    private View mView;

    public AchievementViewFragment() {
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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_achievement_view, container, false);
        init();
        return mView;
    }

    private void init() {
        List<AchievementStub> items = new ArrayList<>();
        for (int i = 0; i < AUX_MOCK; i++) {
            String title = "Title" + (i + 1);
            String description = "Description" + (i + 1);
            AchievementStub item = new AchievementStub(i, title, description, 0, null, 0,
                    0, null, null);
            item.setProgress(i);
            items.add(item);
        }

        RecyclerView achievementRecycler = (RecyclerView) mView.findViewById(R.id
                .achievement_recyclerView);
        achievementRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        achievementRecycler.setLayoutManager(layoutManager);

        AchievementAdapter achievementAdapter = new AchievementAdapter(items, this);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(achievementRecycler.getContext(),
                        LinearLayoutManager.VERTICAL);
        achievementRecycler.addItemDecoration(dividerItemDecoration);

        achievementRecycler.setAdapter(achievementAdapter);
    }
}
