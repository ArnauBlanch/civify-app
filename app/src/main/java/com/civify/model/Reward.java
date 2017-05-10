package com.civify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reward implements Serializable {

    @Expose(serialize = false)
    @SerializedName("coins")
    private int mCoins;

    @Expose(serialize = false)
    @SerializedName("xp")
    private int mExperience;

    public Reward() { }

    public Reward(int coins, int experience) {
        mCoins = coins;
        mExperience = experience;
    }

    public int getCoins() {
        return mCoins;
    }

    public int getExperience() {
        return mExperience;
    }
}
