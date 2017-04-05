package com.civify.civify.model;

import android.support.annotation.DrawableRes;

import com.civify.civify.R;
import com.google.gson.annotations.SerializedName;

public enum Category {
    // Senyalitació vial
    @SerializedName("road_signs")
    ROAD_SIGNS(R.drawable.ic_category_street_furniture, "road_signs"),

    // Il·lumincació
    @SerializedName("illumination")
    ILLUMINATION(R.drawable.ic_category_street_furniture, "illumination"),

    // Arbolada
    @SerializedName("grove")
    GROVE(R.drawable.ic_category_street_furniture, "grove"),

    // Mobiliari urbà
    @SerializedName("street_furniture")
    STREET_FURNITURE(R.drawable.ic_category_street_furniture, "street_furniture");

    // Escombraries i neteja

    // Transport públic

    // Suggeriments

    // Altres

    private final int mDrawable;
    private final String mCategoryString;

    Category(@DrawableRes int drawable, String categoryString) {
        this.mDrawable = drawable;
        this.mCategoryString = categoryString;
    }

    public int getDrawable() {
        return this.mDrawable;
    }

    @Override
    public String toString() {
        return mCategoryString;
    }
}
