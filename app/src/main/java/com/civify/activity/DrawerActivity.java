package com.civify.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.fragments.AchievementsFragment;
import com.civify.activity.fragments.EventsFragment;
import com.civify.activity.fragments.NavigateFragment;
import com.civify.activity.fragments.RewardsFragment;
import com.civify.activity.fragments.SettingsFragment;
import com.civify.activity.fragments.WallFragment;
import com.civify.activity.fragments.profile.ProfileFragment;
import com.civify.adapter.UserAdapter;
import com.civify.model.User;
import com.mikhaellopez.circularimageview.CircularImageView;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int NAVIGATE_ID = 0;
    private static final int WALL_ID = 1;
    private static final int PROFILE_ID = 2;
    private static final int REWARDS_ID = 3;
    private static final int ACHIEVEMENTS_ID = 4;
    private static final int EVENTS_ID = 5;
    private static final int SETTINGS_ID = 6;
    private static final int DETAILS_ID = 7;

    private static final int COINS = 432;
    private static final int EXPERIENCE = 50;
    private static final int LEVEL = 3;
    private static final int SHOW_AS_ACTION_IF_ROOM = 1;
    private static final int SHOW_AS_ACTION_NEVER = 0;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    //private AppBarLayout mAppBarLayout;
    private int mCurrentFragment;
    private boolean mShowMenu;
    private boolean mShowMenuDetails;
    private User mCurrentUser;

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    public int getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //mAppBarLayout = (AppBarLayout) findViewById(R.id.bar_layout);
        mShowMenu = false;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle(R.string.app_name);

        NavigateFragment navigateFragment = NavigateFragment.newInstance();
        setFragment(navigateFragment, NAVIGATE_ID);
        mNavigationView.getMenu().getItem(mCurrentFragment).setChecked(true);

        mCurrentUser = UserAdapter.getCurrentUser();

        mCurrentUser.setLevel(LEVEL);
        mCurrentUser.setCoins(COINS);
        mCurrentUser.setExperience(EXPERIENCE);
        setUserHeader(mCurrentUser);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else super.onBackPressed();
    }

    /*
     * These functions will be used eventually
     */

    //    private void hideToolbar() {
    //        mAppBarLayout.animate().translationY(-mToolbar.getHeight()).setInterpolator(
    //                new AccelerateInterpolator(2));
    //    }

    //    private void showToolbar() {
    //        mAppBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator
    // (2));
    //    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_navigate) {
            NavigateFragment navigateFragment = NavigateFragment.newInstance();
            setFragment(navigateFragment, NAVIGATE_ID);
        } else if (id == R.id.nav_wall) {
            WallFragment wallFragment = WallFragment.newInstance();
            setFragment(wallFragment, WALL_ID);
        } else if (id == R.id.nav_profile) {
            ProfileFragment profileFragment = ProfileFragment.newInstance();
            setFragment(profileFragment, PROFILE_ID);
        } else if (id == R.id.nav_rewards) {
            // paint fragment
            RewardsFragment rewardsFragment = RewardsFragment.newInstance();
            setFragment(rewardsFragment, REWARDS_ID);
        } else if (id == R.id.nav_achievements) {
            AchievementsFragment achievementsFragment = AchievementsFragment.newInstance();
            setFragment(achievementsFragment, ACHIEVEMENTS_ID);
        } else if (id == R.id.nav_events) {
            EventsFragment eventsFragment = EventsFragment.newInstance();
            setFragment(eventsFragment, EVENTS_ID);
        } else if (id == R.id.nav_settings) {
            SettingsFragment settingsFragment = SettingsFragment.newInstance();
            setFragment(settingsFragment, SETTINGS_ID);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment, int fragmentId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_content, fragment)
                .addToBackStack(String.valueOf(fragmentId)).commit();
        if (fragmentId == PROFILE_ID) mShowMenu = true;
        else if (fragmentId == DETAILS_ID) {

            mShowMenu = true;
            mShowMenuDetails = true;
        } else {
            mShowMenu = false;
            mShowMenuDetails = false;
        }
        invalidateOptionsMenu();
        mCurrentFragment = fragmentId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(mShowMenuDetails ? R.menu.details : R.menu.drawer, menu);
        int noIcona = SHOW_AS_ACTION_NEVER;
        if (mShowMenuDetails) {
            noIcona = SHOW_AS_ACTION_IF_ROOM;
        }

        for (int i = 0; i < menu.size(); ++i) {
            menu.getItem(i).setVisible(mShowMenu);
            menu.getItem(i).setShowAsAction(noIcona);
        }

        return true;
    }

    private void setUserHeader(User user) {
        View headerView = mNavigationView.getHeaderView(0);
        // progressBar.setProgress(user.getLevel()/utils.calcMaxLevel(userLevel) * 100);

        ProgressBar progressBar = (ProgressBar) headerView.findViewById(R.id.header_progress);

        TextView name = (TextView) headerView.findViewById(R.id.header_name);
        name.setText(user.getName() + " " + user.getSurname());

        TextView username = (TextView) headerView.findViewById(R.id.header_username);
        username.setText(user.getUsername());

        TextView level = (TextView) headerView.findViewById(R.id.header_level);
        String userLevel = Integer.toString(user.getLevel());
        String showLevel = getString(R.string.level) + ' ' + userLevel;
        level.setText(showLevel);

        TextView xp = (TextView) headerView.findViewById(R.id.header_xp);
        String userExperience = Integer.toString(user.getExperience());
        //xp.setText(userExperience + '/' + utils.calcMaxXp(userLevel));

        TextView coins = (TextView) headerView.findViewById(R.id.header_coins);
        String userCoins = Integer.toString(user.getCoins());
        coins.setText(userCoins);

        CircularImageView profileImage =
                (CircularImageView) headerView.findViewById(R.id.header_image);
        //profileImage.setImageBitmap(img); // bitmap
        //profileImage.setImageIcon(img); // icon
    }
}
