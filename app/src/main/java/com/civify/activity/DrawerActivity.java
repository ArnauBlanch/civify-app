package com.civify.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.fragments.AboutFragment;
import com.civify.activity.fragments.BasicFragment;
import com.civify.activity.fragments.NavigateFragment;
import com.civify.activity.fragments.SettingsFragment;
import com.civify.activity.fragments.achievements.AchievementsFragment;
import com.civify.activity.fragments.award.AwardsFragment;
import com.civify.activity.fragments.event.EventsFragment;
import com.civify.activity.fragments.profile.ProfileFragment;
import com.civify.activity.fragments.wall.WallFragment;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserAttacher;
import com.civify.model.AchievementsEventsContainer;
import com.civify.service.AchievementsEventsCallback;
import com.civify.utils.AdapterFactory;

import java.util.Stack;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int NAVIGATE_ID = 0;
    public static final int WALL_ID = 1;
    public static final int PROFILE_ID = 2;
    public static final int REWARDS_ID = 3;
    public static final int ACHIEVEMENTS_ID = 4;
    public static final int EVENTS_ID = 5;
    public static final int SETTINGS_ID = 6;
    public static final int DETAILS_ISSUE_ID = 7;
    public static final int DETAILS_AWARD_ID = 8;
    public static final int DETAILS_QR_ID = 9;
    public static final int EXCHANGED_AWARDS = 10;
    public static final int DETAILS_ACHIEVEMENTS_ID = 11;
    public static final int DETAILS_EVENTS_ID = 12;
    public static final int ABOUT_ID = 13;

    private static final int DEFAULT_ELEVATION = 6;
    private static final int SHOW_AS_ACTION_IF_ROOM = 1;
    private static final int SHOW_AS_ACTION_NEVER = 0;
    private static final String SPACE = " ";
    private static final String DOT = ".";

    private Stack<Fragment> mFragmentStack;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private Fragment mCurrentFragment;
    private int mCurrentFragmentId;

    private boolean mShowMenu;
    private boolean mShowMenuDetails;
    private boolean mShowMenuWall;
    private boolean mShowMenuNavigate;

    public int getCurrentFragmentId() {
        return mCurrentFragmentId;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.navigate_title));
        setSupportActionBar(mToolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.bar_layout);
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
        mNavigationView.getMenu().getItem(mCurrentFragmentId).setChecked(true);

        setUserHeader();
        AdapterFactory.getInstance().getUserAdapter(this).addOnCurrentUserUpdateListener(
                new Runnable() {
                @Override
                public void run() {
                    Log.d("DrawerActivityListener", "CURRENT USER UPDATE LISTENER");
                    setUserHeader();
                }
            }
        );

        checkNewAchievementsEvents();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mFragmentStack.size() > 1) {
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            if (mCurrentFragmentId != NAVIGATE_ID && mCurrentFragmentId != DETAILS_ISSUE_ID
                    && mCurrentFragmentId != DETAILS_QR_ID && mCurrentFragmentId != DETAILS_AWARD_ID
                    && mCurrentFragmentId != DETAILS_ACHIEVEMENTS_ID
                    && mCurrentFragmentId != DETAILS_EVENTS_ID) {
                BasicFragment fragment = (BasicFragment) mFragmentStack.pop();
                fragmentTransaction.remove(fragment);
                while (fragment.getFragmentId() != NAVIGATE_ID) {
                    fragment = (BasicFragment) mFragmentStack.pop();
                }
                mFragmentStack.clear();
                mFragmentStack.add(fragment);
                fragment.onResume();
                fragmentTransaction.show(fragment).commit();
                mCurrentFragmentId = NAVIGATE_ID;
                mCurrentFragment = fragment;
            } else {
                mFragmentStack.lastElement().onPause();
                fragmentTransaction.remove(mFragmentStack.pop());
                mFragmentStack.lastElement().onResume();
                BasicFragment restoredFragment = (BasicFragment) mFragmentStack.lastElement();
                fragmentTransaction.show(restoredFragment);
                fragmentTransaction.commit();
                mCurrentFragment = restoredFragment;
                mCurrentFragmentId = restoredFragment.getFragmentId();
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
            AwardsFragment awardsFragment = AwardsFragment.newInstance();
            setFragment(awardsFragment, REWARDS_ID);
        } else if (id == R.id.nav_achievements) {
            AchievementsFragment achievementsFragment = AchievementsFragment.newInstance();
            setFragment(achievementsFragment, ACHIEVEMENTS_ID);
        } else if (id == R.id.nav_events) {
            EventsFragment eventsFragment = EventsFragment.newInstance();
            setFragment(eventsFragment, EVENTS_ID);
        } else if (id == R.id.nav_settings) {
            SettingsFragment settingsFragment = SettingsFragment.newInstance();
            setFragment(settingsFragment, SETTINGS_ID);
        } else if (id == R.id.nav_about) {
            AboutFragment aboutFragment = AboutFragment.newInstance();
            setFragment(aboutFragment, ABOUT_ID);
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
            if (mCurrentFragmentId == fragmentId) mFragmentStack.pop();
        }
        mFragmentStack.push(fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        mCurrentFragmentId = fragmentId;
        mCurrentFragment = fragment;
        updateMenu();
        setToolbarTitle();
    }

    public void updateMenu() {
        switch (mCurrentFragmentId) {
            case PROFILE_ID:
                ProfileFragment profile = (ProfileFragment) mCurrentFragment;
                mShowMenu = !profile.isUserSet() || profile.isCurrentUser();
                mShowMenuDetails = false;
                mShowMenuNavigate = false;
                break;
            case DETAILS_ISSUE_ID:
                mShowMenu = true;
                mShowMenuDetails = true;
                mShowMenuWall = false;
                mShowMenuNavigate = false;
                break;
            case WALL_ID:
                mShowMenu = true;
                mShowMenuWall = true;
                mShowMenuDetails = false;
                mShowMenuNavigate = false;
                break;
            case NAVIGATE_ID:
                mShowMenu = true;
                mShowMenuWall = false;
                mShowMenuNavigate = true;
                mShowMenuDetails = false;
                break;
            default:
                mShowMenu = false;
                mShowMenuDetails = false;
                mShowMenuWall = false;
                mShowMenuNavigate = false;
                break;
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int menuRes;
        int noIcona = SHOW_AS_ACTION_NEVER;
        if (mShowMenuDetails) {
            menuRes = R.menu.details;
            noIcona = SHOW_AS_ACTION_IF_ROOM;
        } else if (mShowMenuWall) {
            menuRes = R.menu.wall;
            noIcona = SHOW_AS_ACTION_IF_ROOM;
        } else if (mShowMenuNavigate) {
            menuRes = R.menu.navigation;
            noIcona = SHOW_AS_ACTION_IF_ROOM;
        } else {
            menuRes = R.menu.profile;
        }
        getMenuInflater().inflate(menuRes, menu);

        for (int i = 0; i < menu.size(); ++i) {
            menu.getItem(i).setVisible(mShowMenu);
            menu.getItem(i).setShowAsAction(noIcona);
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DrawerActivity", "onActivityResult");
        getCurrentFragment().onActivityResult(requestCode, resultCode, data);
    }

    public void setUserHeader() {
        View headerView = mNavigationView.getHeaderView(0);

        UserAttacher.get(this, null, UserAdapter.getCurrentUser())
                .setFullName((TextView) headerView.findViewById(R.id.header_name))
                .setUsername((TextView) headerView.findViewById(R.id.header_username))
                .setLevel((TextView) headerView.findViewById(R.id.header_level))
                .setExperienceWithMax((TextView) headerView.findViewById(R.id.header_xp))
                .setProgress((ProgressBar) headerView.findViewById(R.id.header_progress))
                .setCoins((TextView) headerView.findViewById(R.id.header_coins))
                .setAvatar((ImageView) headerView.findViewById(R.id.header_image));

        if (existsCoinsOnToolbar()) showCoinsOnToolbar();
    }

    private void setToolbarTitle() {
        final String title;
        removeCoinsFromToolbar();
        int elevation = DEFAULT_ELEVATION;
        switch (mCurrentFragmentId) {
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
                showCoinsOnToolbar();
                elevation = 0;
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToolbar.setTitle(title);
            }
        });
        setToolbarElevation(elevation);
    }

    private void updateDrawerMenu() {
        Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < menu.size(); ++i) {
            menu.getItem(i).setChecked(false);
        }
        menu.getItem(mCurrentFragmentId).setChecked(true);
    }

    private void setToolbarElevation(int elevation) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setElevation(elevation);
        }
    }

    public boolean existsCoinsOnToolbar() {
        return mToolbar.findViewById(R.id.coins_with_number) != null;
    }

    public void removeCoinsFromToolbar() {
        View coins = mToolbar.findViewById(R.id.coins_with_number);
        if (coins != null) {
            mToolbar.removeView(coins);
        }
    }

    public void showCoinsOnToolbar() {
        removeCoinsFromToolbar();
        View coins = getLayoutInflater().inflate(R.layout.coins_with_number, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.END);
        ((TextView) coins.findViewById(R.id.num_coins)).setText(
                String.valueOf(UserAdapter.getCurrentUser().getCoins()));
        mToolbar.addView(coins, layoutParams);
    }

    private void checkNewAchievementsEvents() {
        AdapterFactory.getInstance().getAchievementsEventsAdapter(getApplicationContext())
                .getNewAchievementsEvents(new AchievementsEventsCallback() {
                    @Override
                    public void onSuccess(AchievementsEventsContainer achievementsEventsContainer) {
                        int a = achievementsEventsContainer.getAchievementList().size();
                        int e = achievementsEventsContainer.getEventList().size();
                        if (a > 0 || e > 0) {
                            Builder builder = new Builder(DrawerActivity.this)
                                    .setTitle(R.string.congratulations)
                                    .setMessage(getAchievementsEventsMessage(a, e))
                                    .setNegativeButton(R.string.close,
                                            new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure() { }
                });
    }

    private String getAchievementsEventsMessage(int a, int e) {
        String achievements = "";
        if (a > 0) {
            achievements += a + SPACE + getString(a > 1 ? R.string.dialog_achievements :
                    R.string.dialog_achievement);
        }

        String events = "";
        if (e > 0) {
            if (a > 0) {
                events += SPACE + getString(R.string.and) + SPACE;
            }
            events += e + SPACE + getString(e > 1 ? R.string.dialog_events : R.string.dialog_event);
        }

        return getString(R.string.you_have_completed) + SPACE + achievements + events + DOT
                + "\n\n" + getString(R.string.access_achievements_events);
    }
}
