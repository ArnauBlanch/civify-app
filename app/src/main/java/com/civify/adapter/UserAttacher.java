package com.civify.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.profile.ProfileFragment;
import com.civify.model.User;

public final class UserAttacher {

    private static final int PERCENT = 100;

    private final String mLevelLabel;
    private OnClickListener mShowProfile;
    private User mUser;
    private DrawerActivity mContext;
    private RelativeLayout mUserLayout;

    private UserAttacher(@NonNull DrawerActivity context, @Nullable RelativeLayout userLayout,
            @NonNull User user) {
        mContext = context;
        mLevelLabel = context.getString(R.string.level);
        mUserLayout = userLayout;
        setUser(user);
        if (userLayout != null) userLayout.setOnClickListener(mShowProfile);
    }

    @NonNull
    public static UserAttacher get(@NonNull DrawerActivity context,
            @Nullable RelativeLayout userLayout, @NonNull User user) {
        return new UserAttacher(context, userLayout, user);
    }

    @NonNull
    public static UserAttacher getFromCurrentUser(@NonNull DrawerActivity context,
            @Nullable RelativeLayout userLayout) {
        return get(context, userLayout, UserAdapter.getCurrentUser());
    }

    public UserAttacher setUser(@NonNull final User user) {
        mUser = user;
        if (mUserLayout != null) {
            mShowProfile = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment profile = ProfileFragment.newInstance(user);
                    mContext.setFragment(profile, DrawerActivity.PROFILE_ID);
                }
            };
        }
        return this;
    }

    public UserAttacher setFullName(@NonNull TextView fullName) {
        fullName.setText(mUser.getName() + ' ' + mUser.getSurname());
        fullName.setOnClickListener(mShowProfile);
        return this;
    }

    public UserAttacher setUsername(@NonNull TextView username) {
        username.setText(mUser.getUsername());
        username.setOnClickListener(mShowProfile);
        return this;
    }

    public UserAttacher setLevel(@NonNull TextView level) {
        String userLevel = Integer.toString(mUser.getLevel());
        String showLevel = mLevelLabel + ' ' + userLevel;
        level.setText(showLevel);
        level.setOnClickListener(mShowProfile);
        return this;
    }

    public UserAttacher setExperienceWithMax(@NonNull TextView xpWithMax) {
        xpWithMax.setText(Integer.toString(mUser.getExperience()) + '/' + mUser.getExperienceMax());
        xpWithMax.setOnClickListener(mShowProfile);
        return this;
    }

    public UserAttacher setProgress(@NonNull ProgressBar progressBar) {
        int progress = (int)
                (((double) mUser.getExperience() / mUser.getExperienceMax()) * PERCENT);
        progressBar.setProgress(progress);
        progressBar.setOnClickListener(mShowProfile);
        return this;
    }

    public UserAttacher setCoins(@NonNull TextView coins) {
        coins.setText(String.valueOf(mUser.getCoins()));
        coins.setOnClickListener(mShowProfile);
        return this;
    }

    public UserAttacher setAvatar(@NonNull ImageView avatar) {
        avatar.setImageResource(mUser.getProfileIcon().getIcon());
        avatar.setOnClickListener(mShowProfile);
        return this;
    }

    public UserAttacher setBadges(@NonNull TextView badges) {
        badges.setText(String.valueOf(mUser.getBadges()));
        badges.setOnClickListener(mShowProfile);
        return this;
    }

}
