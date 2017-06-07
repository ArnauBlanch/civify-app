package com.civify.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.civify.R;
import com.civify.model.User;

import java.io.Serializable;

public final class UserAttacher {

    private static final int PERCENT = 100;

    private final String mLevelLabel;
    private User mUser;

    private UserAttacher(@NonNull Context context, @NonNull User user) {
        mLevelLabel = context.getString(R.string.level);
        setUser(user);
    }

    public static UserAttacher get(@NonNull Context context, @NonNull User user) {
        return new UserAttacher(context, user);
    }

    public static UserAttacher getFromCurrentUser(@NonNull Context context) {
        return get(context, UserAdapter.getCurrentUser());
    }

    public UserAttacher setUser(@NonNull User user) {
        mUser = user;
        return this;
    }

    public UserAttacher setFullName(@NonNull TextView fullName) {
        fullName.setText(mUser.getName() + ' ' + mUser.getSurname());
        return this;
    }

    public UserAttacher setUsername(@NonNull TextView username) {
        username.setText(mUser.getUsername());
        return this;
    }

    public UserAttacher setLevel(@NonNull TextView level) {
        String userLevel = Integer.toString(mUser.getLevel());
        String showLevel = mLevelLabel + ' ' + userLevel;
        level.setText(showLevel);
        return this;
    }

    public UserAttacher setExperienceWithMax(@NonNull TextView xpWithMax) {
        xpWithMax.setText(Integer.toString(mUser.getExperience()) + '/' + mUser.getExperienceMax());
        return this;
    }

    public UserAttacher setProgress(@NonNull ProgressBar progressBar) {
        int progress = (int)
                (((double) mUser.getExperience() / mUser.getExperienceMax()) * PERCENT);
        progressBar.setProgress(progress);
        return this;
    }

    public UserAttacher setCoins(@NonNull TextView coins) {
        coins.setText(String.valueOf(mUser.getCoins()));
        return this;
    }

    public UserAttacher setAvatar(@NonNull ImageView avatar) {
        avatar.setImageResource(mUser.getProfileIcon().getIcon());
        return this;
    }

}
