package com.civify.activity.fragments.award;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.award.AwardsFragmentPagerAdapter;

public class AwardsFragment extends BasicFragment {

    public AwardsFragment() {
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.REWARDS_ID;
    }

    public static AwardsFragment newInstance() {
        return new AwardsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rewards_tab_host, container, false);

        TabLayout tabLayout = setTabLayout(view);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager_rewards);
        AwardsFragmentPagerAdapter pagerAdapter =
                new AwardsFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });

        return view;
    }

    @NonNull
    private TabLayout setTabLayout(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_rewards);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.available_rewards_tab)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exchanged_rewards_tab)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Nunito-Regular.ttf");
        ViewGroup viewGroup = (ViewGroup) tabLayout.getChildAt(0);

        for (int i = 0; i < 2; i++) {
            AppCompatTextView appCompatTextView = (AppCompatTextView) ((ViewGroup) viewGroup
                    .getChildAt(i)).getChildAt(1);
            appCompatTextView.setTypeface(typeface, Typeface.NORMAL);
        }
        return tabLayout;
    }
}
