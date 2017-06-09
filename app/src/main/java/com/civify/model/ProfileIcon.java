package com.civify.model;

import android.support.annotation.DrawableRes;

import com.civify.R;
import com.google.gson.annotations.SerializedName;

public enum ProfileIcon {

    @SerializedName("user_icon")
    USER(R.drawable.profile_user),

    @SerializedName("boy")
    BOY(R.drawable.profile_boy),

    @SerializedName("boy1")
    BOY1(R.drawable.profile_boy1),

    @SerializedName("girl")
    GIRL(R.drawable.profile_girl),

    @SerializedName("girl1")
    GIRL1(R.drawable.profile_girl1),

    @SerializedName("man")
    MAN(R.drawable.profile_man),

    @SerializedName("man1")
    MAN1(R.drawable.profile_man1),

    @SerializedName("man2")
    MAN2(R.drawable.profile_man2),

    @SerializedName("man3")
    MAN3(R.drawable.profile_man3),

    @SerializedName("man4")
    MAN4(R.drawable.profile_man4);

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
