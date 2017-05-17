package com.civify.activity.fragments.award;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;

public class ExchangedRewardsFragment extends BasicFragment {

    public ExchangedRewardsFragment() {
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.REWARDS_ID;
    }

    public static ExchangedRewardsFragment newInstance() {
        ExchangedRewardsFragment fragment = new ExchangedRewardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchanged_rewards, container, false);
    }
}
