package com.civify.model.issue;

import android.support.annotation.DrawableRes;

import com.civify.R;
import com.google.gson.annotations.SerializedName;

public enum Category {
    // Senyalitació vial
    @SerializedName("road_signs")
    ROAD_SIGNS(R.drawable.civify, R.drawable.civify, R.drawable.civify, "road_signs", R.string
            .road_signs),

    // Il·lumincació
    @SerializedName("illumination")
    ILLUMINATION(R.drawable.civify, R.drawable.civify, R.drawable.civify, "illumination",
            R.string.illumination),

    // Arbolada
    @SerializedName("grove")
    GROVE(R.drawable.civify, R.drawable.civify, R.drawable.civify, "grove", R.string.grove),

    // Mobiliari urbà
    @SerializedName("street_furniture")
    STREET_FURNITURE(R.drawable.civify, R.drawable.civify, R.drawable.civify, "street_furniture",
            R.string.street_furniture),

    // Escombraries i neteja
    @SerializedName("trash_and_cleaning")
    TRASH_AND_CLEANING(R.drawable.civify, R.drawable.civify, R.drawable.civify,
            "trash_and_cleaning", R.string.trash_and_cleaning),

    // Transport públic
    @SerializedName("public_transport")
    PUBLIC_TANSPORT(R.drawable.civify, R.drawable.civify, R.drawable.civify, "public_transport",
            R.string.public_transport),

    // Suggeriment
    @SerializedName("suggestion")
    SUGGESTION(R.drawable.civify, R.drawable.civify, R.drawable.civify, "suggestion",
            R.string.suggestion),

    // Altres
    @SerializedName("other")
    OTHER(R.drawable.civify, R.drawable.civify, R.drawable.civify, "other", R.string.other);

    private final int mMarker;
    private final int mMarkerSelected;
    private final int mIcon;
    private final String mCategoryString;
    private final int mName;

    Category(@DrawableRes int marker, @DrawableRes int markerSelected, @DrawableRes int icon, String
            categoryString, int name) {
        this.mMarker = marker;
        this.mMarkerSelected = markerSelected;
        this.mIcon = icon;
        this.mCategoryString = categoryString;
        this.mName = name;
    }

    public int getMarker() {
        return this.mMarker;
    }

    public int getMarkerSelected() {
        return this.mMarkerSelected;
    }

    public int getIcon() {
        return this.mIcon;
    }

    public int getName() {
        return this.mName;
    }

    @Override
    public String toString() {
        return mCategoryString;
    }
}
