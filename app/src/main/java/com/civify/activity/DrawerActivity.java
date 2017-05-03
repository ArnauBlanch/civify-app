package com.civify.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.civify.activity.fragments.BasicFragment;
import com.civify.activity.fragments.EventsFragment;
import com.civify.activity.fragments.NavigateFragment;
import com.civify.activity.fragments.RewardsFragment;
import com.civify.activity.fragments.SettingsFragment;
import com.civify.activity.fragments.WallFragment;
import com.civify.activity.fragments.profile.ProfileFragment;
import com.civify.adapter.UserAdapter;
import com.civify.model.User;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Stack;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int UNDEFINED_ID = -1;
    public static final int NAVIGATE_ID = 0;
    public static final int WALL_ID = 1;
    public static final int PROFILE_ID = 2;
    public static final int REWARDS_ID = 3;
    public static final int ACHIEVEMENTS_ID = 4;
    public static final int EVENTS_ID = 5;
    public static final int SETTINGS_ID = 6;
    public static final int DETAILS_ID = 7;

    private static final int COINS = 432;
    private static final int EXPERIENCE = 50;
    private static final int LEVEL = 3;

    private Stack<Fragment> mFragmentStack;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    //private AppBarLayout mAppBarLayout;
    private int mCurrentFragment;
    private boolean mShowMenu;
    private boolean mShowMenuDetails;
    private User mCurrentUser;

    public int getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.navigate_title));
        setSupportActionBar(mToolbar);
        //mAppBarLayout = (AppBarLayout) findViewById(R.id.bar_layout);
        mShowMenu = false;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mFragmentStack = new Stack<>();

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
        } else if (mFragmentStack.size() > 1) {
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            if (mCurrentFragment != NAVIGATE_ID && mCurrentFragment != DETAILS_ID) {
                BasicFragment fragment = (BasicFragment) mFragmentStack.pop();
                fragmentTransaction.remove(fragment);
                while (fragment.getFragmentId() != NAVIGATE_ID) {
                    fragment = (BasicFragment) mFragmentStack.pop();
                }
                mFragmentStack.clear();
                mFragmentStack.add(fragment);
                fragment.onResume();
                fragmentTransaction
                        .show(fragment)
                        .commit();
                mCurrentFragment = NAVIGATE_ID;
            } else {
                mFragmentStack.lastElement().onPause();
                fragmentTransaction.remove(mFragmentStack.pop());
                mFragmentStack.lastElement().onResume();
                BasicFragment restoredFragment = (BasicFragment) mFragmentStack.lastElement();
                fragmentTransaction.show(restoredFragment);
                fragmentTransaction.commit();
                mCurrentFragment = restoredFragment.getFragmentId();
            }
            setToolbarTitle();
            updateMenu();
            updateDrawerMenu();
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.frame_content, fragment);
        if (!mFragmentStack.empty()) {
            mFragmentStack.lastElement().onPause();
            fragmentTransaction.hide(mFragmentStack.lastElement());
            if (mCurrentFragment == fragmentId) mFragmentStack.pop();
        }
        mFragmentStack.push(fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        mCurrentFragment = fragmentId;
        updateMenu();
        setToolbarTitle();
    }

    private void updateMenu() {
        if (mCurrentFragment == PROFILE_ID) {
            mShowMenu = true;
        } else if (mCurrentFragment == DETAILS_ID) {
            mShowMenu = true;
            mShowMenuDetails = true;
        } else {
            mShowMenu = false;
            mShowMenuDetails = false;
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(mShowMenuDetails ? R.menu.details : R.menu.drawer, menu);

        for (int i = 0; i < menu.size(); ++i) {
            menu.getItem(i).setVisible(mShowMenu);
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

    private void setToolbarTitle() {
        String title;
        switch (mCurrentFragment) {
            case NAVIGATE_ID:
                title = getResources().getString(R.string.navigate_title);
                break;
            case WALL_ID:
                title = getResources().getString(R.string.wall_title);
                break;
            case PROFILE_ID:
                title = getResources().getString(R.string.profile_title);
                break;
            case REWARDS_ID:
                title = getResources().getString(R.string.rewards_title);
                break;
            case ACHIEVEMENTS_ID:
                title = getResources().getString(R.string.achievements_title);
                break;
            case EVENTS_ID:
                title = getResources().getString(R.string.events_title);
                break;
            case SETTINGS_ID:
                title = getResources().getString(R.string.settings_title);
                break;
            default:
                title = getResources().getString(R.string.unspecified_title);
                break;
        }
        mToolbar.setTitle(title);
    }

    private void updateDrawerMenu() {
        Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < menu.size(); ++i) {
            menu.getItem(i).setChecked(false);
        }
        menu.getItem(mCurrentFragment).setChecked(true);
    }
}
