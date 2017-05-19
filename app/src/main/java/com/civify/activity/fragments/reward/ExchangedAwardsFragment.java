package com.civify.activity.fragments.reward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.UserAdapter;
import com.civify.model.award.ExchangedAward;
import com.civify.service.award.ListExchangedAwardSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.List;

public class ExchangedAwardsFragment extends BasicFragment {

    private ProgressBar mProgressBar;
    private ExchangedAwardsViewFragment mExchangedAwardsViewFragment;

    public ExchangedAwardsFragment() {
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.REWARDS_ID;
    }

    public static ExchangedAwardsFragment newInstance() {
        ExchangedAwardsFragment fragment = new ExchangedAwardsFragment();
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
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_rewards);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        mExchangedAwardsViewFragment = new ExchangedAwardsViewFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.rewards_container, mExchangedAwardsViewFragment)
                .commit();
        AdapterFactory.getInstance().getAwardAdapter(getContext())
                .getExchangedAwards(UserAdapter.getCurrentUser().getUserAuthToken(),
                        new ListExchangedAwardSimpleCallback() {
                            @Override
                            public void onSuccess(List<ExchangedAward> exchangedAwards) {
                                mProgressBar.setVisibility(View.GONE);
                                mExchangedAwardsViewFragment.setRewardList(exchangedAwards);
                            }

                            @Override
                            public void onFailure() {
                                Snackbar.make(view, "Couldn't retrieve rewards.",
                                        Snackbar.LENGTH_SHORT);
                            }
                        });
    }
}
