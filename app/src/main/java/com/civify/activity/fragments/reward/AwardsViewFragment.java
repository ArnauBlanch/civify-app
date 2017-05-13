package com.civify.activity.fragments.reward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.adapter.award.AwardsViewAdapter;
import com.civify.model.award.Award;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AwardsViewFragment extends Fragment {
    private List<Award> mRewardList;
    private AwardsViewAdapter mAwardsViewAdapter;

    public AwardsViewFragment() {
        mRewardList = new ArrayList<>();
    }

    public void setRewardList(List<Award> rewardsList) {
        mRewardList.clear();
        ArrayList<Award> intermediateList = new ArrayList<>(rewardsList);
        Collections.reverse(intermediateList);
        mRewardList.addAll(intermediateList);

        mAwardsViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_rewards);
        mAwardsViewAdapter = new AwardsViewAdapter(getContext(), mRewardList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAwardsViewAdapter);

        return view;
    }
}
