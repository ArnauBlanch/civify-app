package com.civify.activity.introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.civify.R;
import com.civify.activity.BaseActivity;
import com.civify.activity.MainActivity;

import java.util.ArrayList;

public class IntroductionActivity extends BaseActivity {

    private static final int PAGES_NUM = 5;

    private IntroductionPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_view_pager);

        ArrayList<IntroductionFragment> fragments = new ArrayList<>();
        initFragmentList(fragments);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id
                .introduction_linear_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.introduction_view_pager);

        mPagerAdapter = new IntroductionPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) linearLayout.findViewById(
                R.id.introduction_tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    private void initFragmentList(ArrayList<IntroductionFragment> fragments) {
        for (int i = 0; i < PAGES_NUM; i++) {
            fragments.add(IntroductionFragment.newInstance(i));
        }
    }

    public void finishIntroduction() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
