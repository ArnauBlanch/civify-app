package com.civify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RewardContainer implements Serializable {

    @Expose(serialize = false)
    @SerializedName("rewards")
    private Reward mReward;

    public RewardContainer() { }

    public RewardContainer(Reward reward) {
        mReward = reward;
    }

    public Reward getReward() {
        return mReward;
    }
}
