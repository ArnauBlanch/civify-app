package com.civify.model;

import android.support.annotation.DrawableRes;

import com.civify.R;
import com.google.gson.annotations.SerializedName;

public enum ProfileIcon {

    @SerializedName("user_icon")
    USER(R.mipmap.profile_user),

    @SerializedName("boy")
    BOY(R.mipmap.profile_boy),

    @SerializedName("boy1")
    BOY1(R.mipmap.profile_boy1),

    @SerializedName("girl")
    GIRL(R.mipmap.profile_girl),

    @SerializedName("girl1")
    GIRL1(R.mipmap.profile_girl1),

    @SerializedName("man")
    MAN(R.mipmap.profile_man),

    @SerializedName("man1")
    MAN1(R.mipmap.profile_man1),

    @SerializedName("man2")
    MAN2(R.mipmap.profile_man2),

    @SerializedName("man3")
    MAN3(R.mipmap.profile_man3),

    @SerializedName("man4")
    MAN4(R.mipmap.profile_man4);

    @DrawableRes
    private final int mIcon;

    ProfileIcon(@DrawableRes int icon) {
        this.mIcon = icon;
    }

    @DrawableRes
    public int getIcon() {
        return mIcon;
    }
}
