package com.civify.civify.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.civify.civify.R;
import com.civify.civify.user.User;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MAP_ID = 0;
    private static final int WALL_ID = 1;
    private static final int PROFILE_ID = 2;
    private static final int REWARDS_ID = 3;
    private static final int ACHIEVMENTS_ID = 4;
    private static final int EVENTS_ID = 5;
    private static final int SETTINGS_ID = 6;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private int mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.bar_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mNavigationView.getMenu().getItem(0).setChecked(true);
        mCurrentFragment = 0;


    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
     * These functions will be used eventually
     */

//    private void hideToolbar() {
//        mAppBarLayout.animate().translationY(-mToolbar.getHeight()).setInterpolator(
//                new AccelerateInterpolator(2));
//    }

//    private void showToolbar() {
//        mAppBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int groupId = item.getOrder();


        if (id == R.id.nav_navigate) {
            mCurrentFragment = 0;
        } else if (id == R.id.nav_wall) {
            mCurrentFragment = 1;
        } else if (id == R.id.nav_profile) {
            mCurrentFragment = 2;
        } else if (id == R.id.nav_rewards) {
            // paint fragment
            ExampleFragment exampleFragment = ExampleFragment.newInstance();
            setFragment(exampleFragment, REWARDS_ID);
        } else if (id == R.id.nav_achievements) {
            mCurrentFragment = 4;
        } else if (id == R.id.nav_events) {
            mCurrentFragment = 5;
        } else if (id == R.id.nav_settings) {
            mCurrentFragment = 6;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment, int fragmentId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();
        mCurrentFragment = fragmentId;
        Log.d("DrawerActivity", Integer.toString(fragmentId));
    }

    private void setUserHeader(User user) {
        //TODO
    }
}
