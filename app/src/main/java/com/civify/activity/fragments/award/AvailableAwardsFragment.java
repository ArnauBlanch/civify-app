package com.civify.activity.fragments.award;

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
import com.civify.model.award.Award;
import com.civify.service.award.ListAwardsSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.List;

public class AvailableAwardsFragment extends BasicFragment {

    private ProgressBar mProgressBar;
    private AwardsViewFragment mAwardsViewFragment;

    public AvailableAwardsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AvailableAwardsFragment.
     */
    public static AvailableAwardsFragment newInstance() {
        AvailableAwardsFragment fragment = new AvailableAwardsFragment();
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        mAwardsViewFragment = new AwardsViewFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.rewards_container, mAwardsViewFragment)
                .commit();
        AdapterFactory.getInstance().getAwardAdapter(getContext())
                .getOfferedAwards(new ListAwardsSimpleCallback() {
                    @Override
                    public void onSuccess(List<Award> awards) {
                        mProgressBar.setVisibility(View.GONE);
                        mAwardsViewFragment.setRewardList(awards);
                    }

                    @Override
                    public void onFailure() {
                        Snackbar.make(view, "Couldn't retrieve rewards.", Snackbar.LENGTH_SHORT);
                    }
                });
    }

    public void updateList() {
        AdapterFactory.getInstance().getAwardAdapter(getContext())
                .getOfferedAwards(new ListAwardsSimpleCallback() {
                    @Override
                    public void onSuccess(List<Award> awards) {
                        mAwardsViewFragment.setRewardList(awards);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
    }
}
