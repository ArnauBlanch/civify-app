package com.civify.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.civify.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public enum ProfileIcon {

    @SerializedName("user_icon")
    USER(R.mipmap.profile_user, false),

    @SerializedName("boy")
    BOY(R.mipmap.profile_boy, false),

    @SerializedName("boy1")
    BOY1(R.mipmap.profile_boy1, false),

    @SerializedName("girl")
    GIRL(R.mipmap.profile_girl, false),

    @SerializedName("girl1")
    GIRL1(R.mipmap.profile_girl1, false),

    @SerializedName("man")
    MAN(R.mipmap.profile_man, false),

    @SerializedName("man1")
    MAN1(R.mipmap.profile_man1, false),

    @SerializedName("man2")
    MAN2(R.mipmap.profile_man2, false),

    @SerializedName("man3")
    MAN3(R.mipmap.profile_man3, false),

    @SerializedName("man4")
    MAN4(R.mipmap.profile_man4, false);

    @DrawableRes
    private final int mIcon;

    private boolean mSelected;

    ProfileIcon(@DrawableRes int icon, boolean selected) {
        this.mIcon = icon;
        this.mSelected = selected;
    }

    @DrawableRes
    public int getIcon() {
        return mIcon;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }
}
