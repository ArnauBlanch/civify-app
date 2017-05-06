package com.civify.activity.fragments.reward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.model.issue.Picture;
import com.civify.model.reward.RewardStub;

import java.util.ArrayList;
import java.util.List;

public class AvailableRewardsFragment extends BasicFragment {

    private static final int COINS = 35;
    private static final int NUM_ITEMS = 5;

    private ProgressBar mProgressBar;
    private RewardsViewFragment mRewardsViewFragment;

    public AvailableRewardsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AvailableRewardsFragment.
     */
    public static AvailableRewardsFragment newInstance() {
        AvailableRewardsFragment fragment = new AvailableRewardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.REWARDS_ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_rewards);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO: Get rewards from server
        mRewardsViewFragment = new RewardsViewFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.rewards_container, mRewardsViewFragment)
                .commit();
        mProgressBar.setVisibility(View.GONE);
        mRewardsViewFragment.setRewardList(mockRewardList());
    }

    private List<RewardStub> mockRewardList() {
        ArrayList<RewardStub> list = new ArrayList<>();

        Picture picture = new Picture();
        picture.setSmallUrl("http://www.sportlife"
                + ".es/media/cache/original/upload/images/article/10489/article-swolf-eficiencia"
                + "-natacion-55632e1909028.jpg");

        RewardStub item = new RewardStub();
        item.setTitle("10% descompte en natació");
        item.setBusiness("Decathlon");
        item.setCoins(COINS);
        item.setPicture(picture);

        for (int i = 0; i < NUM_ITEMS; i++) {
            list.add(item);
        }

        return list;
    }
}
