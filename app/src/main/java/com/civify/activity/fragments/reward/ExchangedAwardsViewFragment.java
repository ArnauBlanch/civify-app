package com.civify.activity.fragments.reward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.adapter.award.ExchangedAwardAdapter;
import com.civify.model.award.ExchangedAward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExchangedAwardsViewFragment extends Fragment {

    private List<ExchangedAward> mRewardList;
    private ExchangedAwardAdapter mExchangedAwardAdapter;

    public ExchangedAwardsViewFragment() {
        mRewardList = new ArrayList<>();
    }

    public void setRewardList(List<ExchangedAward> rewardsList) {
        mRewardList.clear();
        ArrayList<ExchangedAward> intermediateList = new ArrayList<>(rewardsList);
        Collections.reverse(intermediateList);
        mRewardList.addAll(intermediateList);

        sortExchangedRewardsList();

        mExchangedAwardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        sortExchangedRewardsList();

        mExchangedAwardAdapter = new ExchangedAwardAdapter(mRewardList, getContext());

        View view = inflater.inflate(R.layout.fragment_rewards_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_rewards);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mExchangedAwardAdapter);

        return view;
    }

    private void sortExchangedRewardsList() {
        Collections.sort(mRewardList, new Comparator<ExchangedAward>() {
            @Override
            public int compare(ExchangedAward o1, ExchangedAward o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });
    }
}
