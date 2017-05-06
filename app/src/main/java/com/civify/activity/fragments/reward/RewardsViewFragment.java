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
import com.civify.adapter.reward.RewardsViewAdapter;
import com.civify.model.reward.RewardStub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RewardsViewFragment extends Fragment {
    private List<RewardStub> mRewardList;
    private RewardsViewAdapter mRewardsViewAdapter;

    public RewardsViewFragment() {
        mRewardList = new ArrayList<>();
    }

    public void setRewardList(List<RewardStub> rewardsList) {
        mRewardList.clear();
        List<RewardStub> intermediateList = new ArrayList<>(rewardsList);
        Collections.reverse(intermediateList);
        mRewardList.addAll(intermediateList);

        // TODO: Delete the next line
        mRewardsViewAdapter = new RewardsViewAdapter(getContext(), mRewardList);

        mRewardsViewAdapter.notifyDataSetChanged();
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
        mRewardsViewAdapter = new RewardsViewAdapter(getContext(), mRewardList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRewardsViewAdapter);

        return view;
    }
}
