package com.civify.model.issue;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.civify.R;
import com.google.gson.annotations.SerializedName;

public enum Category {
    // Senyalitació vial
    @SerializedName("road_signs")
    ROAD_SIGNS(R.drawable.traffic_signs_lights_pin, R.drawable.traffic_signs_lights_icon,
            R.drawable.traffic_signs_lights_round, R.string.road_signs),

    // Il·lumincació
    @SerializedName("illumination")
    ILLUMINATION(R.drawable.street_lights_pin, R.drawable.street_lights_icon,
            R.drawable.street_lights_round, R.string.illumination),

    // Arbres i plantes
    @SerializedName("grove")
    GROVE(R.drawable.trees_and_plants_pin, R.drawable.trees_and_plants_icon,
            R.drawable.trees_and_plants_round, R.string.grove),

    // Mobiliari urbà
    @SerializedName("street_furniture")
    STREET_FURNITURE(R.drawable.urban_furniture_pin, R.drawable.urban_furniture_icon,
            R.drawable.urban_furniture_round, R.string.street_furniture),

    // Escombraries i neteja
    @SerializedName("trash_and_cleaning")
    TRASH_AND_CLEANING(R.drawable.trash_and_cleaning_pin, R.drawable.trash_and_cleaning_icon,
            R.drawable.trash_and_cleaning_round, R.string.trash_and_cleaning),

    // Transport públic
    @SerializedName("public_transport")
    PUBLIC_TANSPORT(R.drawable.public_transportation_pin, R.drawable.public_transportation_icon,
            R.drawable.public_transportation_round, R.string.public_transport),

    // Suggeriment
    @SerializedName("suggestion")
    SUGGESTION(R.drawable.suggestion_pin, R.drawable.suggestion_icon,
            R.drawable.suggestion_round, R.string.suggestion),

    // Altres
    @SerializedName("other")
    OTHER(R.drawable.others_pin, R.drawable.others_icon,
            R.drawable.others_round, R.string.other);

    @DrawableRes
    private final int mMarker;
    @DrawableRes
    private final int mIcon;
    @DrawableRes
    private final int mCircleIcon;
    @StringRes
    private final int mName;

    Category(@DrawableRes int marker, @DrawableRes int icon, @DrawableRes int circleIcon,
            @StringRes int name) {
        this.mMarker = marker;
        this.mIcon = icon;
        this.mCircleIcon = circleIcon;
        this.mName = name;
    }

    @DrawableRes
    public int getMarker() {
        return mMarker;
    }

    @DrawableRes
    public int getIcon() {
        return mIcon;
    }

    @DrawableRes
    public int getCircleIcon() {
        return mCircleIcon;
    }

    @StringRes
    public int getName() {
        return mName;
    }
}
